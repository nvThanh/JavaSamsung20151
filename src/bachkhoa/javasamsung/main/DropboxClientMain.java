package bachkhoa.javasamsung.main;

import com.dropbox.core.DbxException;

import bachkhoa.javasamsung.control.DropboxManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class DropboxClientMain extends Application {
	private DropboxManager manager;
	
	public void start(Stage arg0) throws Exception {
		 manager = new DropboxManager();
		// TODO Auto-generated method stub
		GridPane logginGrid = new GridPane();
		logginGrid.setAlignment(Pos.CENTER);
		logginGrid.setVgap(10);
		logginGrid.setHgap(10);
		logginGrid.setPadding(new Insets(25, 25, 25, 25));
		TextField textField = new TextField();
		textField.setPromptText("Copy then paste accesstoken here");
		
		Label label = new Label();
		label.setText("Please Sigin Dropbox Client");
		label.setWrapText(true);
		Image image = new Image(getClass().getResourceAsStream(
				"/dropbox-icon.png"));
		label.setGraphic(new ImageView(image));
		label.setFont(new Font("TimeNewRoman", 30));
		// Button sigin
		Button btnSigIn = new Button("SigIn");
		WebView webView = new WebView();
		WebEngine engine = webView.getEngine();
		btnSigIn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(btnSigIn.getText().equals("SigIn")){
					btnSigIn.setText("OK");
					manager.init();
					engine.load(manager.getAuthorizeUrl());
				} else {
				System.out.println("Ok clicking");
				System.out.println(textField.getText().toString());
				manager.finishAuthentication(textField.getText().toString().trim());
				Node node =(Node) arg0.getSource();
				node.getScene().getWindow().hide();
				showListFolder(manager);
				}
			}
		});
		logginGrid.add(textField, 0, 4);
		logginGrid.add(webView, 0, 3);
		logginGrid.add(label, 0, 0);
		logginGrid.add(btnSigIn, 0, 2);
		Scene logginScne = new Scene(logginGrid, 640, 480);
		arg0.setTitle("Welcome to Dropbox Client");
		arg0.setScene(logginScne);
		arg0.show();
	}
	public void showListFolder(DropboxManager manager){
		Stage stage = new Stage();
		try {
			stage.setTitle(manager.getClient().getAccountInfo().displayName);
			ListView<String> folderList = new ListView<>();
			folderList.setItems(manager.listAllFolder());
			GridPane logginGrid = new GridPane();
			logginGrid.setAlignment(Pos.CENTER);
			logginGrid.setVgap(10);
			logginGrid.setHgap(10);
			logginGrid.setPadding(new Insets(25, 25, 25, 25));
			logginGrid.add(folderList, 0, 0);
			Scene logginScne = new Scene(logginGrid, 640, 480);
			stage.setScene(logginScne);
			stage.show();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		launch(args);
	}

}
