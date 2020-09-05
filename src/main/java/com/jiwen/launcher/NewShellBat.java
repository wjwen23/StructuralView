package com.jiwen.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NewShellBat {
    static String jarPath = "D:\\gitpoj\\java\\StructrualView\\src\\main\\resources\\depends.jar";
    static String out = "D:\\gitpoj\\java\\StructrualView\\src\\main\\resources\\out";
    private static long getTotalSizeOfFilesInDir(final File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getTotalSizeOfFilesInDir(child);
        return total;
    }

    public static boolean isBigFile(File file){
        if (file.isFile()) {
            System.out.println(file.length());
            return file.length() >= 1024 * 1000 * 5;
        }
        final File[] children = file.listFiles();
        //long total = 0;
        if (children != null) {
            for (final File child : children) {
                if (isBigFile(child)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void main(String[] args) {
        String Filepath = "C:\\Users\\22166\\out";
        File file = new File(Filepath);
        File file1 = new File(Filepath);

        File[] files = file.listFiles();
        //final ExecutorService exec = Executors.newFixedThreadPool(1);

        for(File f:files){
            if(f.isDirectory()){
                long filesize = getTotalSizeOfFilesInDir(f);
                //boolean isbigfile = isBigFile(f);

                System.out.println(filesize);
                if(filesize < 1024 * 1000 * 20){
                    String k = f.getAbsolutePath();
                    String[] temp = k.split("\\\\");

                    String a = temp[temp.length - 1];
                    String cmd = "java -jar " + jarPath  + " -d " + out + " -p dot java " + k + " " + " " + a;
                    //System.out.println(cmd);
                    try {
                        //Thread.sleep(5000);

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
        }
    }

}
