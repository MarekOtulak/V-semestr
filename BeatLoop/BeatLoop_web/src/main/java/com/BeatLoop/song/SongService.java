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
    
    private Integer songid = 0;

    public List<Song> getList(){ //potrzebne
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
    
    private Song newSong = new Song();

    public Song getNewSong() {
        return newSong;
    }

    public void setNewSong(Song newSong) {
        this.newSong = newSong;
    }
    
    public int getSongid() {
		return songid;
	}

	public void setSongid(int songid) {
		this.songid = songid;
	}

    public String addSong() { //potrzebne
        if (newSong != null && newSong.getTitle() != null && !newSong.getTitle().isEmpty()
                && newSong.getArtist() != null && !newSong.getArtist().isEmpty()
                && newSong.getSongUrl() != null && !newSong.getSongUrl().isEmpty()) {

            if (songid != null) {
                newSong.setGenreGenreId(genreDAO.findById(songid));
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
    
    @Transactional
	public void editSongBySongId(String songid) { //potrzebne

	}
}
