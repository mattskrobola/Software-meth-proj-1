/* 
 *  Matt Skrobola and Kyle Reagle 
 */

package view;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

import application.Song;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LibraryController {
	ArrayList<Song> songs;
	
	@FXML         
	ListView<String> listView;
	
	@FXML
	private Button addButton;
	
	@FXML
	private Button editButton;
	
	@FXML
	private TextField addArtist;
	
	@FXML
	private Button deleteButton;
	
	@FXML
	private TextField addSongName;
	
	@FXML
	private TextField addAlbum;
	
	@FXML
	private TextField addYear;
	
	@FXML         
	ListView<String> detailsView;
	
	
	private ObservableList<String> songList;
	private ObservableList<String> detailsList;
	
	public void start(Stage mainStage) throws IOException, FileNotFoundException, ClassNotFoundException {
		
		//if the file is empty the inputstream will crash so adding an empty arraylist if file is empty
		File file = new File("songlist");
		file.createNewFile(); // does not overwrite file if it exists already
		
		BufferedReader br = new BufferedReader(new FileReader("songlist"));
		if (br.readLine() == null) {
		    writeToFile(new ArrayList<Song>());
		}
		
		br.close();
 
		
		// if the file is empty, create a new list, otherwise read it from the file
		songList = FXCollections.observableArrayList();
		detailsList = FXCollections.observableArrayList();
		
		//since observableList isn't serializable we need to keep this list up to date for the file
		songs = readFromFile();
		
		for(int i =0; i < songs.size(); i++) {
			songList.add(songs.get(i).toString());
		}
		
		listView.setItems(songList);
		
		// selects the first song upon starting the app
		listView.getSelectionModel().select(0);
		
		if(!songs.isEmpty()) {
			Song tempSong = songs.get(0);
			detailsList.add("Song Name: " + tempSong.getSongName());
			detailsList.add("Artist: " + tempSong.getArtist());
			if(!tempSong.getAlbum().equals("")) detailsList.add("Album : " + tempSong.getAlbum());
			if(!tempSong.getYear().equals("")) detailsList.add("Year: " + tempSong.getYear());
			detailsView.setItems(detailsList);
		}
		
		// eventually make separate methods for event handling
		
		//each time we update/add/delete we sort the arraylist,update the observable list, and write the new list of songs to the file
		
		addButton.setOnAction((event) -> {
			//TODO check if song exists
			if(!exists(new Song(addSongName.getText(), addArtist.getText(), addAlbum.getText(), addYear.getText()))){
				if (!addSongName.getText().isEmpty() && !addArtist.getText().isEmpty()) {
					if (confirmAction(mainStage,"Are you sure you want to add this song?")) {
						songs.add(new Song(addSongName.getText(), addArtist.getText(), addAlbum.getText(), addYear.getText()));
						sortAndUpdate(songs);
						try {
							writeToFile(songs);
						} catch (IOException e) {
							//change this
							e.printStackTrace();
						}
						listView.setItems(songList);
						int tempIndex = findIndex(songs, addSongName.getText(), addArtist.getText());
						listView.getSelectionModel().select(tempIndex);
						clearTextFields();
					}
				}
				
			} else {
				warning(mainStage, "The song that you're trying to add already exists!");
			}
			
			// write list to file here
		});
		
		editButton.setOnAction((event) -> {
			
			if(!exists(new Song(addSongName.getText(), addArtist.getText(), addAlbum.getText(), addYear.getText()))){
			
				int index = listView.getSelectionModel().getSelectedIndex();
				
				if (index >= 0 && confirmAction(mainStage,"Are you sure you want to edit this song?")) {
					Song tempSong = songs.get(index);
					
					if(!addSongName.getText().isEmpty()) {
						tempSong.setSongName(addSongName.getText());
					}
					if(!addArtist.getText().isEmpty()) {
						tempSong.setArtist(addArtist.getText());
					}
					if(!addYear.getText().isEmpty()){
						tempSong.setYear(addYear.getText());
					}
					if(!addAlbum.getText().isEmpty()) {
						tempSong.setAlbum(addAlbum.getText());
					}
					songs.set(index,tempSong);
					sortAndUpdate(songs);
					try {
						writeToFile(songs);
					} catch (IOException e) {
						//change this
						e.printStackTrace();
					}
					listView.setItems(songList);
					clearTextFields();
					listView.getSelectionModel().select(index);
				}
				
				
			} else {
				warning(mainStage, "The song that you're trying to edit already exists somewhere else!");
			}
			// write list to file here
		});
		
		deleteButton.setOnAction((event) -> {
			
			int index = listView.getSelectionModel().getSelectedIndex();
			
			if (!songList.isEmpty() && index >= 0 && confirmAction(mainStage,"Are you sure you want to delete this song?")) {
				int selectIndex = listView.getSelectionModel().getSelectedIndex();
				songs.remove(index);
				sortAndUpdate(songs);
				try {
					writeToFile(songs);
				} catch (IOException e) {
					//change this
					e.printStackTrace();
				}
				listView.setItems(songList);
				if(songs.size() <= selectIndex)listView.getSelectionModel().select(selectIndex-1);
				else listView.getSelectionModel().select(selectIndex);
			}
			
			// write list to file here
		});
		
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				detailsList.clear();
				if(!listView.getSelectionModel().isEmpty()) {
					Song tempSong = songs.get(listView.getSelectionModel().getSelectedIndex());
					detailsList.add("Song Name: " + tempSong.getSongName());
					detailsList.add("Artist: " + tempSong.getArtist());
					if(!tempSong.getAlbum().equals("")) detailsList.add("Album : " + tempSong.getAlbum());
					if(!tempSong.getYear().equals("")) detailsList.add("Year: " + tempSong.getYear());
				}
				detailsView.setItems(detailsList);
			}
		});

		
	}
	public void writeToFile(ArrayList<Song> songList) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("songlist"));
		objectOutputStream.writeObject(songList);
		objectOutputStream.close();
	}
	
	public ArrayList<Song> readFromFile() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("songlist"));
		return (ArrayList<Song>) objectInputStream.readObject();
		
	}
	
	public void sortAndUpdate(ArrayList<Song> songs) {
        int n = songs.size();
        for (int i=1; i<n; ++i)
        {
            Song key = songs.get(i);
            int j = i-1;
            
            //insertion sort 
            while (j>=0 && songs.get(j).compareTo(key) > 0)
            {
                songs.set(j+1,songs.get(j));
                j = j-1;
            }
            songs.set(j+1,key);
        }
        songList.clear();
		for(int i =0; i < songs.size(); i++) {
			songList.add(songs.get(i).toString());
		}
        
        
	}
	
	public boolean exists(Song song) {
		for(int i =0; i< songs.size(); i++) {
			if(songs.get(i).compareTo(song) == 0) {
				return true;
			}
		}
		return false;
	}
	
	public int findIndex(ArrayList<Song> songs, String name, String artist) {
		for(int i =0; i < songs.size(); i++) {
			if(songs.get(i).getSongName().equals(name)) {
				if(songs.get(i).getArtist().equals(artist)){
					return i;
				}
			}
		}
		return 0;
	}
	
	public void clearTextFields() {
		addSongName.clear();
		addArtist.clear();
		addYear.clear();
		addAlbum.clear();
	}
	
	public void warning(Stage mainStage, String contentText) {
		Alert warning = new Alert(AlertType.WARNING);
		warning.initOwner(mainStage);
		warning.setTitle("Oops!");
		warning.setHeaderText(contentText);
		warning.showAndWait();
	}
	
	public boolean confirmAction(Stage mainStage, String contentText) {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.initOwner(mainStage);
		confirmation.setContentText(contentText);
		
		ButtonType yesButton = new ButtonType("Yes");
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		
		confirmation.getButtonTypes().setAll(yesButton, cancelButton);
		
		Optional<ButtonType> result = confirmation.showAndWait();
		if (result.get() == yesButton)  {
			return true;
		} else {
			return false;
		}
	}

	
	
}
