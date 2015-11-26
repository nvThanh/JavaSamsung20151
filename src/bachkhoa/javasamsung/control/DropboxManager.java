package bachkhoa.javasamsung.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
	public ObservableList<String> listFolder = FXCollections.observableArrayList();
	private String currentFolder;
	
	public String getCurrentFolder() {
		return currentFolder;
	}

	public void setCurrentFolder(String currentFolder) {
		this.currentFolder = currentFolder;
	}

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
	public ObservableList<String> listAllFolder(String folder){
		//ObservableList<String> listFolder = FXCollections.observableArrayList();
		try {
			DbxEntry.WithChildren listing = client.getMetadataWithChildren(folder);
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
		    listFolder.clear();
			listFolder = listAllFolder(currentFolder);
		    System.out.println("Uploaded: " + uploadedFile.toString());
		} finally {
		    inputStream.close();
		}}
		    
}
	public void reNameFile(String newName, String currentName){
		String name = currentName.replace(currentFolder, "");
		String name1 = name.substring(name.lastIndexOf("."), name.length()-1);
		String name2 = newName +name1;
		System.out.println(name2);
		try {
			client.move(currentName, currentFolder+name2);
			listFolder.clear();
			listFolder = listAllFolder(currentFolder);
			System.out.println("Renamed"+newName);
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void deleteFile(String fileName){
		try {
			client.delete(fileName);
			listFolder.clear();
			listFolder = listAllFolder(currentFolder);
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void dowloadFile(String file){
		File folder = new File("F:\\Java2015Test");
		if(!folder.exists()){
			folder.mkdirs();
		}
		File dowload = new File(folder, file.replace(currentFolder, ""));
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(dowload);
			client.getFile(file, null, fileOutputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	}
