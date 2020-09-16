package com.jiwen.launcher;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.jiwen.cluster.Cluster;
import com.jiwen.mainview.DSMTreeModel;
import com.jiwen.model.DesignStructureMatrix;
import com.jiwen.model.DesignStructureMatrixRepresentation;
import com.jiwen.model.MatrixElement;
import com.jiwen.utils.ConvertJsonToDSM;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/** 拿到包视图  这边也用cluster来存储好了*/
public class PackageView {
    static String jarPath = "D:\\IdeaProjects\\StructrualView\\src\\main\\resources\\depends.jar";
    static String out = "D:\\IdeaProjects\\StructrualView\\src\\main\\resources\\out";
    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "D:\\javaprojects\\SpringMVCDemo-master\\";
        File file = new File(filePath);
        /** 虚拟的根节点 **/
        Cluster root = new Cluster();
        root.setClusterName("root");

        File[] files = file.listFiles();

        List<Cluster> childrenForNode = findChildrenForNode(files);

        Cluster originalCluster = childrenForNode.get(0);

        if(childrenForNode.size() == 1){
            while(originalCluster.getChildren().size() == 1){
                originalCluster = originalCluster.getChildren().get(0);
            }
            originalCluster.setClusterName("root");
        }



        System.out.println(1);

        /** 拿到depends的json **/
        /*
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

        String jsonPath = out + "\\" + a + ".json";
        ArrayList<MatrixElement> elements = ConvertJsonToDSM.convertJsonToElements(jsonPath);

        System.out.println(1);*/


    }

    private static List<Cluster> findChildrenForNode(File[] files) {
        List<Cluster> res = new ArrayList<>();
        for(int i =0;i < files.length;i++){
            File file = files[i];
            if(file.isFile()){
                String fileName = file.getName();
                int location = fileName.lastIndexOf(".");
                if(location != -1) {
                    String fileType = fileName.substring(location, fileName.length());
                    if (fileType.equals(".java")) {
                        Cluster cluster = new Cluster();
                        cluster.setClusterName(file.getAbsolutePath());
                        res.add(cluster);
                    }
                }
            }else{
                File[] filesList = file.listFiles();
                List<Cluster> childrenForNode = findChildrenForNode(filesList);
                if(childrenForNode.size() != 0){
                    Cluster cluster = new Cluster();
                    cluster.setClusterName(file.getName());
                    cluster.setChildren(childrenForNode);
                    res.add(cluster);
                }
            }
        }
        return res;
    }
}
