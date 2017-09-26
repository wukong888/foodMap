package com.marketing.system.util;


import io.swagger.annotations.ApiModelProperty;



public class ApiResult<T> {

    public ApiResult(int c,String m,T r,RdPage p)
    {
        code=c;
        msg=m;
        data=r;
        page=p;
    }

    private int code;
    private String msg;
    private T data;
    private RdPage page;

    @ApiModelProperty("状态码:0成功，非0失败")
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    @ApiModelProperty("消息")
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    @ApiModelProperty("返回结果")
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @ApiModelProperty("页数")
    public RdPage getPage() {
        return page;
    }

    public void setPage(RdPage page) {
        this.page = page;
    }
}
