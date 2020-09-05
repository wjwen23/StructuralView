package com.jiwen.launcher;

import com.jiwen.cluster.Cluster;
import com.jiwen.cluster.ClusterAnalysis;
import com.jiwen.cluster.DataPoint;
import com.jiwen.model.MatrixDependencyGroup;
import com.jiwen.model.MatrixElement;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class BatReadDSM extends Object  {
    static String out = "C:\\Users\\22166\\Desktop\\dsmviewer\\dsmviewer\\src\\resources\\out";

    public static int[][] getMatrix(List<MatrixElement> elements,HashMap<String,Integer> map){
        int[][] res = new int[elements.size()][elements.size()];
        
        for (int i = 0; i < elements.size(); i++) {
            ArrayList<MatrixDependencyGroup> dependencylist = elements.get(i).getDependencies();
            for(MatrixDependencyGroup dependency :dependencylist){
                int start = map.get(dependency.getSource().getName());
                int end = map.get(dependency.getDestination().getName());
                res[start][end]+=dependency.getTotalDependencies();
            }
        }
        return res;

    }
    public static void main(String[] args) {

        File f = new File(out);
        File[] files = f.listFiles();
        for(File file:files){
            if(file.isFile()){
                String[] temp = file.getAbsolutePath().split("\\.");
                if(temp[temp.length - 1].equals( "json")){
                    /** 得到两个指标 */
                    double[] get = new double[2];
                    get = ReadDSM.helpRead(file.getAbsolutePath());
                    System.out.println("处理的文件：" + file.getAbsolutePath());
                    System.out.print("指标1:" + get[0] + " ");
                    System.out.println("指标2:" + get[1]);
                    List<MatrixElement> elements = new ArrayList<>();

                    elements = ReadDSM.getElements(file.getAbsolutePath());
                    HashMap<String,Integer> map = new HashMap<>();
                    int cnt = 0;
                    for(MatrixElement element: elements){
                        map.put(element.getName(),cnt);
                        cnt++;
                    }
                    int groupnum = 0;
                    HashSet<Integer> set = new HashSet<>();
                    for(MatrixElement element:elements){
                        if(set.contains(element.getGroupid())){
                            continue;
                        }else{
                            set.add(element.getGroupid());
                            groupnum++;
                        }
                    }
                    int[][] elementmatrix = new int[elements.size()][elements.size()];
                    elementmatrix = getMatrix(elements,map);

                    /** 打印elementmatrix */
                    /*
                    for(int i = 0;i<elementmatrix.length;i++){
                        for(int j = 0;j<elementmatrix[i].length;j++){
                            if(j == elementmatrix[i].length - 1){
                                System.out.println(elementmatrix[i][j]);
                                continue;
                            }
                            System.out.print(elementmatrix[i][j] + " ");
                        }
                    }
                    */
                    ClusterAnalysis ca = new ClusterAnalysis();
                    List<Cluster> clusters = ca.helpCluster(elementmatrix,groupnum);
                    int k = 1;
                    for(Cluster cl:clusters){
                        System.out.println("------Cluster: No."+k+"------");
                        k++;
                        List<DataPoint> tempDps=cl.getDataPoints();
                        for(DataPoint tempdp:tempDps){
                            System.out.println(tempdp.getDataPointName());
                        }
                    }
                    System.out.println("-----------------------分隔符-----------------------------");


                }
            }
        }
    }
}
