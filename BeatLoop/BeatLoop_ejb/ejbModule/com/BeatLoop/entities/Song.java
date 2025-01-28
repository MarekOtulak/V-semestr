/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.BeatLoop.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author Marek
 */
@Entity
@Table(name = "song")
@NamedQueries({
    @NamedQuery(name = "Song.findAll", query = "SELECT s FROM Song s"),
    @NamedQuery(name = "Song.findBySongId", query = "SELECT s FROM Song s WHERE s.songId = :songId"),
    @NamedQuery(name = "Song.findByTitle", query = "SELECT s FROM Song s WHERE s.title = :title"),
    @NamedQuery(name = "Song.findByImgUrl", query = "SELECT s FROM Song s WHERE s.imgUrl = :imgUrl"),
    @NamedQuery(name = "Song.findBySongUrl", query = "SELECT s FROM Song s WHERE s.songUrl = :songUrl"),
    @NamedQuery(name = "Song.findByArtist", query = "SELECT s FROM Song s WHERE s.artist = :artist")})
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "song_id")
    private Integer songId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @Size(max = 512)
    @Column(name = "img_url")
    private String imgUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "song_url")
    private String songUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "artist")
    private String artist;
    @JoinColumn(name = "genre_genre_id", referencedColumnName = "genre_id")
    @ManyToOne(optional = false)
    private Genre genreGenreId;

    public Song() {
    }

    public Song(Integer songId) {
        this.songId = songId;
    }

    public Song(Integer songId, String title, String songUrl, String artist) {
        this.songId = songId;
        this.title = title;
        this.songUrl = songUrl;
        this.artist = artist;
    }

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Genre getGenreGenreId() {
        return genreGenreId;
    }

    public void setGenreGenreId(Genre genreGenreId) {
        this.genreGenreId = genreGenreId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (songId != null ? songId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Song)) {
            return false;
        }
        Song other = (Song) object;
        if ((this.songId == null && other.songId != null) || (this.songId != null && !this.songId.equals(other.songId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.BeatLoop.entities.Song[ songId=" + songId + " ]";
    }
    
}
