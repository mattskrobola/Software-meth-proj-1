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
		
		songList = FXCollections.observableArrayList();
		
		Scanner in = new Scanner(new FileReader("songlist"));
		while (in.hasNext()) {
			songList.add(in.next());
		}
		
		in.close();
		
		listView.setItems(songList);
		
		listView.getSelectionModel().select(0);
		
		// eventually make separate methods for event handling
		
		addButton.setOnAction((event) -> {
			songList.add(addText.getText());	
			// write list to file
		});
		
		editButton.setOnAction((event) -> {
			int index = listView.getSelectionModel().getSelectedIndex();
			songList.set(index, addText.getText());
			// write list to file
		});
		
		deleteButton.setOnAction((event) -> {
			int index = listView.getSelectionModel().getSelectedIndex();
			// add error check
			songList.remove(index);
			// write list to file
		});
		
	}
	
	
}
