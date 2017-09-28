package view;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import application.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
	private TextField addText;
	

	
	private ObservableList<String> songList;
	
	public void start(Stage mainStage) throws IOException, FileNotFoundException, ClassNotFoundException {
		
		//if the file is empty the inputstream will crash so adding an empty arraylist if file is empty
		BufferedReader br = new BufferedReader(new FileReader("songlist"));     
		if (br.readLine() == null) {
		    writeToFile(new ArrayList<Song>());
		}
		
		// if the file is empty, create a new list, otherwise read it from the file
		songList = FXCollections.observableArrayList();
		
		//since observableList isn't serializable we need to keep this list up to date for the file
		songs = readFromFile();
		
		for(int i =0; i < songs.size(); i++) {
			songList.add(songs.get(i).toString());
		}
		
		listView.setItems(songList);
		
		// selects the first song upon starting the app
		listView.getSelectionModel().select(0);
		
		// eventually make separate methods for event handling
		
		//each time we update/add/delete we sort the arraylist,update the observable list, and write the new list of songs to the file
		
		addButton.setOnAction((event) -> {
			//TODO check if song exists
			if (!addText.getText().isEmpty() && !addArtist.getText().isEmpty()) {
				songs.add(new Song(addText.getText(), addArtist.getText()));
				sortAndUpdate(songs);
				try {
					writeToFile(songs);
				} catch (IOException e) {
					//change this
					e.printStackTrace();
				}
				listView.setItems(songList);
			}
			
			// write list to file here
		});
		
		editButton.setOnAction((event) -> {
			
			int index = listView.getSelectionModel().getSelectedIndex();
			
			if (!addText.getText().isEmpty() && index >= 0 && !addArtist.getText().isEmpty()) {
				
				songs.set(index,new Song(addText.getText(), addArtist.getText()));
				sortAndUpdate(songs);
				try {
					writeToFile(songs);
				} catch (IOException e) {
					//change this
					e.printStackTrace();
				}
				listView.setItems(songList);
			}
			
			// write list to file here
		});
		
		deleteButton.setOnAction((event) -> {
			
			int index = listView.getSelectionModel().getSelectedIndex();
			
			if (!songList.isEmpty() && index >= 0) {
				songs.remove(index);
				sortAndUpdate(songs);
				try {
					writeToFile(songs);
				} catch (IOException e) {
					//change this
					e.printStackTrace();
				}
				listView.setItems(songList);
			}
			
			// write list to file here
		});
		
	}
	public void writeToFile(ArrayList<Song> songList) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("songlist"));
		objectOutputStream.writeObject(songList);
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

	
	
}
