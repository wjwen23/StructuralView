package com.jiwen.cluster;

import weka.clusterers.HierarchicalClusterer;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;


public class HierarchicalCluster {
    int NumCluster =  0;
    Instances insdataClusterer  = null;
    Instances insdata = null;
    String path = "";
    public HierarchicalCluster(String filename){
        try{
            //获取相对路径--------------------------------------
            File directory = new File(".");
            path = directory.getCanonicalPath();
            //(1)读入样本----------------------------------------
            DataSource source = new DataSource(path+"\\AnswerData\\05"+filename+".arff");
            insdata = source.getDataSet();
            if (insdata.classIndex() == -1)
                insdata.setClassIndex(insdata.numAttributes() - 1);
            //generate data for clusterer (w/o class)
            Remove filter = new Remove();
            filter.setAttributeIndices("" + (insdata.classIndex() + 1));
            filter.setInputFormat(insdata);
            insdataClusterer = Filter.useFilter(insdata, filter);
            NumCluster = Integer.parseInt(filename.substring(filename.indexOf("_")+1));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    public void computeHierarchicalCluster(String filePath){
        String[] options = new String[2];
        options[0] = "-L";
        options[1] = "WARD";
        HierarchicalClusterer Hicluster = new HierarchicalClusterer();// new instance of EMcluster
        try {
            Hicluster.setOptions(options);     // set the options
            Hicluster.setNumClusters(NumCluster);
            Hicluster.buildClusterer(insdataClusterer);    // build the EMcluster
            for(int i=0;i<insdataClusterer.numInstances();i++){
                System.out.println(Hicluster.clusterInstance(insdataClusterer.instance(i)));
            }

        } catch (Exception e) {


        }


    }
}
