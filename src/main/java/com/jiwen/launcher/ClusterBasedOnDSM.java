package com.jiwen.launcher;

import com.jiwen.cluster.Cluster;
import com.jiwen.cluster.ClusterAnalysis;
import com.jiwen.cluster.DataPoint;
import com.jiwen.model.MatrixElement;
import com.jiwen.utils.ConvertJsonToDSM;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author wangjiwen
 * @create 2020/9/2
 */

public class ClusterBasedOnDSM {
    public static void main(String[] args) {
        String jsonPath = "D:\\IdeaProjects\\StructrualView\\src\\main\\resources\\out\\SpringMVCDemo-master.json";
        /** elements 包含index和类名 */
        ArrayList<MatrixElement> elements = ConvertJsonToDSM.convertJsonToElements(jsonPath);

        /** 获取包数*/
        int num = ConvertJsonToDSM.getNumFromElement(elements);

        /** DSM就是纯依赖矩阵 */
        int[][] DSM = ConvertJsonToDSM.convertElementsToDSM(elements);
        int k = DSM.length;

        List<Cluster> clusters = new ArrayList<>();
        List<DataPoint> dataPoints = new ArrayList<>();
        for(int i = 0;i<elements.size();i++){
            MatrixElement element = elements.get(i);
            Cluster cluster = new Cluster();
            double[] temp = new double[k];
            for(int x = 0;x<DSM[i].length;x++){
                temp[x] = DSM[i][x];
            }
            DataPoint dataPoint = new DataPoint(temp, element.getName());
            dataPoint.setIndex(element.getIndex());
            List<DataPoint> dataPointList = new ArrayList<>();
            dataPointList.add(dataPoint);
            cluster.setDataPoints(dataPointList);
            cluster.setClusterName(element.getName());
            dataPoint.setCluster(cluster);
            dataPoints.add(dataPoint);
            clusters.add(cluster);
        }

        ClusterAnalysis ca = new ClusterAnalysis();
        /** helpCluster的第二个参数是根据包视图来的 */
        Cluster root = ca.hAnalysis(clusters, num);

        bfs(root);

        System.out.println(1);


    }

    private static void bfs(Cluster root) {
        if(root == null){
            return;
        }
        Queue<Cluster> queue = new LinkedList<>();
        queue.add(root);
        int k = 1;
        while(!queue.isEmpty()){
            System.out.println("第" + k + "层:");
            k++;
            int size = queue.size();
            for(int i = 0;i<size;i++){
                Cluster temp = queue.poll();
                System.out.println(temp.getClusterName());
                if(temp.getChildren() != null || temp.getChildren().size() != 0){
                    List<Cluster> children = temp.getChildren();
                    for(int x = 0;x < children.size();x++){
                        queue.add(children.get(x));
                    }
                }
            }
        }
    }
}
