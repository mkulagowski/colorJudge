package com.company;

import java.awt.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Main {

    public static void main(String[] args) {
        @SuppressWarnings("unchecked")
        JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader("C:\\Users\\mkk-1\\Downloads\\colorQuizzer-master\\colors2.json"));

                JSONArray jsonObject = (JSONArray) obj;

                HashMap<String, Color> colMap = new HashMap<>();
                ArrayList<String> namesPl = new ArrayList<String>();
                ArrayList<String> namesEng = new ArrayList<String>();

                for(Object i : jsonObject) {
                    JSONObject jo = (JSONObject) i;
                    Color col = new Color(jo);
                    colMap.put(col.name_pl, col);
                    namesPl.add(col.name_pl);
                    namesEng.add(col.name_eng);
                }

                for (Color col : colMap.values()){
                    int[] cols = col.rgb;
                    float[] xyz = col.xyz;
                    float[] la = col.lab;
                    //JSONObject labObj = (JSONObject) parser.parse("{\"l\":\"" + String.valueOf(la[0])+"\", \"a\":\"" + String.valueOf(la[1])+"\", \"b\":\"" + String.valueOf(la[2])+"\"}");
                    //jo.put("lab", labObj);
                    System.out.println("HEX: " + col.hex + ", RGB=("+ cols[0] + "," + cols[1] + "," + cols[2] + "), XYZ=(" + xyz[0] + "," + xyz[1] + "," + xyz[2] + "), LAB=(" + la[0] + "," + la[1] + "," + la[2] + ")");
                }
                //FileWriter fileWriter = new FileWriter("C:\\Users\\mkk-1\\Downloads\\colorQuizzer-master\\colors_new.json");
                //fileWriter.write(jsonObject.toJSONString());
                //fileWriter.close();

                String search = "żół";
                ArrayList<Score> searchScore = new ArrayList<Score>();
                for (String i : namesPl) {
                    searchScore.add(new Score(i, JaroComparator.compare(search, i)));
                }
                Collections.sort(searchScore);
                System.out.println("SCORES for \'" + search + "\":");
                System.out.println("1: [" + searchScore.get(0).score + "] " + searchScore.get(0).name);
                System.out.println("2: [" + searchScore.get(1).score + "] " + searchScore.get(1).name);
                System.out.println("3: [" + searchScore.get(2).score + "] " + searchScore.get(2).name);
                System.out.println("4: [" + searchScore.get(3).score + "] " + searchScore.get(3).name);
                System.out.println("5: [" + searchScore.get(4).score + "] " + searchScore.get(4).name);

            } catch (Exception e) {
                e.printStackTrace();
            }

    }


}
