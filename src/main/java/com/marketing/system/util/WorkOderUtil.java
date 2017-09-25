package com.marketing.system.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.DigestUtils;


public class WorkOderUtil {
	 private static final String SLAT = "wfwerjsa%#%dsfdf&*^sdfds4329rugr8fh&*ᑶ$##$";
	/*
	 * MD5加密：摘要算法
	 * 特点:任意长度的字节处理成等长的结果；不可逆
	 * Base64:a-z,A-Z,0-9,+,=
	 */
	  //没加盐的MD5
      public static String md5(String src){
    	try {
			MessageDigest md=MessageDigest.getInstance("MD5");
			byte[] output=md.digest(src.getBytes());
			String ret=Base64.encodeBase64String(output);
			return ret;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}  
		return src;
    	  
      }
      //加盐后的MD5
      public static String getMD5(String password){
    		String base = password + "/" + SLAT;
    		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
    		return md5;	

      }
      //UUID生成ID
      public static  String createId(){
    	 String id= UUID.randomUUID().toString();
    	 return id;
      
     
         
      }
      public static void main(String[] args) {
//       System.out.println(getSHA256StrJava("12345"));
//        System.out.println(1);
    	  }
      /**
       *  利用java原生的摘要实现SHA256加密
       * @param str 加密后的报文
       * @return
       */
      public static String getSHA256StrJava(String str){
          MessageDigest messageDigest;
          String encodeStr = "";
          try {
              messageDigest = MessageDigest.getInstance("SHA-256");
              messageDigest.update(str.getBytes("UTF-8"));
              encodeStr = byte2Hex(messageDigest.digest());
          } catch (NoSuchAlgorithmException e) {
              e.printStackTrace();
          } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
          }
          return encodeStr;
      }

      /**
       * 将byte转为16进制
       * @param bytes
       * @return
       */
      private static String byte2Hex(byte[] bytes){
          StringBuffer stringBuffer = new StringBuffer();
          String temp = null;
          for (int i=0;i<bytes.length;i++){
              temp = Integer.toHexString(bytes[i] & 0xFF);
              if (temp.length()==1){
                  //1得到一位的进行补0操作
                  stringBuffer.append("0");
              }
              stringBuffer.append(temp);
          }
          return stringBuffer.toString();
      }
      
}
