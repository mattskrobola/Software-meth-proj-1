package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class LibraryController {
	
	
	@FXML         
	ListView<String> listView;
	
	private ObservableList<String> songList;
	
	public void start(Stage mainStage) {
		songList = FXCollections.observableArrayList("All Star","Sandstorm");
		listView.setItems(songList);
	}
}
