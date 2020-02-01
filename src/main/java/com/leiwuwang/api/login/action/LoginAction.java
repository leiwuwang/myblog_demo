package com.leiwuwang.api.login.action;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.leiwuwang.api.login.bean.UserInfo;
import com.leiwuwang.api.login.i.IUserInfo;
import com.leiwuwang.common.util.CodeConstants;
import com.leiwuwang.common.util.Constants;
import com.leiwuwang.common.util.RedisDbUtil;
import com.leiwuwang.common.util.Response;
import com.leiwuwang.common.util.Struts2Utils;
/**
 * @Project api
 * @Description 用户登录注册操作
 * @Author leiwang
 * @Create 2018年9月28日 下午6:16:53
 * @Modified By:
 * @Modified Date:
 * @Why <修改原因描述>
 */
public class LoginAction {
	
	private String _username;//登录名
	
	private String _pwd;//密码
	
	private UserInfo _user;//注册用户所需数据
	
	@Autowired
	private IUserInfo iUserInfo;

	/**
	* @Description 登录
	* @Author leiwang
	* @Date 2018年9月28日 下午6:17:21
	 */
	public void login() {
		
		if(StringUtils.isBlank(_username)) {
			Struts2Utils.renderText("{\"code\":1002,\"message\":\"请输入登录名！\"}");
			return;
		}
		
		if(StringUtils.isBlank(_pwd)) {
			Struts2Utils.renderText("{\"code\":1002,\"message\":\"请输入密码！\"}");
			return;
		}
		
		//密码md5加密
		try {
			
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			
			byte[] result = md5.digest(_pwd.getBytes());
			
			//加密后的字符串
			_pwd = byteArray2HexStr(result);//转成字符串
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			Struts2Utils.renderText("{\"code\":1002,\"message\":\"登录失败,用户名或密码错误！\"}");
			return;
		}
		
		Response res = iUserInfo.checkLogin(_username, _pwd);
		
		if(null != res && res.getCode() == CodeConstants.SUCCESS_CODE && res.getReturnValue() != null) {
			
			//登录成功,将用户信息转成字节存入redis中
			UserInfo userInfo = (UserInfo)res.getReturnValue();
			
			String userKey = Constants.LOGIN_BEAN_KEY + userInfo.getUuid();
			
			try {
				
				//判断是否已存在key
				byte[] userKeyByte = userKey.getBytes("utf-8");
				
				boolean exitFlag = RedisDbUtil.exists(userKeyByte, Constants.REDIS_USER_INFO_DB);
				
				if(exitFlag) {//如果存在,选删除
					RedisDbUtil.del(userKeyByte, Constants.REDIS_USER_INFO_DB);
				}
				
				String userInfoJson = JSON.toJSONString(userInfo);
				
				RedisDbUtil.set(userKeyByte, userInfoJson.getBytes("utf-8"), Constants.REDIS_USER_INFO_DB);
				
				Map<String,Object> result = new HashMap<String,Object>();
				result.put("code", 1000);
				result.put("message", "登录成功!");
				result.put("userInfo", userInfoJson);
				
				String resultJson = JSON.toJSONString(result);
				
				Struts2Utils.renderText(resultJson);
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				Struts2Utils.renderText("{\"code\":1003,\"message\":\"登录接口异常,请联系管理员！\"}");
			}
			
		}else {
			Struts2Utils.renderText("{\"code\":1002,\"message\":\"登录失败,用户名或密码错误！\"}");
		}
		
	}
	
	/**
	 * 注册接口
	* @Description 注册
	* @Author leiwang
	* @Date 2018年9月28日 下午6:16:39
	 */
	public void register() {
		
		
		//注册成功,将用户信息转成字节存入redis中
	}
	
	/**
	 * 处理字节数组得到MD5密码的方法
	 */
	private static String byteArray2HexStr(byte[] bs) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bs) {
			sb.append(byte2HexStr(b));
		}
		return sb.toString();
	}
	
	/**
	 * 字节标准移位转十六进制方法
	 */
	private static String byte2HexStr(byte b) {
		String hexStr = null;
		int n = b;
		if (n < 0) {
			// 若需要自定义加密,请修改这个移位算法即可
			n = b & 0x7F + 333;
		}
		hexStr = Integer.toHexString(n / 16) + Integer.toHexString(n % 16);
		return hexStr.toUpperCase();
	}

	public String get_username() {
		return _username;
	}

	public void set_username(String _username) {
		this._username = _username;
	}

	public String get_pwd() {
		return _pwd;
	}

	public void set_pwd(String _pwd) {
		this._pwd = _pwd;
	}

	public UserInfo get_user() {
		return _user;
	}

	public void set_user(UserInfo _user) {
		this._user = _user;
	}
	
	
}
