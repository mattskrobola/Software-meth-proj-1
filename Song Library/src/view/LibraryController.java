package view;

import java.io.*;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LibraryController {
	
	
	@FXML         
	ListView<String> listView;
	
	@FXML
	private Button addButton;
	
	@FXML
	private Button editButton;
	
	@FXML
	private Button deleteButton;
	
	@FXML
	private TextField addText;
	
	private ObservableList<String> songList;
	
	public void start(Stage mainStage) throws IOException, FileNotFoundException, ClassNotFoundException {
		
		// if the file is empty, create a new list, otherwise read it from the file
		
		songList = FXCollections.observableArrayList();
		
		Scanner in = new Scanner(new FileReader("songlist"));
		while (in.hasNext()) {
			songList.add(in.next());
		}
		
		in.close();
		
		listView.setItems(songList);
		
		// selects the first song upon starting the app
		listView.getSelectionModel().select(0);
		
		// eventually make separate methods for event handling
		
		addButton.setOnAction((event) -> {
			
			if (!addText.getText().isEmpty()) songList.add(addText.getText());	
			
			// write list to file here
		});
		
		editButton.setOnAction((event) -> {
			
			int index = listView.getSelectionModel().getSelectedIndex();
			
			if (!addText.getText().isEmpty() && index >= 0) songList.set(index, addText.getText());
			
			// write list to file here
		});
		
		deleteButton.setOnAction((event) -> {
			
			int index = listView.getSelectionModel().getSelectedIndex();
			
			if (!songList.isEmpty() && index >= 0) songList.remove(index);
			
			// write list to file here
		});
		
	}
	
	
}
