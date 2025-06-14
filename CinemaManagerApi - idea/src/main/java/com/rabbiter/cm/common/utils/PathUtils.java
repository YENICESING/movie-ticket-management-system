package com.rabbiter.cm.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
///////这个 PathUtils 类的 getClassLoadRootPath 方法旨在获取项目的根路径，并根据不同的操作系统进行路径调整
public class PathUtils {
    public static String getClassLoadRootPath() {
        String path = "";
        try {
            String prePath = URLDecoder.decode(PathUtils.class.getClassLoader().getResource("").getPath(),"utf-8").replace("/target/classes", "");
            String osName = System.getProperty("os.name");
            if (osName.toLowerCase().startsWith("mac")) {
                // 苹果
                path = prePath.substring(0, prePath.length() - 1);
            } else if (osName.toLowerCase().startsWith("windows")) {
                // windows
                path = prePath.substring(1, prePath.length() - 1);
            } else if(osName.toLowerCase().startsWith("linux") || osName.toLowerCase().startsWith("unix")) {
                // unix or linux
                path = prePath.substring(0, prePath.length() - 1);
            } else {
                path = prePath.substring(1, prePath.length() - 1);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return path;
    }

}
