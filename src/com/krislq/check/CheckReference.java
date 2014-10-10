package com.krislq.check;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class CheckReference {
    public static final String PROJECT_PATH = "D:\\Projects\\HelloWorld";
    public static void main(String[] args) {
        CheckReference cr = new CheckReference();
        cr.start();
    }
    
    public CheckReference() {
        
    }
    
    public void start() {
        new Thread(){
            @Override
            public void run() {
                File rootFile = new File(PROJECT_PATH);
                if(!rootFile.exists()) {
                    System.out.println("Project is no exist");
                }

                System.out.println("----begin scan layout reference----");
                List<String> layoutRefrences = readLayoutReference();
                if(layoutRefrences!=null && !layoutRefrences.isEmpty()) {
                    for(String s:layoutRefrences) {
                        boolean ret = findLayoutRefrence(s);
                        if(!ret) {
                            System.out.println("[Layout - NOREFER] "+s);
                        }
                    }
                }
                
                System.out.println("----begin scan string reference----");
                List<String> stringRefrences = readStringReference();
                if(stringRefrences!=null && !stringRefrences.isEmpty()) {
                    for(String s:stringRefrences) {
                        boolean ret = findStringRefrence(s);
                        if(!ret) {
                            System.out.println("[String - NOREFER] "+s);
                        }
                    }
                }

                System.out.println("----begin scan drawable reference----");
                List<Drawable> drawableRefrences = readDrawableReference();
                if(drawableRefrences!=null && !drawableRefrences.isEmpty()) {
                    for(Drawable s:drawableRefrences) {
                        boolean ret = findDrawableRefrence(s);
                        if(!ret) {
                            System.out.println("[Drawable - NOREFER] "+s);
                        }
                    }
                }
                System.out.println("----scan over----");
            }
            
        }.start();
    }
    

    private List<String> readLayoutReference() {
        List<String> layoutRefrences = new ArrayList<String>();

        File layoutFile = new File(PROJECT_PATH+"\\res\\layout");
        if(layoutFile.exists() && layoutFile.isDirectory()) {
            File[] layoutFiles = layoutFile.listFiles();
            if(layoutFiles!=null) {
                for(File f:layoutFiles) {
                    if(f.isFile()) {
                        String name = f.getName();

                        int dotIndex = name.indexOf(".");
                        if(dotIndex>0) {
                            String layoutname = name.substring(0, dotIndex);
                            layoutRefrences.add(layoutname);
                        }
                    }
                }
            }
        } else {
            System.out.println("Layout directory is no exist");
        }
        return layoutRefrences;
    }

    private boolean findLayoutRefrence(String s) {
        File srcfile = new File(PROJECT_PATH+"\\src");
        boolean ret = findRefrenceInFile(srcfile, "R.layout."+s);
        if(!ret) {

            File layoutfile = new File(PROJECT_PATH+"\\res\\layout");
            ret = findRefrenceInFile(layoutfile, "@layout/"+s);
        }
        return ret;
    }

    private List<Drawable> readDrawableReference() {
        List<Drawable> drawableRefrences = new ArrayList<Drawable>();
        File drawableFile = new File(PROJECT_PATH+"\\res");
        if(drawableFile.exists() && drawableFile.isDirectory()) {
            File[] drawableDirs = drawableFile.listFiles();
            if(drawableDirs!=null) {
                for(File d:drawableDirs) {
                    String dirname = d.getName();
                    if(dirname.startsWith("drawable")) {
                        File[] drawableFiles = d.listFiles();
                        if(drawableFiles!=null) {
                            for(File f:drawableFiles) {
                                String name = f.getName();
                                int dotIndex = name.indexOf(".");
                                if(dotIndex>0) {
                                    String drawablename = name.substring(0, dotIndex);
                                    Drawable drawable = new Drawable();
                                    drawable.setName(drawablename);
                                    drawable.setParentDirName(dirname);
                                    drawable.setFileName(name);
                                    drawable.setPath(f.getAbsolutePath());
                                    if(drawableRefrences.contains(name)) {
                                        System.out.println("[Drawable - REPEAT] "+drawable);
                                    } else {
                                        drawableRefrences.add(drawable);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("Drawable files is no exist");
        }
        return drawableRefrences;
    }

    private boolean findDrawableRefrence(Drawable s) {
        File srcfile = new File(PROJECT_PATH+"\\src");
        boolean ret = findRefrenceInFile(srcfile, "R.drawable."+s.getName());
        if(!ret) {
            File layoutfile = new File(PROJECT_PATH+"\\res\\layout");
            ret = findRefrenceInFile(layoutfile, "@drawable/"+s.getName());
        }
        if(!ret) {
            File layoutfile = new File(PROJECT_PATH+"\\res\\anim");
            ret = findRefrenceInFile(layoutfile, "@drawable/"+s.getName());
        }
        if(!ret) {
            File layoutfile = new File(PROJECT_PATH+"\\res\\drawable");
            ret = findRefrenceInFile(layoutfile, "@drawable/"+s.getName());
        }
        if(!ret) {
            //看看在style ，theme中有没有用到
            File layoutfile = new File(PROJECT_PATH+"\\res\\values");
            ret = findRefrenceInFile(layoutfile, "@drawable/"+s.getName());
        }
        if(!ret) {
            //看看在manifest中有没有用到
            File layoutfile = new File(PROJECT_PATH+"\\AndroidManifest.xml");
            ret = findRefrenceInFile(layoutfile, "@drawable/"+s.getName());
        }
        return ret;
    }
    
    private List<String> readStringReference() {

        List<String> stringRefrences = new ArrayList<String>();
        SAXReader reader = new SAXReader();

        File stringFile = new File(PROJECT_PATH+"\\res\\values\\strings.xml");
        if(stringFile.exists()) {
            try {
                Document document = reader.read(stringFile);// 读取XML文件
                Element root = document.getRootElement();// 得到根节点
                for(@SuppressWarnings("unchecked")
                Iterator<Element> i = root.elementIterator();i.hasNext();) {
                    Element e = i.next();
                    String name = e.attributeValue("name");
                    if(stringRefrences.contains(name)) {
                        System.out.println("[String - REPEAT] "+name);
                    } else {
                        stringRefrences.add(name);
                    }
                }
            } catch (DocumentException e) {
            }
        } else {
            System.out.println("String file is no exist");
        }
        return stringRefrences;
    }
    
    private boolean findStringRefrence(String s) {
        File srcfile = new File(PROJECT_PATH+"\\src");
        boolean ret = findRefrenceInFile(srcfile, "R.string."+s);
        if(!ret) {

            File layoutfile = new File(PROJECT_PATH+"\\res\\layout");
            ret = findRefrenceInFile(layoutfile, "@string/"+s);
        }
        if(!ret) {
            //看看在manifest中有没有用到
            File layoutfile = new File(PROJECT_PATH+"\\AndroidManifest.xml");
            ret = findRefrenceInFile(layoutfile, "@string/"+s);
        }
        return ret;
    }

    private boolean findRefrenceInFile(File file,String find) {
        boolean ret = false;
        if(file.exists()) {
            if(file.isDirectory()) {
                File[] files = file.listFiles();
                if(files!=null) {
                    for(File f:files) {
                        ret = findRefrenceInFile(f, find);
                        if(ret) {
                            //如果能找到一个了，就直接返回了。
                            break;
                        }
                    }
                }
            } else {
                //is file
                try {
                    FileReader fileReader = new FileReader(file);
                    BufferedReader reader = new BufferedReader(fileReader);
                    String s;
                    while ((s = reader.readLine()) != null) {
                        ret = s.contains(find);
                         if(ret) {
                             break;
                         }
                    }
                    reader.close();
                    fileReader.close();
                } catch (IOException e) {
                }
            }
        }
        return ret;
    }
    
    static class Drawable {
        private String name;
        private String path;
        private String fileName;
        private String parentDirName;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getPath() {
            return path;
        }
        public void setPath(String path) {
            this.path = path;
        }
        public String getFileName() {
            return fileName;
        }
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        public String getParentDirName() {
            return parentDirName;
        }
        public void setParentDirName(String parentDirName) {
            this.parentDirName = parentDirName;
        }
        public String toString() {
            return name +" -> "+ parentDirName;
        }
        @Override
        public boolean equals(Object obj) {
            if(name==null) return false;
            return name.equals(((Drawable)obj).getName());
        }
        
    }
    
}
