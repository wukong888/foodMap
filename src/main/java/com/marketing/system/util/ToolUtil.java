package com.marketing.system.util;

import com.marketing.system.entity.ProjectInfo;
import org.apache.http.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ToolUtil {
    //文件响应下载
    public static void downloadFile(File file, OutputStream out) {
        try {
            FileInputStream ins = new FileInputStream(file);
            System.out.println("ins--------" + ins);
            byte[] b = new byte[1024];
            int n = 0;
            while ((n = ins.read(b)) != -1) {
                out.write(b, 0, n);
                System.out.println("b---------" + b);
            }

            ins.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //集合分页
    public static List<Map> listSplit(int page, int limit, List<Map> list) {

        List<Map> result = new ArrayList<Map>();
        if (list != null && list.size() > 0) {
            int allCount = list.size();
            int pageCount = (allCount + limit - 1) / limit;
            if (page >= pageCount) {
                page = pageCount;
            }
            int start = (page - 1) * limit;
            int end = page * limit;
            if (end >= allCount) {
                end = allCount;
            }
            for (int i = start; i < end; i++) {
                result.add(list.get(i));
            }
        }

        return (result != null && result.size() > 0) ? result : new ArrayList<>();

    }

    //集合分页
    public static List<ProjectInfo> listSplit2(int page, int limit, List<ProjectInfo> list) {

        List<ProjectInfo> result = new ArrayList<ProjectInfo>();
        if (list != null && list.size() > 0) {
            int allCount = list.size();
            int pageCount = (allCount + limit - 1) / limit;
            if (page >= pageCount) {
                page = pageCount;
            }
            int start = (page - 1) * limit;
            int end = page * limit;
            if (end >= allCount) {
                end = allCount;
            }
            for (int i = start; i < end; i++) {
                result.add(list.get(i));
            }
        }

        return (result != null && result.size() > 0) ? result : new ArrayList<>();

    }

    /**
     * 计算距离现在时间
     * @return
     */
    public static Long getBetweenTimes(String time) {
        long betweenDays = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date plansdate = sdf.parse(time);//预计完成时间

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(plansdate);
            //当前时间
            Date smdate = new Date();

            smdate = sdf.parse(sdf.format(smdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(calendar.getTime());
            long time2 = cal.getTimeInMillis();
            betweenDays = ((time2 - time1) / (1000 * 3600 * 24));//天数

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return betweenDays;
    }
}
