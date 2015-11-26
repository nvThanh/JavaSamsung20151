package bachkhoa.javasamsung.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;

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
	private ArrayList<String> listPath;
	private ObservableList<String> listFolder = FXCollections.observableArrayList();
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
			System.out.println("Logged In to "+ client.getAccountInfo().displayName);
			
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
	public ObservableList<String> listAllFolder(){
		//ObservableList<String> listFolder = FXCollections.observableArrayList();
		try {
			DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
			for(DbxEntry child : listing.children){
				listFolder.add(child.path);
			}
			return listFolder;
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void upLoadFiles() throws IOException, DbxException{
		 JFileChooser fileopen = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("c files", "c");
		    fileopen.addChoosableFileFilter(filter);

		    int ret = fileopen.showDialog(null, "Open file");

		    if (ret == JFileChooser.APPROVE_OPTION) {
		      File file = fileopen.getSelectedFile();
		FileInputStream inputStream = new FileInputStream(file);
		try {
		    DbxEntry.File uploadedFile = client.uploadFile("/" + file.getName(),
		        DbxWriteMode.add(), file.length(), inputStream);
		    listFolder.add("/"+file.getName());
		    System.out.println("Uploaded: " + uploadedFile.toString());
		} finally {
		    inputStream.close();
		}}
		    
}
	public void downLoadFiles(String global){
	try{
		File folder = new File("C:/Users/quang/Documents/dropboxdata");
		if(!folder.exists()){
			folder.mkdirs();
		}
		File file = new File(folder, global.replace("/", ""));
		FileOutputStream outputStream = new FileOutputStream(file);
		try {
		    DbxEntry.File downloadedFile = client.getFile(global, null,
		        outputStream);
		    System.out.println("Metadata: " + downloadedFile.toString());
		} finally {
		    outputStream.close();
		}
	}
	catch(Exception e){
		System.out.println("Error file!");
	};
	
	}
	
	public void reNameFiles(String global, String myFile, int globalIndex){
		try{
			DbxEntry renameFile = client.move(global, myFile);
			listFolder.remove(1);
			 listFolder.add( myFile);
		}
		catch(Exception e){}
	}
	public void deleteFiles(String global, int globalIndex){
		try{
			client.delete(global);
			listFolder.remove(globalIndex);	
		}
		catch(Exception e){}
	}
	}
