/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.BeatLoop.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author Marek
 */
@Embeddable
public class SongPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "song_id")
    private int songId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Genre_genre_id")
    private int genregenreid;

    public SongPK() {
    }

    public SongPK(int songId, int genregenreid) {
        this.songId = songId;
        this.genregenreid = genregenreid;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getGenregenreid() {
        return genregenreid;
    }

    public void setGenregenreid(int genregenreid) {
        this.genregenreid = genregenreid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) songId;
        hash += (int) genregenreid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SongPK)) {
            return false;
        }
        SongPK other = (SongPK) object;
        if (this.songId != other.songId) {
            return false;
        }
        if (this.genregenreid != other.genregenreid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beatloop.entities.SongPK[ songId=" + songId + ", genregenreid=" + genregenreid + " ]";
    }
    
}
