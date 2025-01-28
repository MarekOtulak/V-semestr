package com.BeatLoop.song;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import com.BeatLoop.dao.GenreDAO;
import com.BeatLoop.dao.SongDAO;
import com.BeatLoop.entities.Genre;
import com.BeatLoop.entities.Song;

@Named
@RequestScoped
public class SongService {
    private static final String PAGE_SONGDASHBOARD = "/pages/moderator/dashboard-songs?faces-redirect=true";

    @EJB
    private SongDAO songDAO;

    @EJB
    private GenreDAO genreDAO;

    private Integer songId = 0;
    private String title;
    private String artist;
    private String songUrl;
    private Integer genreId;

    private Song newSong = new Song();

    // Getters and setters for new song fields
    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public Song getNewSong() {
        return newSong;
    }

    public void setNewSong(Song newSong) {
        this.newSong = newSong;
    }

    public List<Song> getList() { //potrzebne
        List<Song> list = null;
	Map<String,Object> searchParams = new HashMap<String, Object>();

	list = songDAO.getList(searchParams);
	return list;
    }

    public List<Genre> getAllGenres() { //potrzebne
        List<Genre> genres = genreDAO.findAll();
        if (genres == null || genres.isEmpty()) {
            throw new IllegalStateException("Nie znaleziono gatunków w bazie danych.");
        }
        //genres.forEach(genre -> System.out.println("Gatunek: " + genre.getName())); //debugowanie
        return genres;
    }

    public String addSong() { //potrzebne
        if (newSong != null && newSong.getTitle() != null && !newSong.getTitle().isEmpty()
                && newSong.getArtist() != null && !newSong.getArtist().isEmpty()
                && newSong.getSongUrl() != null && !newSong.getSongUrl().isEmpty()) {

            if (songId != null) {
                newSong.setGenreGenreId(genreDAO.findById(songId));
                songDAO.create(newSong);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Piosenka została dodana!", null));
                System.out.println("Dodano utwór z gatunkiem: " + newSong.getGenreGenreId().getName());
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Proszę wybrać gatunek", null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wszystkie pola są wymagane", null));
        }
        return PAGE_SONGDASHBOARD;
    }

    @Transactional
    public void deleteSongByName(String title) { //potrzebne
    	System.out.println("Metoda deleteSongByName wywołana dla tytułu: " + title);
        if (title == null || title.isEmpty()) {
        	System.out.println("Podano pustą nazwę piosenki!");
        	return;
        }
	System.out.println("Próba usunięcia piosenki: " + title);
	Song song = songDAO.getSongByTitle(title);
	if (song != null) {
		try {
	        	System.out.println("Usuwanie piosenki: " + song.getTitle());
	        	songDAO.delete(song);
	        	System.out.println("Piosenka usunięta: " + title);
	        } catch (Exception e) {
	        	System.out.println("Błąd podczas usuwania piosenki: " + e.getMessage());
	        	e.printStackTrace();
	        }
       } else {
       	System.out.println("Piosenka nie znaleziona: " + title);
       }
    }
    
    public String prepareEdit(Integer songId) { //Sprawdza, czy songId jest ustawione. Jeśli nie, dodaje wiadomość o błędzie. Jeśli songId jest poprawne, wykonuje przekierowanie do strony songEdit.xhtml, dodając songId jako parametr w URL.
        if (songId == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Song ID jest wymagane", null));
            return null;
        }
        System.out.println("Preparing to edit song with ID: " + songId);
        this.songId = songId;
        return "songEdit.xhtml?faces-redirect=true&songId=" + songId;
    }

    public String loadSongDetails() { //metoda sprawdza, czy songId jest poprawne (czy nie jest puste i czy odnaleziono utwór w bazie danych). Jeśli wystąpi problem, dodaje wiadomość o błędzie, Jeśli wszystko przebiegnie poprawnie, dane utworu są ładowane do pól w komponencie (title, artist, songUrl, genreId), dzięki czemu można je później wyświetlić lub edytować w widoku 
        if (songId == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Song ID jest wymagane", null));
            return null;
        }

        Song song = songDAO.findById(songId);
        if (song == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Piosenka nie znaleziona", null));
            return null;
        }

        this.title = song.getTitle();
        this.artist = song.getArtist();
        this.songUrl = song.getSongUrl();
        this.genreId = song.getGenreGenreId().getGenreId();

        return null;
    }

    public String saveSongDetails() {
        if (songId == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Song ID is required", null));
            return null;
        }

        Song song = songDAO.findById(songId);
        if (song == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Song not found", null));
            return null;
        }

        Genre genre = genreDAO.findById(genreId);
        if (genre == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid genre selected", null));
            return null;
        }

        song.setTitle(title);
        song.setArtist(artist);
        song.setSongUrl(songUrl);
        song.setGenreGenreId(genre);

        songDAO.update(song);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Song updated successfully", null));
        return PAGE_SONGDASHBOARD;
    }
}
