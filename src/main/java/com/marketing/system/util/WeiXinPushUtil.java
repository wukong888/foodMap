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
  
  public static void main(String[] args){

  }

  
  public static String httpPostWithJSON(String json) throws Exception {
    Properties prop = new Properties();
	    try{
	      InputStream in = WeiXinPushUtil.class.getClassLoader().getResourceAsStream("conf/weixinpush.properties");
	      
	      prop.load(in);
	      in.close();
	    }
	    catch (Exception e) {
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
