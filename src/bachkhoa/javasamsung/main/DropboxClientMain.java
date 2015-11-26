package bachkhoa.javasamsung.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.dropbox.core.DbxEntry;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class DropboxClientMain extends Application {
	private DropboxManager manager;
	public String global;
	private int globalIndex;
	private String currentFolder;
	private ArrayList<String> folderList1;

	public void start(Stage arg0) throws Exception {
		manager = new DropboxManager();
		currentFolder = "/";
		folderList1 = new ArrayList<String>();
		folderList1.add(currentFolder);
		manager.setCurrentFolder(currentFolder);
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
				if (btnSigIn.getText().equals("SigIn")) {
					btnSigIn.setText("OK");
					manager.init();
					engine.load(manager.getAuthorizeUrl());
				} else {
					System.out.println("Ok clicking");
					System.out.println(textField.getText().toString());
					manager.finishAuthentication(textField.getText().toString()
							.trim());
					Node node = (Node) arg0.getSource();
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

	public void showListFolder(DropboxManager manager) {
		Stage stage = new Stage();
		try {
			stage.setTitle(manager.getClient().getAccountInfo().displayName);
			ListView<String> folderList = new ListView<>();
			folderList.setItems(manager.listAllFolder(currentFolder));
			folderList.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					// TODO Auto-generated method stub
					System.out.println("clicked on "
							+ folderList.getSelectionModel().getSelectedItem());
					global = new String(folderList.getSelectionModel()
							.getSelectedItem());
					globalIndex = folderList.getSelectionModel()
							.getSelectedIndex();
				}
			});
			GridPane logginGrid = new GridPane();
			logginGrid.setAlignment(Pos.CENTER);
			logginGrid.setVgap(10);
			logginGrid.setHgap(10);
			logginGrid.setPadding(new Insets(25, 25, 25, 25));
			logginGrid.add(folderList, 0, 0);
			Button button = new Button("DeleteFile");
			button.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					// TODO Auto-generated method stub
					System.out.println("On action event");
					Thread deleteThread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							manager.deleteFile(global);
						}
					});
					deleteThread.start();
				}
			});
			logginGrid.add(button, 0, 1);

			Button btnRename = new Button("Rename");
			btnRename.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg1) {
					// TODO Auto-generated method stub
					System.out.println("On action event");
					Thread renameThread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String newName = JOptionPane.showInputDialog(null,
									"Please enter new name", null,
									JOptionPane.QUESTION_MESSAGE);
							manager.reNameFile(global + newName, global);
						}
					});
					renameThread.start();
				}
			});
			logginGrid.add(btnRename, 0, 2);

			Button btnUpload = new Button("UpLoad");
			btnUpload.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg2) {
					// TODO Auto-generated method stub
					Thread thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								manager.upLoadFiles();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (DbxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					thread.start();
				}
			});
			logginGrid.add(btnUpload, 0, 3);

			Button btnDownload = new Button("DownLoad");
			btnDownload.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg3) {
					// TODO Auto-generated method stub
					System.out.println("On action event");
					Thread dowloadFile = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							manager.dowloadFile(global);
						}
					});
					dowloadFile.start();
				}
			});
			logginGrid.add(btnDownload, 0, 4);

			Button btnOpen = new Button("Open");
			btnOpen.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg3) {
					// TODO Auto-generated method stub
					if (global.charAt(global.length() - 4) == '.') {
						// manager.dowloadFile(global);

						try {
							manager.dowloadFile(global);
							File file = new File("F:\\Java2015Test\\"+(global.replace(currentFolder, "")));
							Desktop.getDesktop().open(file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						currentFolder = global;
						DropboxClientMain.this.folderList1.add(currentFolder);
						manager.setCurrentFolder(currentFolder);
						manager.listFolder.clear();
				manager.listFolder =		manager.listAllFolder(currentFolder);
					}
				}
			});
			logginGrid.add(btnOpen, 0, 5);
			Button btnBack = new Button("Back");
			btnBack.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg3) {
					// TODO Auto-generated method stub
					folderList1.remove(folderList1.size() -1);
					currentFolder = folderList1.get(folderList1.size() - 1);
					manager.setCurrentFolder(currentFolder);
					manager.listFolder.clear();
					manager.listFolder = manager.listAllFolder(currentFolder);
				}
			});
			logginGrid.add(btnBack, 0, 6);
			Scene logginScne = new Scene(logginGrid, 320, 480);
			stage.setScene(logginScne);
			stage.show();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		launch(args);
	}
	public void listAllFile(String folder){
		
	}

}
