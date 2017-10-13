package com.marketing.system.util;

import org.apache.http.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class ToolUtil {
    //文件响应下载
    public static void downloadFile(File file, OutputStream out){
        try {
        FileInputStream ins = new FileInputStream(file);
        System.out.println("ins--------"+ins);
        byte[] b = new byte[1024];
        int n=0;
        while((n=ins.read(b))!=-1){
            out.write(b, 0, n);
            System.out.println("b---------"+b);
        }

        ins.close();
        out.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

}
