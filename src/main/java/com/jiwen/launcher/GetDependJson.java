package com.jiwen.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author wangjiwen
 * @create 2020/9/2
 */

/**
 * step1 利用denpends.jar生成描述依赖的json文件
 **/
public class GetDependJson {
    static String jarPath = "D:\\IdeaProjects\\StructrualView\\src\\main\\resources\\depends.jar";
    static String out = "D:\\IdeaProjects\\StructrualView\\src\\main\\resources\\out";
    public static void main(String[] args) {
        String filePath = "C:\\Users\\22166\\out\\SpringMVCDemo-master";
        File file = new File(filePath);
        String k = file.getAbsolutePath();
        String[] temp = k.split("\\\\");

        String a = temp[temp.length - 1];
        String cmd = "java -jar " + jarPath  + " -d " + out + " -p dot java " + k + " " + " " + a;
        //System.out.println(cmd);
        try {
            Process process = Runtime.getRuntime().exec(cmd);

            InputStream is = process.getInputStream();

            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader br = new BufferedReader(isr);

            String content = br.readLine();

            while (content != null) {

                System.out.println(content);

                content = br.readLine();

            }

        } catch (Exception ie ){
            ie.printStackTrace();

        }

    }
}
