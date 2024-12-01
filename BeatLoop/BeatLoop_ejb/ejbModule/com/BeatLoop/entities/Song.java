/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.BeatLoop.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "song")
@NamedQueries({
    @NamedQuery(name = "Song.findAll", query = "SELECT s FROM Song s"),
    @NamedQuery(name = "Song.findBySongId", query = "SELECT s FROM Song s WHERE s.songPK.songId = :songId"),
    @NamedQuery(name = "Song.findByTitle", query = "SELECT s FROM Song s WHERE s.title = :title"),
    @NamedQuery(name = "Song.findByReleaseYear", query = "SELECT s FROM Song s WHERE s.releaseYear = :releaseYear"),
    @NamedQuery(name = "Song.findByFilePath", query = "SELECT s FROM Song s WHERE s.filePath = :filePath"),
    @NamedQuery(name = "Song.findByIsApproved", query = "SELECT s FROM Song s WHERE s.isApproved = :isApproved"),
    @NamedQuery(name = "Song.findByCreatedAt", query = "SELECT s FROM Song s WHERE s.createdAt = :createdAt"),
    @NamedQuery(name = "Song.findByUpdatedAt", query = "SELECT s FROM Song s WHERE s.updatedAt = :updatedAt"),
    @NamedQuery(name = "Song.findByGenregenreid", query = "SELECT s FROM Song s WHERE s.songPK.genregenreid = :genregenreid")})
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SongPK songPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Column(name = "release_year")
    @Temporal(TemporalType.DATE)
    private Date releaseYear;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "is_approved")
    private Short isApproved;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @ManyToMany(mappedBy = "songCollection")
    private Collection<Album> albumCollection;
    @JoinColumn(name = "Genre_genre_id", referencedColumnName = "genre_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Genre genre;
    /*
    @JoinColumns({
    	@JoinColumn(name = "song_id", referencedColumnName = "song_id", insertable = false, updatable = false),
        @JoinColumn(name = "Genre_genre_id", referencedColumnName = "genre_id", insertable = false, updatable = false)
    })
    @ManyToOne(optional = false)
    private Genre genre;*/

    
    @JoinColumn(name = "updated_by", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User updatedBy;

    public Song() {
    }

    public Song(SongPK songPK) {
        this.songPK = songPK;
    }

    public Song(SongPK songPK, String title, Date releaseYear, String filePath, Date createdAt) {
        this.songPK = songPK;
        this.title = title;
        this.releaseYear = releaseYear;
        this.filePath = filePath;
        this.createdAt = createdAt;
    }

    public Song(int songId, int genregenreid) {
        this.songPK = new SongPK(songId, genregenreid);
    }

    public SongPK getSongPK() {
        return songPK;
    }

    public void setSongPK(SongPK songPK) {
        this.songPK = songPK;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Date releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Short getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Short isApproved) {
        this.isApproved = isApproved;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Collection<Album> getAlbumCollection() {
        return albumCollection;
    }

    public void setAlbumCollection(Collection<Album> albumCollection) {
        this.albumCollection = albumCollection;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (songPK != null ? songPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Song)) {
            return false;
        }
        Song other = (Song) object;
        if ((this.songPK == null && other.songPK != null) || (this.songPK != null && !this.songPK.equals(other.songPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beatloop.entities.Song[ songPK=" + songPK + " ]";
    }
    
}
