package com.BeatLoop.song;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.BeatLoop.dao.GenreDAO;
import com.BeatLoop.dao.SongDAO;
import com.BeatLoop.entities.Genre;
import com.BeatLoop.entities.Song;

@Named
@RequestScoped
public class SongService {
	
	private static final String PAGE_SONGDASHBOARD = "/pages/moderator/dashboard-songs?faces-redirect=true";

    @Inject
    private SongDAO songDAO;
    
    @Inject
    private GenreDAO genreDAO;
    
    private List<Song> allSongs;
    // Pobiera wszystkie piosenki z encji Song
    public List<Song> getAllSongs() {
        if (allSongs == null) {
            allSongs = songDAO.getAllSongsWithGenre();
        }
        return allSongs;
    }
    
    public List<Genre> getAllGenres() {
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

    public String addSong() {
        if (newSong != null && newSong.getTitle() != null && !newSong.getTitle().isEmpty()
                && newSong.getArtist() != null && !newSong.getArtist().isEmpty()
                && newSong.getSongUrl() != null && !newSong.getSongUrl().isEmpty()) {

            if (newSong.getGenreGenreId() != null) {
                System.out.println("Dodano utwór z gatunkiem: " + newSong.getGenreGenreId().getName());
                songDAO.create(newSong);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Piosenka została dodana!", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Proszę wybrać gatunek", null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wszystkie pola są wymagane", null));
        }
        return PAGE_SONGDASHBOARD;
    }

    // Usuwa piosenkę z bazy danych. param songId ID piosenki do usunięcia.
    public void deleteSong(Integer songId) {
        Song song = songDAO.findById(songId);
        if (song != null) {
            songDAO.delete(song);
        }
    }

    //Wyszukuje piosenki na podstawie tytułu. @param title Tytuł piosenki.  @return lista piosenek z danym tytułem.
    public List<Song> findSongsByTitle(String title) {
        return songDAO.findByTitle(title);
    }

}
