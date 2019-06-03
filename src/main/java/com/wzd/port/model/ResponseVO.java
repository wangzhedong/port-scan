package com.wzd.port.model;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 返回到页面对象
 * @author guanhongwei
 *
 */
@Data
public class ResponseVO implements Serializable {
	
	private static final long serialVersionUID = 350939574321573616L;


	private static final String success_status = "1";
	private static final String success_msg = "请求成功";
	private static final String success_code = "success";

	private static final String fail_status = "0";
	private static final String fail_msg = "请求失败";
	private static final String fail_code = "fail";


	/**
	 * 消息状态
	 */
	private String status = "";
	
	/**
	 * 交互结果消息描述
	 */
	private String msg = "";
	
	/**
	 * 消息体
	 */
	private Object data;
	
	/**
	 * 消息代码
	 */
	private String code = "";
	
	/**
	 * 成功默认消息
	 * @return
	 */
	private ResponseVO() {
		this.status = success_status;
		this.msg = success_msg;
		this.code = success_code;
	}
	
	/**
	 * 成功默认消息
	 * @return
	 */
	public static ResponseVO successInstance() {
		return new ResponseVO();
	}
	
	/**
	 * 成功-返回数据
	 * @param data
	 * @return
	 */
	public static ResponseVO successInstance(Object data) {
		ResponseVO res = successInstance();
		res.setData(data);
		return res;
	}
	
	/**
	 * 失败错误消息
	 * @return
	 */
	public static ResponseVO errorInstance() {
		ResponseVO res = successInstance();
		res.setStatus(fail_status);
		res.setCode(fail_code);
		res.setMsg(fail_msg);
		return res;
	}
	
	/**
	 * 失败错误消息
	 * @param msg
	 * @return
	 */
	public static ResponseVO errorInstance(String msg) {
		ResponseVO res = successInstance();
		res.setStatus(fail_status);
		res.setCode(fail_code);
		res.setMsg(StringUtils.isEmpty(msg) ? fail_msg : msg);
		return res;
	}
	
	/**
	 * 失败错误消息
	 * @param code 消息代码
	 * @return
	 */
	public static ResponseVO errorInstance(String code, String msg) {
		ResponseVO res = successInstance();
		res.setStatus(fail_status);
		res.setCode(StringUtils.isEmpty(code) ? fail_code : code);
		res.setMsg(StringUtils.isEmpty(msg) ? fail_msg : msg);
		return res;
	}
	
	/**
	 * 失败错误消息
	 * @param status 状态代码
	 * @param code  消息代码
	 * @param msg
	 * @return
	 */
	public static ResponseVO errorInstance(String status,String code, String msg) {
		ResponseVO res = successInstance();
		res.setStatus(StringUtils.isEmpty(status) ? fail_status : status);
		res.setCode(StringUtils.isEmpty(code) ? fail_code : code);
		res.setMsg(StringUtils.isEmpty(msg) ? fail_msg : msg);
		return res;
	}
	
	/**
	 * 获取指定类型的对象
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData(Class<T> cls){
		return (T)this.getData();
	}

}
