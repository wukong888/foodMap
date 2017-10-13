package com.marketing.system.util;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class WeiXinPushUtil {
    private static Logger logger = Logger.getLogger(WeiXinPushUtil.class);
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    public static void main(String[] args) {
        String postUrl = "{\"Uid\":" + 1299 + ",\"Content\":\"创建人:" + "周圣李"
                + "\\n\\n项目管理系统:" + "测试" + "\\n\\n内容:" + "内容" + "\\n\\n期望处理时间:" + ""
                + "\",\"AgentId\":1000011,\"Title\":\"创建\",\"Url\":\"http://192.168.3.26:5826/index?username=王东&password=5994471ABB01112AFCC18159F6CC74B4F511B99806DA59B3CAF5A9C173CACFC5\"}";
        try {
            httpPostWithJSON(postUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String httpPostWithJSON(String json) throws Exception {
        Properties prop = new Properties();
        try {
            InputStream in = WeiXinPushUtil.class.getClassLoader().getResourceAsStream("conf/weixinpush.properties");

            prop.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = null;


        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(prop.getProperty("weixinInteface"));
        httpPost.addHeader("Content-Type", "application/json");

        StringEntity se = new StringEntity(json, Charset.forName("UTF-8"));
        se.setContentType("text/json");
        se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
        httpPost.setEntity(se);
        CloseableHttpResponse responseBody = httpClient.execute(httpPost);
        result = EntityUtils.toString(responseBody.getEntity(), "UTF-8");


        return result;
    }
}
