
package com.krislq.check;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.krislq.check.CheckReference.Drawable;

public class HandleChecked {
    /***
     * @author jack shy
     * @function:处理扫出的无效资源或者引用接口类
     * @date:2015-04-09
     */

    private static final boolean DEBUG = true;

    private static final String TAG = "[HandleChecked]";

    private List<String> mStrList = new ArrayList<String>();

    public HandleChecked() {

    }

    /***
     * 删除无效layout
     * 
     * @param dirPath
     * @param list
     */
    public void onDelLayout(String dirPath, List<String> list) {
        if (DEBUG) {
            System.out.println(TAG + " onDelLayout:");
        }
        if (list == null || list.size() == 0)
            return;
        for (String fullName : list) {
            // 删除该文件
            FileUtils.delFilebyFullName(dirPath, fullName);
        }

    }

    /***
     * 删除string.xml中的无效引用，针对文件本身
     * 
     * @param srcFilePath
     * @param listChecked
     */
    public void onDelStrings(String srcFilePath, List<String> listChecked) {
        onDelStrings(srcFilePath, srcFilePath, listChecked);
    }

    /***
     * 删除string.xml中的无效引用，写到指定的文件中
     * 
     * @param srcFilePath
     * @param desFilePath
     * @param listChecked
     */
    public void onDelStrings(String srcFilePath, String desFilePath, List<String> listChecked) {
        if (DEBUG) {
            System.out.println(TAG + " onDelStrings:");
        }
        if (listChecked == null || listChecked.size() == 0)
            return;
        // 1.先读取现有的string.xml数据保存
        mStrList = new ArrayList<String>();
        if (FileUtils.readFileTxt2List(srcFilePath, mStrList)) {

            // 2.从string.xml中删除无效的引用
            for (String s : listChecked) {
                Iterator<String> sListIterator = mStrList.iterator();
                while (sListIterator.hasNext()) {
                    String e = sListIterator.next();
                    if (e.contains(s)) {
                        sListIterator.remove();
                    }
                }
            }

            // 3.再把去掉的写回去
            FileUtils.wirteList2File(desFilePath, mStrList);
        }

    }

    /***
     * 删除无效的资源文件
     * 
     * @param dirPath
     * @param list
     */
    public void onDelDrawable(String dirPath, List<Drawable> list) {
        if (DEBUG) {
            System.out.println(TAG + " onDelDrawable:");
        }
        if (list == null || list.size() == 0)
            return;
        for (Drawable d : list) {
            // 删除drawable中出现的无效文件
            FileUtils.delFilebyFullName(dirPath + "\\" + d.getParentDirName(), d.getFileName());
        }
    }

    /***
     * 拷贝无效的资源文件到指定目录 做对比
     * 
     * @param srcDirPath
     * @param destDirPath
     * @param list
     */
    public void onCopyDrawable(String srcDirPath, String destDirPath, List<Drawable> list) {
        if (DEBUG) {
            System.out.println(TAG + " onCopyDrawable:");
        }
        if (list == null || list.size() == 0)
            return;
        for (Drawable d : list) {
            // copy drawable中出现的无效文件
            FileUtils.copyFileByFullName(srcDirPath + "\\" + d.getParentDirName(), destDirPath + "\\" + d.getParentDirName(), d.getFileName());
        }
    }

    /***
     * 移动无效的资源文件到指定目录
     * 
     * @param srcDirPath
     * @param destDirPath
     * @param list
     */
    public void onRemoveDrawable(String srcDirPath, String destDirPath, List<Drawable> list) {
        if (DEBUG) {
            System.out.println(TAG + " onRemoveDrawable:");
        }
        if (list == null || list.size() == 0)
            return;

        for (Drawable d : list) {
            // remove drawable中出现的无效文件 可能不同目录下有相同的文件名
            FileUtils.removeFileByFullName(srcDirPath + "\\" + d.getParentDirName(), destDirPath + "\\" + d.getParentDirName(), d.getFileName());
        }
    }

}
