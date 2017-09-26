package com.marketing.system.service.impl;


import com.marketing.system.entity.ProjectInfo;
import com.marketing.system.mapper_two.OnlineProMapper;
import com.marketing.system.service.OnlineProService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OnilneProServiceImpl implements OnlineProService{

    @Resource
    private OnlineProMapper OnProDao;

    //模糊查询所有待审批的项目
    public Map<String, Object> selectOnPro(Integer current,Integer pageSize,String createsquadid,String creater,String createdate1,String createdate2,String plansdate1,String plansdate2,String protype,String param){
        if(createsquadid==null){
           createsquadid="";
        }
        if(creater==null){
            creater="";
        }
        if(createdate1==null){
            createdate1="2010-01-01";
        }
        if(createdate2==null){
            createdate1="2040-01-01";
        }
        if(plansdate1==null){
            createdate1="2010-01-01";
        }
        if(createdate2==null){
            createdate1="2040-01-01";
        }
        if(protype==null){
            protype="";
        }
        if(param==null){
            param="";
        }

        Map<String,Object> onProMap=new HashMap<String,Object>();
        List<ProjectInfo> OnPro=OnProDao.selectOnPro(createsquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param,current,pageSize);
        Integer OnProNum=OnProDao.selectOnProNum(createsquadid,creater,createdate1,createdate2,plansdate1,plansdate2,protype,param);

        onProMap.put("OnPro",OnPro);
        onProMap.put("OnProNum",OnProNum);


        return onProMap;
    }
}
