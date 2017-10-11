package com.marketing.system.util;

import org.apache.http.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class ToolUtil {
    //文件响应下载
    public static void downloadFile(String subtaskFile, OutputStream out){
        try {
        FileInputStream ins = new FileInputStream(subtaskFile);
        byte[] b = new byte[1024];
        int n=0;
        while((n=ins.read(b))!=-1){
            out.write(b, 0, n);
        }

        ins.close();
        out.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

}
