package com.kt.ucloud.api;

import java.util.HashMap;

import com.kt.openplatform.sdk.KTOpenApiHandler;

public class UcloudApiManager {

	private String strAuthKey = "";
	private String strAuthSecret = "";
	private KTOpenApiHandler apiHandler;
	

	public UcloudApiManager(String authKey , String authSecret)
	{
		this.strAuthKey = authKey;
		this.strAuthSecret = authSecret;
	}
	
	public KTOpenApiHandler getHandler()
	{
		return this.apiHandler;
	}
	
	public boolean connect()
	{
		this.apiHandler = KTOpenApiHandler.createHandler(strAuthKey, strAuthSecret);
		if(this.apiHandler == null)
		{
			System.out.println("APIHandler를 연결하는데 오류가 발생했습니다.");
			return false;
		}
		return true;
	}

	public HashMap<?,?> apiCall(String api_id) {
		return this.apiCall(api_id , new HashMap<String , String>());
	}

	public HashMap<?,?> apiCall(String api_id, HashMap<String, String> params) {
		
		// https
		boolean bSSL = false;

		if(apiHandler == null && this.connect() == false) {
			return null;
		} 
		
		HashMap<?,?> r = apiHandler.call(api_id, params, null, bSSL);

		return r;
	}

	public boolean isSuccess(HashMap<? , ?> result)
	{
		return Integer.valueOf((String)result.get("result_code")) < 300;
	}
	
	public String getFullURL(HashMap<?,?> result)
	{
		String redirectUrl = result.get("redirect_url").toString();
		String fileToken = result.get("file_token").toString();
		return getFullURL(redirectUrl , fileToken);
	}
	
	public String getFullURL(String redirectUrl, String fileToken)
	{
		String apiToken = apiHandler.makeApiToken();
		return redirectUrl + "?api_token=" + apiToken + "&file_token=" + fileToken;
	}

}
