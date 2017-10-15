package com.marketing.system.util;

import com.marketing.system.entity.ProjectInfo;
import org.apache.http.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    //集合分页
    public static List<Map> listSplit(int page, int limit, List<Map> list){

        List<Map> result = new ArrayList<Map>();
        if(list != null && list.size() > 0){
            int allCount = list.size();
            int pageCount = (allCount + limit-1) / limit;
            if(page >= pageCount){
                page = pageCount;
            }
            int start = (page-1) * limit;
            int end = page * limit;
            if(end >= allCount){
                end = allCount;
            }
            for(int i = start; i < end; i ++){
                result.add(list.get(i));
            }
        }

        return (result != null && result.size() > 0) ? result : new ArrayList<>();

    }

    //集合分页
    public static List<ProjectInfo> listSplit2(int page, int limit, List<ProjectInfo> list){

        List<ProjectInfo> result = new ArrayList<ProjectInfo>();
        if(list != null && list.size() > 0){
            int allCount = list.size();
            int pageCount = (allCount + limit-1) / limit;
            if(page >= pageCount){
                page = pageCount;
            }
            int start = (page-1) * limit;
            int end = page * limit;
            if(end >= allCount){
                end = allCount;
            }
            for(int i = start; i < end; i ++){
                result.add(list.get(i));
            }
        }

        return (result != null && result.size() > 0) ? result : new ArrayList<>();

    }

}
