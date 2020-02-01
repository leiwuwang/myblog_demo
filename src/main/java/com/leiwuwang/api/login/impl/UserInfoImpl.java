package com.leiwuwang.api.login.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leiwuwang.api.login.handler.UserInfoHandler;
import com.leiwuwang.api.login.i.IUserInfo;
import com.leiwuwang.common.util.Response;

@Repository("userInfoImpl")
public class UserInfoImpl implements IUserInfo {
	
	@Autowired
	private UserInfoHandler dao;

	@Override
	public Response getList() {
		return dao.getList();
	}

	@Override
	public Response get(String uuid) {
		return dao.get(uuid);
	}

	@Override
	public Response checkLogin(String username, String pwd) {
		return dao.checkLogin(username, pwd);
	}

}
