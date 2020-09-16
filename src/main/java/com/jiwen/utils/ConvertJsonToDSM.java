package com.jiwen.utils;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.jiwen.model.MatrixDependencyGroup;
import com.jiwen.model.MatrixElement;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangjiwen
 * @create 2020/9/2
 */

/**
 * step2
 * 将表示依赖的json转为dsm矩阵
 */
public class ConvertJsonToDSM {


    public static ArrayList<MatrixElement> convertJsonToElements(String filename){
        ArrayList<MatrixElement> elements = new ArrayList<MatrixElement>();

        try  {

            Reader reader = new FileReader(filename);


            // Convert JSON File to Java Object
            JsonReader jsonReader = new JsonReader(reader);


            jsonReader.beginObject();

            // consume "schemaVersion" : "1.0"
            jsonReader.nextName();
            jsonReader.nextString();

            // consume "name" : "dependency-sdsm"
            jsonReader.nextName();
            jsonReader.nextString();

            jsonReader.nextName(); // consume "variables"
            jsonReader.beginArray();

            // filenames
            JsonToken token = jsonReader.peek();

            int index = 0;
            while(token!=JsonToken.END_ARRAY) {
                String value = jsonReader.nextString();
                elements.add(new MatrixElement(value, index++)); // consume file name
                token = jsonReader.peek();
            }
            jsonReader.endArray();

            jsonReader.nextName(); // consume "cells"

            jsonReader.beginArray();

            token = jsonReader.peek();

            while(token!=JsonToken.END_ARRAY) {
                jsonReader.beginObject();

                jsonReader.nextName(); // consume "src"
                int row = jsonReader.nextInt();

                jsonReader.nextName(); // consume "dest"
                int column = jsonReader.nextInt();

                jsonReader.nextName(); // consume "values"
                jsonReader.beginObject();

                MatrixElement source = elements.get(row);
                MatrixElement destination = elements.get(column);
                MatrixDependencyGroup dependencies = new MatrixDependencyGroup(source,destination);
                source.addDependency(dependencies);

                while(token!=JsonToken.END_OBJECT) {

                    String dependencyType = jsonReader.nextName(); // consume dependency type
                    int intValue = jsonReader.nextInt(); // consume value

                    dependencies.addDependency(dependencyType, intValue);

                    token = jsonReader.peek();
                }

                jsonReader.endObject();

                jsonReader.endObject();
                token = jsonReader.peek();
            }

            jsonReader.endArray();
            jsonReader.endObject();

            jsonReader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        /** 区分group */

        HashMap<String,Integer> map = new HashMap<>();
        for(MatrixElement me: elements){
            //String temp = me.getName().split(".")[0];
            String a = me.getName();
            String[] ak = a.split("\\.");
            StringBuilder sb = new StringBuilder();
            for(int i = 0;i<ak.length - 1;i++){
                sb.append(ak[i]);
            }
            String temp = sb.toString();
            if(map.containsKey(temp)){
                me.setGroupid(map.get(temp));
            }else{
                map.put(temp,map.size());
                me.setGroupid(map.get(temp));
            }

        }

        return elements;


        /**将elementList 变为矩阵或者是list*/
/*        int size = elements.size();
        int[][] DSM = new int[size][size];

        return convertElementListToArrays(elements);*/
    }



    private static int[][] convertElementListToArrays(ArrayList<MatrixElement> elements) {
        int size = elements.size();
        int[][] ret = new int[size][size];
        for(int i = 0;i<size;i++){
            ArrayList<MatrixDependencyGroup> dependencies = elements.get(i).getDependencies();
            for (MatrixDependencyGroup dependency : dependencies) {
                int sourceId = dependency.getSource().getIndex();
                int destinationId = dependency.getDestination().getIndex();
                ret[sourceId][destinationId] = dependency.getTotalDependencies();
            }
        }
        return ret;
    }

    public static int[][] convertElementsToDSM(ArrayList<MatrixElement> elements) {
        /**将elementList 变为矩阵或者是list*/
        int size = elements.size();
        int[][] DSM = new int[size][size];

        return convertElementListToArrays(elements);
    }

    public static int getNumFromElement(ArrayList<MatrixElement> elements) {
        int res = 0;
        Set<Integer> set = new HashSet<>();
        for(int i = 0;i<elements.size();i++){
            int temp = elements.get(i).getGroupid();
            if(!set.contains(temp)){
                set.add(temp);
                res++;
            }
        }
        return res;
    }
}
