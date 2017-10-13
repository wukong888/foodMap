package com.marketing.system.util;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
    public String getStringRegDate(String entryDate) {


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = format.parse(entryDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 得到60天后的日期
        Date date2 = new Date(date1.getTime() + 60L * 24L * 60L * 60L * 1000L);
        String date3 = format.format(date2);


        return date3;

    }


    public  Integer daysBetween(String entrydate) throws ParseException {
        Date d = new Date();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        Date date=formatter.parse(entrydate);
        long  week = (d.getTime() - date.getTime())/(24L * 60L * 60L * 1000L*7);
        return Integer.parseInt(String.valueOf(week))+1;
    }

    public String getNewRegDate(String entryDate,int datej) {


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = format.parse(entryDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 得到60天后的日期
        Date date2 = new Date(date1.getTime() + (datej) * 24L * 60L * 60L * 1000L);
        String date3 = format.format(date2);
        return date3;
    }

    public   String getSystemTime(){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");//设置日期格式
            String str=df.format(new Date());// new Date()为获取当前系统时间
            return str;
    }


    public  String getSystemSmallTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String str=df.format(new Date());// new Date()为获取当前系统时间
        return str;
    }
    //获取YYYY-MM格式的当前时间
    public static String getYMDate(){
        Date Date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(Date);
    };

    //获取YYYY-MM-DD HH:mm的当前时间
    public static String getYMDHMDate(){
        Date Date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(Date);
    }

    //获取yyyy-MM-dd格式的当前时间
    public static String getYMDDate(){
        Date Date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        return sdf.format(Date);
    }

    //获取yyyy-MM-dd hh:mm:ss格式的当前时间
    public static String getYMDHMSDate(){
        Date Date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(Date);
    }

    //获取yyyy格式的当前时间
    public static String getYDate(){
        Date Date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(Date);
    }

    //获取某月时间的月第一天和下个月的第一天
    public static Map getMonthDate(String Date){
        Map<String,Object> MonthDayDate=new HashMap<String,Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date=null;
        try {
            date = sdf.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //得到当前时间的下个月
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        Date nextMonDate=cal.getTime();
        String inDate=sdf.format(date);
        String nextDate=sdf.format(nextMonDate);
        //得到这个月的1号和下个月的1号
        MonthDayDate.put("startDate",Date+"-01");
        MonthDayDate.put("endDate",nextDate+"-01");

        return MonthDayDate;
    }
//
//    public  static void main(String[] args){
//
//       System.out.println(getSystemTime());
//    }
}
