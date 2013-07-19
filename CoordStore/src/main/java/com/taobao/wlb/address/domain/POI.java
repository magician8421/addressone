package com.taobao.wlb.address.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: xiaotong.dxt
 * Date: 13-7-8
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class POI {

    public POI parent;
    //id
    public String id;
    //城市名
    public String name;
    //是否支持COD
    public boolean isSupCod;

    // 是否含有下级

    public boolean hasNext;

    public Set<Service> services=new HashSet<Service>();
}
