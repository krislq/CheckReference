
package com.krislq.check;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileUtils {

    /***
     * @author jack shy
     * @function:处理扫出的无效资源或者引用接口工具类
     * @date:2015-04-09
     */
    private static final boolean DEBUG = true;

    private static final String TAG = "[FileUtils]";

    /**
     * line read text to list
     * 
     * @param filePath
     * @param list
     * @return
     */
    public static boolean readFileTxt2List(String filePath, List<String> list) {
        if (DEBUG) {
            System.out.println(TAG + " readFileTxt2List: " + filePath);
        }

        boolean ret = false;
        File stringFile = new File(filePath);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(stringFile));
            String lineStr = null;
            while ((lineStr = reader.readLine()) != null) {
                // line read
                list.add(lineStr);
            }
            reader.close();
            ret = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        return ret;
    }

    /***
     * line write text from list
     * 
     * @param filePath
     * @param list
     * @return
     */
    public static boolean wirteList2File(String filePath, List<String> list) {
        if (DEBUG) {
            System.out.println(TAG + " wirteList2File " + filePath);
        }
        boolean ret = false;
        FileWriter writer = null;
        File file = null;
        try {
            // append
            file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            writer = new FileWriter(file, true);
            for (String content : list) {
                writer.write(content + "\n");
            }

            ret = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            }
        }
        return ret;
    }

    /***
     * del file by full name
     * 
     * @param srcDirPath
     * @param fileFullName
     */
    public static void delFilebyFullName(String srcDirPath, String fileFullName) {
        if (DEBUG) {
            System.out.println(TAG + " delFilebyFullName " + fileFullName);
        }

        File file = new File(srcDirPath);
        File[] fs = file.listFiles();

        for (int i = 0; i < fs.length; i++) {

            String name = fs[i].getName();
            if (name.equals(fileFullName)) {
                if (fs[i].delete()) {
                    if (DEBUG) {
                        System.out.println(TAG + " delFilebyFullName " + fileFullName + "success");
                    }
                }

                return;
            }
        }
    }

    /***
     * remove file by full name
     * 
     * @param srcDirPath
     * @param destDirPath
     * @param fileFullName
     */
    public static void removeFileByFullName(String srcDirPath, String destDirPath, String fileFullName) {
        if (DEBUG) {
            System.out.println(TAG + " removeFileByFullName " + fileFullName);
        }

        File file = new File(srcDirPath);
        File[] fs = file.listFiles();

        File file2 = new File(destDirPath);
        if (!file2.exists()) {
            if (DEBUG) {
                System.out.println(TAG + " mkdir: " + destDirPath);
            }
            file2.mkdirs();
        }

        for (int i = 0; i < fs.length; i++) {

            String name = fs[i].getName();
            if (name.equals(fileFullName)) {
                fs[i].renameTo(new File(destDirPath + "\\" + name));
                return;
            }
        }
    }

    /***
     * copy file by full name to dest dir
     * 
     * @param srcDirPath
     * @param destDirPath
     * @param fileFullName
     */
    public synchronized static void copyFileByFullName(String srcDirPath, String destDirPath, String fileFullName) {
        if (DEBUG) {
            System.out.println(TAG + " copyFileByFullName " + fileFullName);
        }

        File file2 = new File(destDirPath);
        if (!file2.exists()) {
            if (DEBUG) {
                System.out.println(TAG + " mkdir: " + destDirPath);
            }
            file2.mkdirs();
        }

        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int byteread = 0;
            File oldfile = new File(srcDirPath + "\\" + fileFullName);
            if (oldfile.exists()) {

                inStream = new FileInputStream(srcDirPath + "\\" + fileFullName); // 读入原文件
                fs = new FileOutputStream(destDirPath + "\\" + fileFullName);
                byte[] buffer = new byte[2048];
                while ((byteread = inStream.read(buffer)) != -1) {

                    fs.write(buffer, 0, byteread);
                }

                System.err.println("success");
            }
        } catch (Exception e) {
            if (DEBUG) {
                System.err.println("err:" + e);
            }
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e1) {
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e1) {
                }
            }
        }

    }

}
