package com.jiwen.launcher;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.jiwen.model.MatrixDependencyGroup;
import com.jiwen.model.MatrixElement;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReadDSM {
    public static double[] help_1(ArrayList<MatrixElement> elements){
        int All_count = 0;
        int Cross_count = 0;
        int Cross_num = 0;
        //double[] res = new double[2];
        for(int i = 0;i<elements.size();i++){
            ArrayList<MatrixDependencyGroup> depenarray = elements.get(i).getDependencies();
            for(MatrixDependencyGroup temp:depenarray){
                for(Map.Entry<String,Integer> entry: temp.getDependencies().entrySet()){
                    All_count += entry.getValue();
                }
                //All_count += temp.getDependencies().size();
                if(temp.getSource().getGroupid() != temp.getDestination().getGroupid()){
                    Cross_num++;
                    for(Map.Entry<String,Integer> entry:temp.getDependencies().entrySet()){
                        Cross_count += entry.getValue();
                    }
                    //Cross_count += temp.getDependencies().size();
                }
            }
        }
        double[] res = new double[2];
        res[0] = (double) Cross_count/All_count;
        res[1] = (double) Cross_num/(elements.size() * elements.size());
        return res;
    }
    public static ArrayList<MatrixElement> getElements(String filename){
        ArrayList <MatrixElement> elements = new ArrayList<MatrixElement>();

        //String filename = "D:\\迅雷下载\\depends-0.9.3\\yui.json";

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

            while(token!=JsonToken.END_ARRAY) {
                String value = jsonReader.nextString();
                elements.add(new MatrixElement(value)); // consume file name
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
    }
    public static double[] helpRead(String filename){
        ArrayList <MatrixElement> elements = new ArrayList<MatrixElement>();

        //String filename = "D:\\迅雷下载\\depends-0.9.3\\yui.json";

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

            while(token!=JsonToken.END_ARRAY) {
                String value = jsonReader.nextString();
                elements.add(new MatrixElement(value)); // consume file name
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

        //DesignStructureMatrix dsm  = new DesignStructureMatrix(filename, elements);


        //HashSet<String> M = new HashSet<>();
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
        return help_1(elements);
        //double[] bench = help_1(elements);
    }

    public static void main(String[] args) throws IOException {
        ArrayList <MatrixElement> elements = new ArrayList<MatrixElement>();

        String filename = "C:\\Users\\22166\\Desktop\\dsmviewer\\dsmviewer\\src\\resources\\out\\SpringMVCDemo-master.json";

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

            while(token!=JsonToken.END_ARRAY) {
                String value = jsonReader.nextString();
                elements.add(new MatrixElement(value)); // consume file name
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

        //DesignStructureMatrix dsm  = new DesignStructureMatrix(filename, elements);


        //HashSet<String> M = new HashSet<>();
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
        double[] bench = help_1(elements);
        //int bench_2 = help_2(elements);



        /*String a = elements.get(0).getName();
        String[] ak = a.split("\\.");
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<ak.length - 1;i++){
            sb.append(ak[i]);
        }*/
        /*File dic = new File(a);
        String aaa = dic.getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        File temp = new File(aaa);
        while (temp.getParentFile() != null && temp.getParentFile().getName().length() != 0) {
            sb.insert(0, "/" + temp.getParentFile().getName());
            temp = temp.getParentFile();
        }
        sb.append("/");*/
        //System.out.println(5);
    }
}
