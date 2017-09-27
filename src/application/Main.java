package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import view.LibraryController;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) 
	throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/SongLibrary.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		LibraryController libraryController = loader.getController();
		libraryController.start(primaryStage);
		
		Scene scene = new Scene(root,400,400);
		// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
