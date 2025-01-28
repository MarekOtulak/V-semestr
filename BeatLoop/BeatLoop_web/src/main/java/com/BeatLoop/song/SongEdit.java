package com.BeatLoop.song;

import com.BeatLoop.dao.GenreDAO;
import com.BeatLoop.dao.SongDAO;
import com.BeatLoop.entities.Genre;
import com.BeatLoop.entities.Song;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class SongEdit {
    private Integer songId;
    private String title;
    private String artist;
    private String songUrl;
    private Integer genreId;

    @Inject
    private SongDAO songDAO;

    @Inject
    private GenreDAO genreDAO;

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

    public List<Genre> getAvailableGenres() {
        return genreDAO.findAll();
    }

    public String loadSongDetails() {
        if (songId == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Song ID is required", null));
            return null;
        }

        Song song = songDAO.find(songId);
        if (song == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Song not found", null));
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Song ID is required", null));
            return null;
        }

        Song song = songDAO.find(songId);
        if (song == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Song not found", null));
            return null;
        }

        Genre genre = genreDAO.find(genreId);
        if (genre == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid genre selected", null));
            return null;
        }

        song.setTitle(title);
        song.setArtist(artist);
        song.setSongUrl(songUrl);
        song.setGenreGenreId(genre);

        songDAO.update(song);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Song updated successfully", null));
        return "songList?faces-redirect=true";
    }
}
