package com.taobao.wlb.address;

import com.taobao.wlb.address.utils.HttpTools;
import com.taobao.wlb.address.utils.Param;
import com.taobao.wlb.address.utils.Result;
import com.taobao.wlb.address.domain.City;
import com.taobao.wlb.address.domain.County;
import com.taobao.wlb.address.domain.POI;
import com.taobao.wlb.address.domain.ReachEndException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 爬取京东的物流配送区域信息
 * User: xiaotong.dxt
 * Date: 13-7-8
 * Time: 下午4:02
 * To change this template use File | Settings | File Templates.
 */
public class AddressFethcerJingDongApp {

    public static void main(String[] args) throws Exception {

        FileWriter writer = new FileWriter(new File("d://jingdong.txt"));
        List<POI> pois = getSupProvince();

        if (pois != null) {
            for (POI poi : pois) {

                writer.write(poi.name + "\n");
                try {

                    List<City> cities = getCitys(String.valueOf(poi.id));
                    if (cities != null) {

                        for (City city : cities) {
                            writer.write("       " + "城市=" + city.name + ",COD=" + city.isSupCod + "\n");
                            List<County> counties = getCountys(city.id);
                            for (County county : counties)
                                writer.write("                        " + "城镇=" + county.name + ",COD=" + county.isSupCod + "\n");
                        }
                    } else {
                        break;
                    }
                } catch (ReachEndException e) {
                    break;
                }

            }
        }

        writer.flush();
/*        FileWriter writer = new FileWriter(new File("d://jingdong.txt"));
        for (int i = 1; i < 40; i++) {
            try {

                List<City> cities = getCitys(String.valueOf(i));
                writer.write("省---" + i + "\n");
                if (cities != null) {

                    for (City city : cities) {
                        writer.write("       " + "城市=" + city.name + ",COD=" + city.isSupCod + "\n");
                        List<County> counties = getCountys(city.id);
                        for (County county : counties)
                            writer.write("                        " + "城镇=" + county.name + ",COD=" + county.isSupCod + "\n");
                    }
                } else {
                    break;
                }
            } catch (ReachEndException e) {
                break;
            }


        }


        writer.flush();
        ;*/


/*        String url = "http://passport.jd.com/uc/loginService";
        Param param1 = new Param();
        param1.key = "uuid";
        param1.value = "8d9ba208-22be-4f4e-931f-9bfe19ee96f3";
        Param param2 = new Param();
        param2.key = "loginname";
        param2.value = "83378122@163.com";
        Param param3 = new Param();
        param3.key = "loginpwd";
        param3.value = "sss";

        Set<Param> paramSet = new HashSet<Param>();
        paramSet.add(param1);
        paramSet.add(param2);
        paramSet.add(param3);
        Result result = HttpTools.doPost(url, paramSet);
        System.out.println(result.statusCode);
        System.out.println(conver(result.response));*/
    }

    public void simulateLogin(String userName, String pwd, String url) {

    }

    public static List<City> getCitys(String parentId) throws Exception {


        List<City> cities = new ArrayList<City>();
        String url = "http://buy.jd.com/purchase/flows/easybuy/FlowService.ashx";
        Param action = new Param();
        action.key = "action";
        action.value = "GetCitys";
        Param action2 = new Param();
        action2.key = "Id";
        action2.value = parentId;
        Set<Param> paramSet = new HashSet<Param>();
        paramSet.add(action);
        paramSet.add(action2);
        Result result = HttpTools.doGet(url, paramSet);
        if (result.statusCode == 200) {
            String response = result.response.replace("(", "").replace(")", "");
            JSONObject jsonObject = JSONObject.fromObject(response);
            if (jsonObject.getBoolean("Flag") == false) {
                return cities;
            }
            JSONObject obj = jsonObject.getJSONObject("Obj");
            if (obj != null) {

                JSONArray array = null;
                try {

                    array = obj.getJSONArray("Areas");
                } catch (Exception e) {

                    throw new ReachEndException();
                }
                for (int i = 0; i < array.size(); i++) {
                    JSONObject city = array.getJSONObject(i);
                    if (city != null) {
                        String id = city.getString("Id");
                        String name = city.getString("Name");
                        boolean isSupCOD = city.getBoolean("IsSupCOD");
                        if (StringUtils.isBlank(id) || StringUtils.isBlank(name))
                            continue;
                        City cityO = new City();
                        cityO.id = id;
                        cityO.name = name;
                        cityO.isSupCod = isSupCOD;
                        cities.add(cityO);

                    }
                }
            }
        }

        return cities;
    }

