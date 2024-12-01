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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Marek
 */
@Entity
@Table(name = "album")
@NamedQueries({
    @NamedQuery(name = "Album.findAll", query = "SELECT a FROM Album a"),
    @NamedQuery(name = "Album.findByAlbumId", query = "SELECT a FROM Album a WHERE a.albumId = :albumId"),
    @NamedQuery(name = "Album.findByAlbumName", query = "SELECT a FROM Album a WHERE a.albumName = :albumName"),
    @NamedQuery(name = "Album.findByAlbumReleaseYear", query = "SELECT a FROM Album a WHERE a.albumReleaseYear = :albumReleaseYear")})
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "album_id")
    private Integer albumId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "album_name")
    private String albumName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "album_release_year")
    @Temporal(TemporalType.DATE)
    private Date albumReleaseYear;
    @JoinTable(name = "album_has_song", joinColumns = {
        @JoinColumn(name = "Album_album_id", referencedColumnName = "album_id")}, inverseJoinColumns = {
        @JoinColumn(name = "Song_song_id", referencedColumnName = "song_id")})
    @ManyToMany
    private Collection<Song> songCollection;

    public Album() {
    }

    public Album(Integer albumId) {
        this.albumId = albumId;
    }

    public Album(Integer albumId, String albumName, Date albumReleaseYear) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.albumReleaseYear = albumReleaseYear;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Date getAlbumReleaseYear() {
        return albumReleaseYear;
    }

    public void setAlbumReleaseYear(Date albumReleaseYear) {
        this.albumReleaseYear = albumReleaseYear;
    }

    public Collection<Song> getSongCollection() {
        return songCollection;
    }

    public void setSongCollection(Collection<Song> songCollection) {
        this.songCollection = songCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (albumId != null ? albumId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Album)) {
            return false;
        }
        Album other = (Album) object;
        if ((this.albumId == null && other.albumId != null) || (this.albumId != null && !this.albumId.equals(other.albumId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beatloop.entities.Album[ albumId=" + albumId + " ]";
    }
    
}
