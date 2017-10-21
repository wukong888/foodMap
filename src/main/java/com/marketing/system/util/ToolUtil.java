package com.marketing.system.util;

import com.marketing.system.cache.Cache;
import com.marketing.system.cache.CacheManager;
import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.entity.SystemUser;
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

    /**
     * 判断缓存是否存在
     */
    public static String cacheExist(String user_id) {
        String key = user_id;
        Cache cache = CacheManager.getCacheInfo(key);
        if (null == cache || cache.getValue() == null) {
            cache = new Cache();
            cache.setKey(key);
            cache.setValue(1);
            CacheManager.putCache(key, cache);

            return "empty";
        } else {
            return "full";
        }
    }

    /**
     * 判断返回map
     */
    public static Map<String,Object> putMap(int current,String createrSquadId,String creater,String proState,
                                            String createDateStart,String createDateEnd,String planSDateStart,String planSDateEnd,
                                            String proType,String proName){

        Map<String, Object> map = new HashMap<>();

        map.put("current", current);
        map.put("pageSize", 1000);
        map.put("createrSquadId", createrSquadId);//项目发起部门
        map.put("creater", creater);//创建人
        map.put("proState", proState);//项目状态(1:立项待审批，2：开发中，3：上线带审批，4：完成，5：驳回，6：作废）
        if (createDateStart == "" || createDateStart == null) {
            map.put("createDateStart", "1980-01-01 00:00:00");//项目发起开始时间
        } else {
            map.put("createDateStart", createDateStart);//项目发起开始时间
        }

        if (createDateEnd == "" || createDateEnd == null) {
            map.put("createDateEnd", "2999-01-01 00:00:00");//项目发起结束时间
        } else {
            map.put("createDateEnd", createDateEnd);//项目发起结束时间
        }

        if (planSDateStart == "" || planSDateStart == null) {
            map.put("planSDateStart", "1980-01-01 00:00:00");//预计上线开始时间
        } else {
            map.put("planSDateStart", planSDateStart);//预计上线开始时间
        }

        if (planSDateEnd == "" || planSDateEnd == null) {
            map.put("planSDateEnd", "2999-01-01 00:00:00");//预计上线结束时间
        } else {
            map.put("planSDateEnd", planSDateEnd);//预计上线结束时间
        }

        map.put("proType", proType);//项目类型
        map.put("proName", proName);//项目名称

        return map;
    }

    /**
     * 判断返回map
     */
    public static Map<String,Object> putMaplimit(String[] Idsmember,String createrSquadId,String creater,String proState,
                                            String createDateStart,String createDateEnd,String planSDateStart,String planSDateEnd,
                                            String proType,String proName){

        Map<String, Object> mapTmem = new HashMap<>();

        mapTmem.put("menuLeafIds", Idsmember);

        mapTmem.put("createrSquadId", createrSquadId);//项目发起部门
        mapTmem.put("creater", creater);//创建人
        mapTmem.put("proState", proState);//项目状态(1:立项待审批，2：开发中，3：上线带审批，4：完成，5：驳回，6：作废）
        if (createDateStart == "" || createDateStart == null) {
            mapTmem.put("createDateStart", "1980-01-01 00:00:00");//项目发起开始时间
        } else {
            mapTmem.put("createDateStart", createDateStart);//项目发起开始时间
        }

        if (createDateEnd == "" || createDateEnd == null) {
            mapTmem.put("createDateEnd", "2999-01-01 00:00:00");//项目发起结束时间
        } else {
            mapTmem.put("createDateEnd", createDateEnd);//项目发起结束时间
        }

        if (planSDateStart == "" || planSDateStart == null) {
            mapTmem.put("planSDateStart", "1980-01-01 00:00:00");//预计上线开始时间
        } else {
            mapTmem.put("planSDateStart", planSDateStart);//预计上线开始时间
        }

        if (planSDateEnd == "" || planSDateEnd == null) {
            mapTmem.put("planSDateEnd", "2999-01-01 00:00:00");//预计上线结束时间
        } else {
            mapTmem.put("planSDateEnd", planSDateEnd);//预计上线结束时间
        }

        mapTmem.put("proType", proType);//项目类型
        mapTmem.put("proName", proName);//项目名称

        return mapTmem;
    }

    /**
     * setDuty
     * @param pro
     * @param userName
     * @param user
     * @param map1
     * @return
     */
    public static ProjectInfo setDuty(ProjectInfo pro,String userName,SystemUser user,Map map1){
        if (!StringUtil.isEmpty(user.getDuty()) && user.getDuty() != "") {
            if (map1 != null) {
                if (userName.equals(pro.getCreater()) && !user.getDuty().equals("CEO")) {
                    pro.setDuty("项目发起人");
                } else if (!userName.equals(pro.getCreater()) && userName.equals(map1.get("subtaskHandler"))) {
                    pro.setDuty("组员");
                } else if (user.getDuty().equals("CEO")) {
                    pro.setDuty("CEO");
                } else if (user.getDuty().contains("组长") || user.getDuty().contains("经理")) {
                    pro.setDuty("经理/组长");
                } else {
                    pro.setDuty("项目无关人员");
                }
            } else {
                if (userName.equals(pro.getCreater()) && !user.getDuty().equals("CEO")) {
                    pro.setDuty("项目发起人");
                } else if (user.getDuty().equals("CEO")) {
                    pro.setDuty("CEO");
                } else if (user.getDuty().contains("组长") || user.getDuty().contains("经理")) {
                    pro.setDuty("经理/组长");
                } else {
                    pro.setDuty("项目无关人员");
                }
            }
        } else {
            pro.setDuty("项目无关人员");
        }
        return pro;
    }

    public static ProjectInfo setDutyProInfo(ProjectInfo pro,String userName,SystemUser user,ProjectInfo map1){
        if (!StringUtil.isEmpty(user.getDuty()) && user.getDuty() != "") {
            if (userName.equals(pro.getCreater()) && !user.getDuty().equals("CEO")) {
                map1.setDuty("项目发起人");
                //
            } else if (!userName.equals(pro.getCreater()) && userName.equals(map1.getCreater())) {
                map1.setDuty("组员");
            } else if (user.getDuty().equals("CEO")) {
                map1.setDuty("CEO");
            } else if (user.getDuty().contains("组长") || user.getDuty().contains("经理")) {
                map1.setDuty("经理/组长");
            } else {
                map1.setDuty("项目无关人员");
            }
        }

        return map1;
    }


    public static Map<String,Object> getmapList(List<Map<String, Object>> mapList,String id){

        String mentIds = StringUtil.toString(MapUtil.collectProperty(mapList, id));
        String[] mIds = mentIds.split(",");
        Map<String, Object> mapTid = new HashMap<>();

        mapTid.put("mentIds", mIds);

        return mapTid;
    }
}
