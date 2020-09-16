package com.jiwen.cluster;

import java.util.ArrayList;
import java.util.List;

public class DataPoint {
    String dataPointName; // 样本点名
    Cluster cluster; // 样本点所属类簇
    private double dimensioin[]; // 样本点的维度
    List<DataPoint> children = new ArrayList<>();
    int index;

    public DataPoint() {

    }

    public DataPoint(double[] dimensioin, String dataPointName) {
        this.dataPointName = dataPointName;
        this.dimensioin = dimensioin;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<DataPoint> getChildren() {
        return children;
    }

    public void setChildren(List<DataPoint> children) {
        this.children = children;
    }

    public double[] getDimensioin() {
        return dimensioin;
    }

    public void setDimensioin(double[] dimensioin) {
        this.dimensioin = dimensioin;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public String getDataPointName() {
        return dataPointName;
    }

    public void setDataPointName(String dataPointName) {
        this.dataPointName = dataPointName;
    }
}