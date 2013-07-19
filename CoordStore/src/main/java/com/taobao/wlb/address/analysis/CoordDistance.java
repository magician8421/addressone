package com.taobao.wlb.address.analysis;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaotong.dxt
 * Date: 13-7-8
 * Time: 上午11:28
 * To change this template use File | Settings | File Templates.
 */
public class CoordDistance {

    private final static double PI = 3.14159265358979323; // 圆周率
    private final static double R = 6371.229; // 地球的半径(公里)


    /**
     * 计算两个经纬度之间的距离
     *
     * @param longt1 经度1
     * @param lat1   纬度1
     * @param longt2 经度2
     * @param lat2   纬度2
     * @return
     */
        public static double getDistance(double longt1, double lat1, double longt2,
                                     double lat2) {
        double x, y, distance;
        x = (longt2 - longt1) * PI * R
                * Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
        y = (lat2 - lat1) * PI * R / 180;
        distance = Math.hypot(x, y);
        return distance;
    }

    /**
     * 计算两点的距离
     *
     * @param coord1 (longt1,lat1)
     * @param coord2 (longt2,lat2)
     * @return
     */
    public static double _getDistance(String coord1, String coord2) {
        String[] array_coord1 = coord1.split(",");
        String[] array_coord2 = coord2.split(",");
        double longt1 = Double.parseDouble(array_coord1[0]);
        double lat1 = Double.parseDouble(array_coord1[1]);
        double longt2 = Double.parseDouble(array_coord2[0]);
        double lat2 = Double.parseDouble(array_coord2[1]);
        return getDistance(longt1, lat1, longt2, lat2);

    }

    /**
     * 坐标链表的平均值
     *
     * @param coordList
     * @return
     */
    public static String getMeanCoord(List<String> coordList) {
        String meanCoord = null;
        double sum_longt = 0.0;
        double sum_lat = 0.0;
        for (String coord : coordList) {
            String[] array = coord.split(",");
            sum_longt += Double.parseDouble(array[0]);
            sum_lat += Double.parseDouble(array[1]);
        }
        double mean_longt = sum_longt / coordList.size();
        double mean_lat = sum_lat / coordList.size();
        meanCoord = new DecimalFormat("#0.000000").format(mean_longt) + ","
                + new DecimalFormat("#0.000000").format(mean_lat);
        return meanCoord;
    }

    /**
     * 计算一点到街道的垂直距离
     *
     * @param coord1 街道上的坐标1
     * @param coord2 街道上的坐标2
     * @param coord  另一坐标
     * @return 距离（公里）
     */
    public static double getPoint2StreDis(String coord1, String coord2,
                                          String coord) {
        double distince = 0.0;
        String[] array1 = coord1.split(",");
        String[] array2 = coord2.split(",");
        String[] array = coord.split(",");
        double longt1 = Double.parseDouble(array1[0]);
        double lat1 = Double.parseDouble(array1[1]);
        double longt2 = Double.parseDouble(array2[0]);
        double lat2 = Double.parseDouble(array2[1]);
        double longt = Double.parseDouble(array[0]);
        double lat = Double.parseDouble(array[1]);

        double a = CoordDistance.getDistance(longt1, lat1, longt2, lat2);
        double b = CoordDistance.getDistance(longt1, lat1, longt, lat);
        double c = CoordDistance.getDistance(longt2, lat2, longt, lat);
        if (a == b + c) {
            distince = 0.0;
        } else {
            double p = (a + b + c) / 2;// 半周长
            double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
            distince = 2 * s / c;// 返回点到线的距离（利用三角形面积公式求高）
        }

        return distince;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String s1 = "120.12011,30.28180";
        String s2 = "120.14064,30.28318";
        String s3 = "120.12502,30.27262";
        System.out.println(getPoint2StreDis(s1, s2, s3));

    }
}