    public static List<POI> getSupProvince() throws Exception {
        String url = "http://buy.jd.com/purchase/flows/easybuy/FlowService.ashx";


        List<POI> pois = new ArrayList<POI>();

        Param action = new Param();
        action.key = "action";
        action.value = "GetAllAreas";
        Param action2 = new Param();
        action2.key = "IdProvince";
        action2.value = "-22";
        Param action3 = new Param();
        action3.key = "IdCity";
        action3.value = "-22";
        Param action4 = new Param();
        action4.key = "IdCounty";
        action4.value = "-22";

        Set<Param> paramSet = new HashSet<Param>();
        paramSet.add(action);
        paramSet.add(action2);
        paramSet.add(action3);
        paramSet.add(action4);

        Result result = HttpTools.doGet(url, paramSet);
        if (result.statusCode == 200) {
            String response = result.response.replace("(", "").replace(")", "");
            JSONObject jsonObject = JSONObject.fromObject(response);
            if (jsonObject.getBoolean("Flag") == false) {
                return pois;
            }
            JSONObject obj = jsonObject.getJSONObject("Obj");
            if (obj != null) {

                JSONArray array = null;
                try {

                    array = obj.getJSONArray("SupProvinces");
                } catch (Exception e) {

                    throw new ReachEndException();
                }
                for (int i = 0; i < array.size(); i++) {
                    JSONObject city = array.getJSONObject(i);
                    if (city != null) {
                        String id = city.getString("Id");
                        String name = city.getString("Name");
                        boolean isSupCOD = city.getBoolean("IsSupCOD");
                        if (StringUtils.isBlank(id) || StringUtils.isBlank(name))
                            continue;
                        POI province = new POI();
                        province.id = id;
                        province.name = name;
                        province.isSupCod = isSupCOD;
                        pois.add(province);

                    }
                }
            }
        }


        return pois;
    }

    public static List<County> getCountys(String parentId) throws Exception {
        {

            List<County> cities = new ArrayList<County>();
            String url = "http://buy.jd.com/purchase/flows/easybuy/FlowService.ashx";
            Param action = new Param();
            action.key = "action";
            action.value = "GetCountys";
            Param action2 = new Param();
            action2.key = "Id";
            action2.value = parentId;
            Set<Param> paramSet = new HashSet<Param>();
            paramSet.add(action);
            paramSet.add(action2);
            Result result = HttpTools.doGet(url, paramSet);
            if (result.statusCode == 200) {
                String response = result.response.replace("(", "").replace(")", "");
                JSONObject jsonObject = JSONObject.fromObject(response);
                JSONObject obj = jsonObject.getJSONObject("Obj");
                if (obj != null) {
                    JSONArray array = obj.getJSONArray("Areas");
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject city = array.getJSONObject(i);
                        if (city != null) {
                            String id = city.getString("Id");
                            String name = city.getString("Name");
                            boolean isSupCOD = city.getBoolean("IsSupCOD");
                            if (StringUtils.isBlank(id) || StringUtils.isBlank(name))
                                continue;
                            County country = new County();
                            country.id = id;
                            country.name = name;
                            country.isSupCod = isSupCOD;
                            cities.add(country);

                        }
                    }
                }
            }

            return cities;
        }
    }


}
