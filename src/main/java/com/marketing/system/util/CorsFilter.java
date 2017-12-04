package com.marketing.system.util;


import com.marketing.system.entity.TokenRecord;
import com.marketing.system.mapper_two.TokenRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class CorsFilter implements Filter {

    @Autowired
    TokenRecordMapper tokenRecordMapper;

    final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CorsFilter.class);

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;

        HttpServletRequest reqs = (HttpServletRequest) req;

        response.setHeader("Access-Control-Allow-Origin", reqs.getHeader("Origin"));
        //response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization,SessionToken");

        req.setCharacterEncoding("UTF-8");

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/gif");
        response.setContentType("application/json");
        chain.doFilter(req, res);
        String token = reqs.getHeader("Authorization");

        Map<String, Object> rec = new LinkedHashMap<String, Object>();
        String url = reqs.getRequestURI();

        try {
           /* List<TokenRecord> list = tokenRecordMapper.selectByToken(token);
            boolean isFilter = false;
            if (null == token || token.isEmpty()) {
                if (url.contains("index") || url.contains("doc") || url.contains("webjars/bycdao") || url.contains("getGifCode")) {
                    isFilter = true;
                } else {
                    rec.put("code", Constant.FAIL_CODE_VALUE);
                    rec.put("msg", "token没有认证通过!原因为：客户端请求参数中无token信息!");
                    rec.put("data", null);
                    rec.put("page", null);

                    JsonUtil.writeJson(rec, response);
                    return;
                }

            } else if (list.size() < 0 || list.size() == 0) {
                rec.put("code", Constant.FAIL_CODE_VALUE);
                rec.put("msg", "token没有认证通过!原因为：token信息错误!");
                rec.put("data", null);
                rec.put("page", null);
                JsonUtil.writeJson(rec, response);
                return;

            } else {
                isFilter = true;
            }
            if (isFilter) {
                logger.info("token filter过滤ok!");
                chain.doFilter(req, res);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询token出错：" + e.getMessage());
        }
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}
