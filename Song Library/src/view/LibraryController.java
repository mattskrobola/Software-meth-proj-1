package view;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class LibraryController {
	
	
	@FXML         
	ListView<String> listView;
	
	private ObservableList<String> songList;
	
	public void start(Stage mainStage) throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader("songlist"));
		songList = FXCollections.observableArrayList(in.next());
		listView.setItems(songList);
	}
}
