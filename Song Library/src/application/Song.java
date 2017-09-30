/* 
 *  Matt Skrobola and Kyle Reagle 
 */

package application;

import java.io.Serializable;

public class Song implements Serializable {
	String songName;
	String artist;
	String year;
	String album;
	
	public Song(String songName,String artist, String album, String year) {
		this.artist = artist;
		this.songName = songName;
		this.year = year;
		this.album = album;
	}
	public Song(String songName, String artist) {
		this(songName,artist,null,null);
	}
	public String getAlbum() {
		return album;
	}
	public String getArtist() {
		return artist;
	}
	public String getYear(){
		return year;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public void setYear(String year){
		this.year = year;
	}
	public String getSongName() {
		return songName;
	}
	
	@Override
	public String toString() {
		return songName + " : " + artist;
	}
	
	public int compareTo(Song song) {
		if(song == null)return 1;
		int i = this.songName.toLowerCase().compareTo(song.songName.toLowerCase());
		
		if(i == 0) { // if the song names are the same compare the artist names
			i = this.artist.toLowerCase().compareTo(song.artist.toLowerCase());
		}
		return i;
	}
}
