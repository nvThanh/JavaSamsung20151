package bachkhoa.javasamsung.control;

import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;

public class DropboxManager {
	private final String APP_KEY = "0olubd4zreas1ov";
	private final String APP_SECRECT = "ojh19hwhpwjsmwq";
	private DbxAppInfo appInfo;
	private DbxRequestConfig requestConfig;
	private DbxWebAuthNoRedirect webAuthNoRedirect;
	private String authorizeUrl;
	private DbxAuthFinish authFinish;
	private String accessToken;
	private DbxClient client;
	public DbxAppInfo getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(DbxAppInfo appInfo) {
		this.appInfo = appInfo;
	}

	public DbxRequestConfig getRequestConfig() {
		return requestConfig;
	}

	public void setRequestConfig(DbxRequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}

	public DbxWebAuthNoRedirect getWebAuthNoRedirect() {
		return webAuthNoRedirect;
	}

	public void setWebAuthNoRedirect(DbxWebAuthNoRedirect webAuthNoRedirect) {
		this.webAuthNoRedirect = webAuthNoRedirect;
	}

	public String getAuthorizeUrl() {
		return authorizeUrl;
	}

	public void setAuthorizeUrl(String authorizeUrl) {
		this.authorizeUrl = authorizeUrl;
	}

	public DropboxManager() {
		super();
	}
	public void init(){
		System.out.println(authorizeUrl);
		appInfo = new DbxAppInfo(APP_KEY, APP_SECRECT);
		requestConfig = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
		webAuthNoRedirect = new DbxWebAuthNoRedirect(requestConfig, appInfo);
		authorizeUrl = webAuthNoRedirect.start();
		System.out.println(authorizeUrl);
	}
	public void finishAuthentication(String code){
		try {
			authFinish = webAuthNoRedirect.finish(code);
			accessToken = authFinish.accessToken;
			client = new DbxClient(requestConfig, accessToken);
			System.out.println("Done");
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public DbxAuthFinish getAuthFinish() {
		return authFinish;
	}

	public void setAuthFinish(DbxAuthFinish authFinish) {
		this.authFinish = authFinish;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public DbxClient getClient() {
		return client;
	}

	public void setClient(DbxClient client) {
		this.client = client;
	}
}
