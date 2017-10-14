package com.marketing.system.controller;

import com.marketing.system.accessCode.Captcha;
import com.marketing.system.accessCode.GifCaptcha;
import com.marketing.system.entity.SystemUser;
import com.marketing.system.service.UserInfoService;
import com.marketing.system.util.ApiResult;
import com.marketing.system.util.Constant;
import com.marketing.system.util.MyDES;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Api(description = "用户登录接口", value = "用户登录接口")
@Scope("prototype")
@RestController
//@Controller
@EnableAutoConfiguration
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    UserInfoService userInfoService;

    //权限测试用
    @RequestMapping(value="add", method = RequestMethod.POST)
    public String add() {
        return "add";
    }

    @ApiOperation(value = "未登录或登录已过期页面")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ApiResult<String> noPermissions(HttpServletRequest request) {
        ApiResult<String> result = null;
        result = new ApiResult<String>(Constant.NOSESSION_CODE_VALUE, "您未登录或登录已过期，请登录后再操作!", null, null);
        Session session = SecurityUtils.getSubject().getSession();
        String a = request.getRequestURI();
        String b = request.getPathInfo();
        String c = request.getServletPath();
        String c2 = request.getPathTranslated();
        String d = request.getHeader("Referer");
        StringBuffer e = request.getRequestURL();
        SavedRequest savedRequest = (SavedRequest) session.getAttribute("shiroSavedRequest");
        String u = savedRequest.getRequestURI();

        return result;
    }


    @ApiOperation(value = "用户打开登录页面")
    @RequestMapping(value = "loginOld", method = RequestMethod.POST)
    public ApiResult<String> login(HttpServletRequest request,HttpSession httpSession) {
        Session session = SecurityUtils.getSubject().getSession();
        ApiResult<String> result = null;
        if (session.getAttribute("lyout") == "" || session.getAttribute("lyout") == null) {

            result = new ApiResult<String>(200, "进入登录页面", null, null);

            return result;
        } else {
            String a = request.getRequestURI();
            String b = request.getPathInfo();
            String c = request.getServletPath();
            String c2 = request.getPathTranslated();
            String d = request.getHeader("Referer");
            StringBuffer e = request.getRequestURL();
            SavedRequest savedRequest = (SavedRequest) session.getAttribute("shiroSavedRequest");
            String u = savedRequest.getRequestURI();
            //登陆后退出
            String lyout = (String) session.getAttribute("lyout");
            if (lyout == "true" && !a.contains("login")) {
                //httpSession.setAttribute("lyout", "true2");
                result = new ApiResult<String>(400, "您未登录或登录已过期，请登录后再操作!", null, null);

                return result;
            } else {
                result = new ApiResult<String>(200, "进入登录页面", null, null);
                return result;
            }

        }

    }

    @ApiOperation(value = "用户登录处理")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = true, dataType = "String"),
            /*@ApiImplicitParam(paramType = "query", name = "accessCode", value = "验证码", required = true, dataType = "String")*/
    })
    @RequestMapping(value = "index",method = RequestMethod.GET)
    public ApiResult<Map<String,Object>> loginDeal(@RequestParam(value = "username", required = true) String username,
                                    @RequestParam(value = "password", required = true) String password,
                                           /*@RequestParam(value = "accessCode", required = false) String accessCode,*/
                                           HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {

        ApiResult<Map<String,Object>> r = null;
        Map<String,Object> map = new HashMap<>();
       /* if (accessCode == null || accessCode == "") {
            r = new ApiResult<SystemUser>(Constant.FAIL_CODE_PARAM_INSUFFICIENT, "验证码不能为空！", null, null);
            return r;
        }*/

        Session session = SecurityUtils.getSubject().getSession();
        //转化成小写字母
        //accessCode = accessCode.toLowerCase();
        //String v = (String) session.getAttribute("_code");
        //还可以读取一次后把验证码清空，这样每次登录都必须获取验证码
        //session.removeAttribute("_come");
        /*if (!accessCode.equals(v)) {
            r = new ApiResult<SystemUser>(Constant.FAIL_CODE_VALUE, "验证码错误！", null, null);
            return r;
        }*/
        //username = "陈冬和";
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            SecurityUtils.getSubject().login(token);



            //加密
            String data = MyDES.encryptBasedDes(password);
            //RememberMe这个参数设置为true后，在登陆的时候就会在客户端设置remenberme的相应cookie
            token.setRememberMe(true);

            SystemUser user = (SystemUser) SecurityUtils.getSubject().getPrincipal();

            map.put("users",user);
            map.put("token",subject.getSession().getId());
            r = new ApiResult<Map<String,Object>>(Constant.SUCCEED_CODE_VALUE, "登录成功！", map, null);
            //存入Session
            httpSession = request.getSession(true);
            httpSession.setAttribute("SysUser", token);
            httpSession.setAttribute("lyout", "false");
        } catch (Exception e) {
            logger.error("登录错误信息："+e.getMessage());
            r = new ApiResult<Map<String,Object>>(Constant.OTHER_CODE_VALUE, e.getMessage(), null, null);
            return r;

        }

        return r;
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation(value = "用户注销处理")
    @RequestMapping(value = "/boot/logout", method = RequestMethod.GET)
    public ApiResult<String> logout(HttpServletRequest request, HttpSession httpSession) {
        ApiResult<String> r = null;
        Session session = SecurityUtils.getSubject().getSession();
        try {
            //退出
            SecurityUtils.getSubject().logout();
            r = new ApiResult<String>(Constant.SUCCEED_CODE_VALUE, "退出成功！", null, null);
            httpSession = request.getSession(true);
            httpSession.setAttribute("lyout", "true");
        } catch (Exception e) {
            logger.error("退出："+e.getMessage());
        }
        return r;
    }

    /**
     * 获取验证码（Gif版本）
     *
     * @param response
     */
    @ApiOperation(value = "用户获取验证码")
    @RequestMapping(value = "getGifCode", method = RequestMethod.GET)
    public ApiResult<String> getGifCode(HttpServletResponse response, HttpServletRequest reques, HttpSession session) {
        ApiResult<String> r = null;
        try {
            /**
             * gif格式动画验证码
             * 宽，高，位数。
             */
            Captcha captcha = new GifCaptcha(146, 33, 4);
            //输出
            captcha.out(response.getOutputStream());
            session = reques.getSession(true);
            //存入Session
            session.setAttribute("_code", captcha.text().toLowerCase());
            r = new ApiResult<String>(Constant.SUCCEED_CODE_VALUE, "获取验证码成功！", captcha.text().toLowerCase(), null);
        } catch (Exception e) {
            logger.error("获取验证码异常：" + e.getMessage());
        }
        return r;
    }

    /**
     * 用户添加;
     * @return
     */
    @RequestMapping(value = "/userAdd", method = RequestMethod.POST)
    public ApiResult<String> userInfoAdd(){
        ApiResult<String> r = new ApiResult<>(200,"用户添加！",null,null);;
        return r;
    }

    /**
     * 用户添加;
     * @return
     */
    @RequestMapping(value = "/403", method = RequestMethod.POST)
    public ApiResult<String> noPeimission(){
        ApiResult<String> r = new ApiResult<>(403,"当前用户没有权限访问！",null,null);
        return r;
    }

    @RequestMapping(value = "/403403", method = RequestMethod.POST)
    public String noPeimission22(){
        ApiResult<String> r = null;
        return "没权限2222";
    }
}
