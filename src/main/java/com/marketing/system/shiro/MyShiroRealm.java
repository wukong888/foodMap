package com.marketing.system.shiro;


import com.marketing.system.entity.Permission;
import com.marketing.system.entity.SystemUser;
import com.marketing.system.service.PermissionService;
import com.marketing.system.service.SystemUserService;
import com.marketing.system.service.UserInfoService;
import com.marketing.system.util.WorkOderUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


/**
 * shiro身份校验核心类
 */

public class MyShiroRealm extends AuthorizingRealm {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SystemUserService systemUserService;

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     *
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

        String name = token.getUsername();
        String password = String.valueOf(token.getPassword());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("UserName", name);
        //密码进行加密处理  明文为  password

        String paw = password;
        String pawDES = WorkOderUtil.getSHA256StrJava(paw);
        map.put("Password", pawDES);
        SystemUser user = null;
        Map<String, Object> objectMap = new HashMap<String, Object>();
        // 从数据库获取对应用户名密码的用户

        List<SystemUser> userList = systemUserService.selectByMap(map);
        if (userList.size() != 0) {
            user =  userList.get(0);
        }
        if (null == user) {
            throw new AccountException("帐号或密码不正确！");
        } /*else if ("1".equals(objectMap.get("status"))) {
            *//**
             * 如果用户的status为禁用。那么就抛出<code>DisabledAccountException</code>
             *//*
            throw new DisabledAccountException("此帐号已经设置为禁止登录！");
        } */else {
            //登录成功

            //更新登录时间 last login time
            //user.setLoginTime(new Date());
            //userInfo.updateById(user);
            //清空登录计数
        }
        SystemUser userIn = systemUserService.selectByPrimaryKey(user.getId());
        logger.info("身份认证成功，登录用户：" + name);
        return new SimpleAuthenticationInfo(userIn, password, getName());
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        logger.info("权限认证方法：MyShiroRealm.doGetAuthorizationInfo()");
        SystemUser user = (SystemUser) SecurityUtils.getSubject().getPrincipal();
        int userId = user.getId();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //根据用户ID查询角色（role），放入到Authorization里。
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SystemId", userId);
		/*List<SysRole> roleList = sysRoleService.selectByMap(map);
		Set<String> roleSet = new HashSet<String>();
		for(SysRole role : roleList){
			roleSet.add(role.getType());
		}*/

        Set<String> roleSet = new HashSet<String>();
        roleSet.add(user.getUserName());
        info.setRoles(roleSet);

        //根据用户ID查询权限（permission），放入到Authorization里。
		List<Permission> permissionList = permissionService.selectByMap(map);
		Set<String> permissionSet = new HashSet<String>();
		for(Permission permission : permissionList){
			permissionSet.add(permission.getName());
		}

        /*Set<String> permissionSet = new HashSet<String>();
        permissionSet.add("权限添加:权限删除");
        permissionSet.add("uadd22");*/
        info.setStringPermissions(permissionSet);
        return info;
    }
}