package com.taobao.wlb.address;

import com.taobao.wlb.address.utils.HttpTools;
import com.taobao.wlb.address.utils.Result;
import com.taobao.wlb.address.domain.POI;
import com.taobao.wlb.address.domain.Service;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 京东的服务地址和服务区域查询表
 * User: xiaotong.dxt
 * Date: 13-7-9
 * Time: 上午11:00
 * To change this template use File | Settings | File Templates.
 */
public class JDServiceAreaFetcher {


    private static String pattern = "";

    private static Set<String> JDService = new TreeSet<String>() {
        {
            this.add("211限时达");
            this.add("大家电211");
            this.add("货到付款");
            this.add("定时达");
            this.add("开箱验机");
            this.add("自提");
            this.add("夜间配");
            this.add("极速达");
        }
    };

    public static void main(String[] args) throws Exception {

        ExecutorService pool = new ThreadPoolExecutor(10,
                10,
                30000,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10000));
        List<POI> procs = AddressFethcerJingDongApp.getSupProvince();
        for (POI proc : procs) {


            pool.execute(new FetcherRunnable(proc));

        }


    }


    public static String toServiceString(POI area) {
        if (!area.hasNext) {
            Set<Service> services = area.services;
            String serviceStr = "      (";
            int i = 0;
            for (Service service : services) {

                serviceStr = serviceStr + (i > 0 ? "," : "") + service.serviceName;
                i++;
            }
            serviceStr = "                             " + area.name + serviceStr + ")";
            return serviceStr;
        }
        return "";
    }

    public static void loadServiceArea() {

    }

    public static List<POI> initCity(POI parent) throws Exception {

        String url = "http://help.jd.com/help/front/initCity.action?provinceId=" + parent.id;
        return parse(url, parent);
    }


    public static List<POI> initArea(POI parent) throws Exception {
        String url = "http://help.jd.com/help/front/initArea.action?provinceId=" + parent.parent.id + "&cityId=" + parent.id;
        return parse(url, parent);

    }

    public static List<POI> initFourth(POI parent) throws Exception {
        String url = "http://help.jd.com/help/front/initFouth.action?provinceId=" + parent.parent.parent.id + "&cityId=" + parent.parent.id + "&areaId=" + parent.id;
        return parse(url, parent);
    }

    public static List<POI> initService(POI parent) throws Exception {
        String url = "http://help.jd.com/help/front/initService.action?provinceId=" + parent.parent.parent.parent.id

                + "&cityId=" + parent.parent.parent.id
                + "&areaId=" + parent.parent.id + "&fouthId=" + parent.id;
        return parse(url, parent);
    }

    public static List<POI> parseService() {
        return null;

    }

    public static List<POI> parse(String url, POI parent) throws Exception {


        ;
        Result result = HttpTools.doGet(url, null);

        if (result.statusCode == 200) {
            String response = result.response;
            if (StringUtils.isNotBlank(response)) {
                JSONObject secondJson = JSONObject.fromObject(response).getJSONObject("result").getJSONObject("map");
                if (secondJson.getBoolean("hasNext")) {
                    parent.hasNext = true;
                    return parsePOI(secondJson, parent);
                } else {
                    Set<Service> services = parseService(secondJson);
                    parent.services = services;
                    return null;
                }

            }
        }
        return null;
    }


    public static List<POI> parsePOI(JSONObject response, POI parent) {

        List<POI> pois = new ArrayList<POI>();
        JSONArray jsonArray = response.getJSONArray("resultList");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject child = jsonArray.getJSONObject(i);
                boolean cod = child.getBoolean("cod");
                String id = child.getString("id");
                String name = child.getString("name");
                if (StringUtils.isNotBlank("id") && StringUtils.isNotBlank("name")) {
                    POI poi = new POI();
                    poi.id = id;
                    poi.name = name;
                    poi.isSupCod = cod;
                    poi.parent = parent;

                    pois.add(poi);
                } else {
                    continue;
                }
            }
        }
        return pois;
    }


    public static Set<Service> parseService(JSONObject response) {
        Set<Service> services = new HashSet<Service>();
        String jsonArray = response.getString("resultList");
        if (jsonArray != null) {
            for (String ser : JDService) {
                if (jsonArray.contains(ser)) {
                    Service service = new Service();
                    service.serviceName = ser;
                    services.add(service);
                }
            }
        }
        return services;
    }

    public static class FetcherRunnable implements Runnable {

        POI proc;

        public FetcherRunnable(POI poi) {
            this.proc = poi;
        }

        public void run() {
            {

                System.out.println("启动了");
                ;

                try {
                    FileWriter writer = new FileWriter(new File("d:/JD/jingdong_service_" + proc.name + ".txt"));

                    writer.write("省--------------------" + proc.name + "\n");
                    List<POI> cities = initCity(proc);

                    if (cities == null && !proc.hasNext) {
                        writer.write(toServiceString(proc) + "\n");
                        ;
                    } else {
                        for (POI poi : cities) {
                            writer.write("         市------------------" + poi.name + "\n");
                            List<POI> areas = initArea(poi);
                            if (areas == null && !poi.hasNext) {
                                writer.write(toServiceString(poi) + "\n");
                            } else {
                                for (POI areai : areas) {
                                    writer.write("                 区------------------" + areai.name + "\n");
                                    List<POI> fourths = initFourth(areai);
                                    if (fourths == null && !areai.hasNext) {
                                        writer.write(toServiceString(areai) + "\n");
                                        ;
                                    } else {
                                        for (POI four : fourths) {
                                            writer.write("                  四级------------------" + four.name + "\n");
                                            initService(four);
                                            writer.write(toServiceString(four) + "\n");
                                        }
                                    }

                                }

                            }
                        }
                    }

                    writer.flush();
                } catch (Exception e) {

                }
            }
        }
    }

}
