package com.taobao.wlb.address;

import com.taobao.wlb.address.utils.HttpTools;
import com.taobao.wlb.address.utils.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: xiaotong.dxt
 * Date: 13-7-10
 * Time: ÏÂÎç7:25
 * To change this template use File | Settings | File Templates.
 */
public class EmsAddressFetcher {

    public static void main(String[] args) throws Exception {

        fetchPostCodeArea("010010", 1);


    }

    public static boolean fetchPostCodeArea(String theCode, int page) throws Exception {
        String url = "http://opendata.baidu.com/post/s?p=mini&wd=" + theCode + "&pn=" + (page * 60) + "&rn=60";
        Result result = HttpTools.doGet(url, null);

        Pattern articleP = Pattern.compile("(?i)(?<=<article class=\"region-data\">)[\\s|\\S]*?(?=</article>)");
        Pattern codeP = Pattern.compile("(?i)(?<=<em>)[\\s|\\S]*?(?=</em>)");
        Pattern regionP = Pattern.compile("(?i)(?<=</em>£º)[\\s|\\S]*?(?=</h3>)");
        Pattern roadP = Pattern.compile("(?i)(?<=<!-- baidu-tc begin --><li>)[\\s|\\S]*?(?=</li><!-- baidu-tc end -->)");
        Matcher articleMatcher = articleP.matcher(result.response);
        while (articleMatcher.find()) {
            String content = articleMatcher.group();
            Matcher codePMatcher = codeP.matcher(content);
            if (codePMatcher.find()) {
                String code = codePMatcher.group();
                System.out.println(code);
            }
            Matcher regionMatcher = regionP.matcher(content);
            if (regionMatcher.find()) {
                System.out.println(regionMatcher.group());
                ;
            }

            Matcher roadMatcher = roadP.matcher(content);
            while (roadMatcher.find()) {
                System.out.println(roadMatcher.group());
            }


        }
        return true;

    }
}
