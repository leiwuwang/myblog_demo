package com.leiwuwang.api.login.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import com.leiwuwang.api.login.bean.UserInfo;
import com.leiwuwang.common.util.CodeConstants;
import com.leiwuwang.common.util.Response;

@Repository("userInfoHandler")
public class UserInfoHandler  extends SqlMapClientDaoSupport {

	public Response getList() {
		
		List<UserInfo> user= this.getSqlMapClientTemplate().queryForList("userInfo.getAll");
		
		return null;
	}
	
	public Response get(String uuid) {
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("uuid", uuid);
		List<UserInfo> user= this.getSqlMapClientTemplate().queryForList("userInfo.getByUuid",params);
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public Response checkLogin(String username, String pwd) {
		
		Response res = new Response();
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("username", username);
		params.put("pwd", pwd);
		List<UserInfo> userList= (List<UserInfo>)this.getSqlMapClientTemplate().queryForList("userInfo.checkLogin",params);
		
		if(null != userList && userList.size()>0) {
			
			res.setCode(CodeConstants.SUCCESS_CODE);
			res.setMessage(CodeConstants.SUCCESS_CODE_MESSAGE);
			res.setReturnValue(userList.get(0));
			
		}else {
			res.setCode(CodeConstants.FAIL_CODE);
			res.setMessage("用户名或密码错误!");
			res.setReturnValue(null);
		}
		
		return res;
	}

}
