package com.yc.property.util;


/**
 * VO:
 * 借助此对象封装服务端的影响数据
 * 1)响应的状态码 (1表示正常数据,0表示异常数据)
 * 2)响应消息 (呈现给用户的消息,例如一个弹出框中的数据)
 * 3)响应数据 (要呈现的正常数据,例如日志记录信息)
 * 4)....
 */
public class JsonResult {
	/**状态码*/
    private int state=1;
    /**状态码对应的状态信息*/
    private String message="ok";
    /**正常数据*/
    private Object data;
    public JsonResult(String message) {
    	this.message=message;
    }
    public JsonResult(Object data) {
    	this.data=data;
    }
    /**封装异常数据*/
    public JsonResult(Throwable e) {
    	this.state=0;
    	this.message=e.getMessage();
    }
    public JsonResult() {
    }


    public JsonResult(int state, String message, Object data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "state=" + state +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}











