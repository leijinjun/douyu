package com.lei2j.douyu.im;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lei2j
 */
public class LoginMessage implements Message{

    private String username;

    private String password;
    
    private static final ChatType CHAT_TYPE = ChatType.LOGIN;

    public LoginMessage() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	@Override
	public String getBody() {
		return JSONObject.toJSONString(this).concat(END_FLAG);
	}

	@Override
	public ChatType getType() {
		return CHAT_TYPE;
	}
}
