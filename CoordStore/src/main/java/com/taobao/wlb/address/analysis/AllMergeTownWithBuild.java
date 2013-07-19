package com.taobao.wlb.address.analysis;

import org.apache.commons.collections.list.TreeList;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: xiaotong.dxt
 * Date: 13-7-9
 * Time: 下午11:49
 * To change this template use File | Settings | File Templates.
 */
public class AllMergeTownWithBuild {

    public static void main(String[] args) throws Exception {

        File dic = new File("D:\\物流宝\\地址数据\\物流\\物流\\乡镇");
        //   FileWriter writer = new FileWriter("D:\\物流宝\\temp\\result.txt", "uft-8");

        OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream("D:\\物流宝\\temp\\result_building.txt"), "UTF-8");
        File[] files = dic.listFiles();
        for (File file : files) {
            if (file.isDirectory())
                continue;
            ;
            merge(file.getName().trim(), writer);
        }
        ;
    }

    private static void merge(String fileName, OutputStreamWriter writer) throws Exception {

        Map<String, TreeList> sort = new HashMap<String, TreeList>();
        BufferedReader townReader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\物流宝\\地址数据\\物流\\物流\\乡镇\\" + fileName), "UTF-8"));
        BufferedReader buildReader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\物流宝\\地址数据\\物流\\物流\\社区\\" + fileName), "UTF-8"));

        String cityName = fileName.replace(".txt", "");
        //先初始化化乡镇的集合
        Map<String, Set<POI>> towns = new HashMap<String, Set<POI>>();
        while (townReader.ready()) {
            String lineA = townReader.readLine();


            String[] t = lineA.split("\t");

            POI poi = new POI();
            poi.id = t[0].trim();
            poi.name = t[1].trim();

            try {
                poi.x = Double.parseDouble(t[2].trim().split(",")[0]);
                poi.y = Double.parseDouble(t[2].trim().split(",")[1]);
            } catch (Exception e) {
                System.out.println(lineA);
                continue;

            }

            poi.parent = t[3].trim();
            if (towns.containsKey(poi.parent)) {
                towns.get(poi.parent).add(poi);
            } else {
                Set<POI> pois = new HashSet<POI>();
                pois.add(poi);
                towns.put(poi.parent, pois);
            }


        }

        System.out.println("初始化乡镇结束");
        //先初始化化乡镇的集合
        Map<String, Set<POI>> build = new HashMap<String, Set<POI>>();
        while (buildReader.ready()) {
            String line = buildReader.readLine();
            String[] t = line.split("\t");
            POI poi = new POI();
            poi.id = t[0].trim();
            poi.name = t[1].trim();
            try {
                poi.x = Double.parseDouble(t[2].trim().split(",")[0]);
                poi.y = Double.parseDouble(t[2].trim().split(",")[1]);
            } catch (Exception e) {
                System.out.println(line);
                continue;
            }
            poi.parent = t[3].trim();
            if (build.containsKey(poi.parent)) {
                build.get(poi.parent).add(poi);
            } else {
                Set<POI> pois = new HashSet<POI>();
                pois.add(poi);
                build.put(poi.parent, pois);
            }
        }

        System.out.println("初始化社区结束");
        ;
        for (Map.Entry<String, Set<POI>> entry : build.entrySet()) {

            for (POI building : entry.getValue()) {
                Set<POI> theTowns = towns.get(entry.getKey());
                if (theTowns == null)
                    continue;
                double min = Double.MAX_VALUE;
                POI nearest = null;
                for (POI townInfo : theTowns) {
                    double distances = CoordDistance.getDistance(building.x, building.y, townInfo.x, townInfo.y);
                    if (distances < min) {
                        min = distances;
                        nearest = townInfo;
                    }
                    //    System.out.println(building.name + "--->" + townInfo.name + "---->" + distances);
                }
                writer.write(building.name + "\t" + nearest.name + "\t" + entry.getKey() + "\t" + cityName + "\n");
            }

        }

        writer.flush();
        ;


    }
}
