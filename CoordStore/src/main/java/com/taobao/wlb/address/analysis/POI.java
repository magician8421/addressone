package com.taobao.wlb.address.analysis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xiaotong.dxt
 * Date: 13-7-9
 * Time: 下午11:46
 * To change this template use File | Settings | File Templates.
 */
public class POI {
    public String id;
    public String name;
    public boolean isRealExist;
    public double x;
    public double y;
    public String parent;
    public double instance;
    /**
     * key是POI数据
     */
    public Map<POI,Double> nearstTown=new HashMap<POI,Double>();
}
