package com.taobao.wlb.address.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: xiaotong.dxt
 * Date: 13-7-8
 * Time: ����4:12
 * To change this template use File | Settings | File Templates.
 */
public class POI {

    public POI parent;
    //id
    public String id;
    //������
    public String name;
    //�Ƿ�֧��COD
    public boolean isSupCod;

    // �Ƿ����¼�

    public boolean hasNext;

    public Set<Service> services=new HashSet<Service>();
}
