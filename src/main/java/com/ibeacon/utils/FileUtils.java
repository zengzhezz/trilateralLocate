package com.ibeacon.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtils {

    /**
     * 写数据到特定的文件中
     * @param c	需要写入的字符串
     * @param dirname	路径
     * @param filename	文件名
     * @param isAppend	是否在文件尾添加
     * @return
     */
    public static boolean writeContent(String c, String dirname, String filename,
                                       boolean isAppend) {
        File f = new File(dirname);
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(dirname + filename,
                    isAppend);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(c);
            writer.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
