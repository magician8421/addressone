package com.taobao.wlb.address.utils;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Http的工具类
 * User: xiaotong.dxt
 * Date: 13-7-8
 * Time: 下午4:18
 * To change this template use File | Settings | File Templates.
 */
public class HttpTools {


    public static Result doPost(String url, Set<Param> params) throws HttpException, IOException {
        Result result = new Result();
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);

        client.getHttpConnectionManager().getParams().setSoTimeout(60000);
        //设置访问编码
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF8");
        //设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        //让服务器知道访问源为浏览器
        client.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; rv:8.0.1) Gecko/20100101 Firefox/8.0.1");
        PostMethod postMethod = new PostMethod(url);
        //通过head对象来设置请求头参数
        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("Cookie", "F8CCAF4DF66BF2594AA8391170A93A7F8F64D94C4FB1AE489AE2D8E13E1D026A6683BBA162B9D16313EE36970D3F60639E68D06211A7494D57121F402B0E99E54EDB044534A64F8FFCD88890D70E9792ABD70BA43BBC7939C5914C0EBE0C93DBF3312D07BF557B16B6F3A8AD5A82E290CBA04C6C0AD85AD20A18D4D7EF49D65792BC5F6120B2DBACC05A29AC4163FB6C4DF55DBCABFA5A4F936BEFE8020C8E88"));
        headers.add(new Header("Connection", "keep-alive"));
        client.getHostConfiguration().getParams().setParameter(
                "http.default-headers", headers);
        setHeaders(postMethod);
        NameValuePair[] nameValuePairs = null;
        if (params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            for (Param param : params) {
                if (param != null && param.key != null && param.value != null) {
                    NameValuePair nameValuePair = new NameValuePair();
                    nameValuePair.setName(param.key);
                    nameValuePair.setValue(param.value);
                    nameValuePairList.add(nameValuePair);
                }
            }
            nameValuePairs = new NameValuePair[nameValuePairList.size()];
            nameValuePairList.toArray(nameValuePairs);
        }
        postMethod.setRequestBody(nameValuePairs);

        int statusCode = client.executeMethod(postMethod);
        if (statusCode != HttpStatus.SC_OK) {
            result.statusCode = statusCode;
        } else {
            byte[] responseBytes = postMethod.getResponseBody();
            if (responseBytes != null) {
                result.statusCode = statusCode;
                result.response = new String(responseBytes);
            }

        }

        return result;
    }

    public static Result doGet(String url, Set<Param> params) throws HttpException, IOException {
        Result result = new Result();
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);

        client.getHttpConnectionManager().getParams().setSoTimeout(60000);
        GetMethod getMethod = new GetMethod(url);
        getMethod.setRequestHeader("Host", "buy.jd.com");
        HttpState initialState = new HttpState();
        Cookie c = new Cookie();
        c.setDomain(".jd.com");
        c.setPath("/");
        c.setName("ceshi3.com");
        c.setValue("70357C6AFCC3D50B1DBFEAE5D7AAE3EEF56C0517B99C8DE34AC71663BD313A9AEB9C373A58E9382990180B2356D2EC93E62C0970F4642DAE59309A18DF6FFE60D68B57FBE2DDD0AB95806A192891323204E625F445FA41D4465D4B177CC11E2665CBB3B29606124B9B7D6FDBD0172775FF2BD49D864778347A214EC4A16D8746BC5F8EC67699E7A3A44ABD918B4676F62EFD79C625FDF803E354B903BDDE48E6");
        initialState.addCookie(c);
        client.setState(initialState);
        NameValuePair[] nameValuePairs = null;
        if (params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            for (Param param : params) {
                if (param != null && param.key != null && param.value != null) {
                    NameValuePair nameValuePair = new NameValuePair();
                    nameValuePair.setName(param.key);
                    nameValuePair.setValue(param.value);
                    nameValuePairList.add(nameValuePair);
                }
            }
            nameValuePairs = new NameValuePair[nameValuePairList.size()];
            nameValuePairList.toArray(nameValuePairs);
        }

        if (nameValuePairs != null) {
            getMethod.setQueryString(nameValuePairs);
        }

        int statusCode = client.executeMethod(getMethod);
        if (statusCode != HttpStatus.SC_OK) {
            result.statusCode = statusCode;
        } else {
            byte[] responseBytes = getMethod.getResponseBody();
            if (responseBytes != null) {
                result.statusCode = statusCode;
                result.response = new String(responseBytes);
            }

        }

        return result;

    }

    public static void setHeaders(HttpMethod method) {
        method.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
        method.setRequestHeader("Accept-Language", "zh-cn");
        method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
        method.setRequestHeader("Accept-Charset", "gbk");
        method.setRequestHeader("Keep-Alive", "300");
        method.setRequestHeader("Connection", "Keep-Alive");
        method.setRequestHeader("Cache-Control", "no-cache");
    }
}
