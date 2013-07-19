package com.taobao.wlb.address;

import com.taobao.wlb.address.utils.HttpTools;
import com.taobao.wlb.address.utils.Param;
import com.taobao.wlb.address.utils.Result;
import com.taobao.wlb.address.domain.City;
import com.taobao.wlb.address.domain.County;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: xiaotong.dxt
 * Date: 13-7-9
 * Time: 上午9:50
 * To change this template use File | Settings | File Templates.
 */
public class AddressFetcherEMS {

    static Map<String, String> map = new TreeMap<String, String>();

    static {

        //初始化EMS里的省市地区

        map.put("11", "北京");
        map.put("12", "天津");
        map.put("13", "河北");
        map.put("14", "山西");
        map.put("15", "内蒙古");
        map.put("21", "辽宁省");
        map.put("22", "吉林");
        map.put("23", "黑龙江");
        map.put("31", "上海");
        map.put("32", "江苏");
        map.put("33", "浙江");
        map.put("34", "安徽");
        map.put("35", "福建");
        map.put("36", "江西");
        map.put("37", "山东");
        map.put("41", "河南");
        map.put("42", "湖北");
        map.put("43", "湖南");
        map.put("44", "广东");
        map.put("45", "广西");
        map.put("46", "海南");
        map.put("50", "重庆");
        map.put("51", "四川");
        map.put("52", "贵州");
        map.put("53", "云南");
        map.put("54", "西藏");
        map.put("61", "陕西");
        map.put("62", "甘肃");
        map.put("63", "青海");
        map.put("64", "宁夏");
        map.put("65", "新疆");
        map.put("71", "台湾");
        map.put("81", "香港");
        map.put("82", "澳门");
    }

    public static void main(String[] args) throws Exception {

        FileWriter writer = new FileWriter(new File("d:/ems_info.txt"));
        for (Map.Entry<String, String> proc : map.entrySet()) {
            writer.write("省:" + proc.getKey() + "-->" + proc.getValue() + "\n");
            ;
            List<City> cities = getCityByProv(proc.getKey());
            for (City city : cities) {
                writer.write("      市:" + city.id + "-->" + city.name + "\n");
                List<County> counties = getCountyByCity(city.id);
                for (County county : counties) {
                    writer.write("         区县 " + county.id + "-->" + county.name + "\n");

                }

            }

        }
        writer.flush();
    }

    /**
     * 根据省获取城市
     *
     * @return
     */
    public static List<City> getCityByProv(String prov) throws Exception {

        List<City> cityList = new ArrayList<City>();
        Param param = new Param();
        param.key = "c0-scriptName";
        param.value = "AddressAuto";
        Param param1 = new Param();
        param1.key = "c0-methodName";
        param1.value = "getCityByProv";
        Param param2 = new Param();
        param2.key = "c0-id";
        param2.value = "0";
        Param param3 = new Param();
        param3.key = "c0-param0";
        param3.value = "string:" + prov;
        Param param4 = new Param();
        param4.key = "batchId";
        param4.value = "11";
        Param param5 = new Param();
        param5.key = "callCount";
        param5.value = "1";
        Param param6 = new Param();
        param6.key = "page";
        param6.value = "/ec-web/";
        Param param7 = new Param();
        param6.key = "scriptSessionId";
        param6.value = "2FC965D99A152C397708F8FFCC09D299325";

        Set<Param> paramSet = new HashSet<Param>();
        paramSet.add(param);
        paramSet.add(param1);
        paramSet.add(param2);
        paramSet.add(param3);
        paramSet.add(param4);
        paramSet.add(param5);
        paramSet.add(param6);
        paramSet.add(param7);
        String url = "http://www.11183.com.cn/ec-web/dwr/call/plaincall/AddressAuto.getCityByProv.dwr";
        Result result = HttpTools.doPost(url, paramSet);
        if (result.statusCode == 200) {
            String response = result.response;
            if (response.contains("_remoteHandleCallback")) {
                String cities = response.substring(response.indexOf("{"), response.indexOf("}") + 1);
                if (StringUtils.isNotBlank(cities)) {
                    JSONObject array = JSONObject.fromObject(cities);
                    JSONArray names = array.names();
                    for (int i = 0; i < names.size(); i++) {
                        City city = new City();
                        city.id = names.getString(i);
                        city.name = array.getString(names.getString(i));
                        if (StringUtils.isBlank(city.id) || StringUtils.isBlank(city.name)) {
                            continue;
                        }

                        cityList.add(city);

                    }
                }
            }
        }

        return cityList;
    }


    private static List<County> getCountyByCity(String countyParam) throws Exception {

        List<County> countyList = new ArrayList<County>();
        Param param = new Param();
        param.key = "c0-scriptName";
        param.value = "AddressAuto";
        Param param1 = new Param();
        param1.key = "c0-methodName";
        param1.value = "getCountyByCity";
        Param param2 = new Param();
        param2.key = "c0-id";
        param2.value = "0";
        Param param3 = new Param();
        param3.key = "c0-param0";
        param3.value = "string:" + countyParam;
        Param param4 = new Param();
        param4.key = "batchId";
        param4.value = "3";
        Param param5 = new Param();
        param5.key = "callCount";
        param5.value = "1";
        Param param6 = new Param();
        param6.key = "page";
        param6.value = "/ec-web/";
        Param param7 = new Param();
        param6.key = "scriptSessionId";
        param6.value = "2FC965D99A152C397708F8FFCC09D299325";

        Set<Param> paramSet = new HashSet<Param>();
        paramSet.add(param);
        paramSet.add(param1);
        paramSet.add(param2);
        paramSet.add(param3);
        paramSet.add(param4);
        paramSet.add(param5);
        paramSet.add(param6);
        paramSet.add(param7);
        String url = "http://www.11183.com.cn/ec-web/dwr/call/plaincall/AddressAuto.getCountyByCity.dwr";
        Result result = HttpTools.doPost(url, paramSet);
        if (result.statusCode == 200) {
            String response = result.response;
            if (response.contains("_remoteHandleCallback")) {
                String cities = response.substring(response.indexOf("{"), response.indexOf("}") + 1);
                if (StringUtils.isNotBlank(cities)) {
                    JSONObject array = JSONObject.fromObject(cities);
                    JSONArray names = array.names();
                    for (int i = 0; i < names.size(); i++) {
                        County county = new County();
                        county.id = names.getString(i);
                        county.name = array.getString(names.getString(i));
                        if (StringUtils.isBlank(county.id) || StringUtils.isBlank(county.name)) {
                            continue;
                        }

                        countyList.add(county);

                    }
                }
            }
        }

        return countyList;
    }

}

