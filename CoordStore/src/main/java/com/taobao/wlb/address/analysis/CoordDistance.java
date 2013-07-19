package com.taobao.wlb.address.analysis;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaotong.dxt
 * Date: 13-7-8
 * Time: ����11:28
 * To change this template use File | Settings | File Templates.
 */
public class CoordDistance {

    private final static double PI = 3.14159265358979323; // Բ����
    private final static double R = 6371.229; // ����İ뾶(����)


    /**
     * ����������γ��֮��ľ���
     *
     * @param longt1 ����1
     * @param lat1   γ��1
     * @param longt2 ����2
     * @param lat2   γ��2
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
     * ��������ľ���
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
     * ���������ƽ��ֵ
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
     * ����һ�㵽�ֵ��Ĵ�ֱ����
     *
     * @param coord1 �ֵ��ϵ�����1
     * @param coord2 �ֵ��ϵ�����2
     * @param coord  ��һ����
     * @return ���루���
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
            double p = (a + b + c) / 2;// ���ܳ�
            double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// ���׹�ʽ�����
            distince = 2 * s / c;// ���ص㵽�ߵľ��루���������������ʽ��ߣ�
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
