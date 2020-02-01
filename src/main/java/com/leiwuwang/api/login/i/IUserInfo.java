package com.leiwuwang.api.login.i;

import com.leiwuwang.common.util.Response;

public interface IUserInfo {

	public Response getList();
	
	public Response get(String uuid);
	
	public Response checkLogin(String username,String pwd);
	
}
