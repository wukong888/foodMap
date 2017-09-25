package com.marketing.system.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.util.DigestUtils;

/**
 * 密码加密和生成uuid
 * @author liu
 *
 */
public class MD5Util {
    private static final String SLAT = "wfwerjsa%#%dsfdf&*^sdfds4329rugr8fh&*ᑶ$##$";
    //加盐后的MD5
    public static String getMD5(String password){
        String base = password /*+ "/" + SLAT*/;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        //		System.out.println("u:"+md5);
        return md5;

    }
    //UUID生成ID
    public static  String createId(){
        String id= UUID.randomUUID().toString();
        return id;
    }
    public static void main(String[] args) {
        System.out.println(getMD5("12345678"));
        //	System.err.println(createId());
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
