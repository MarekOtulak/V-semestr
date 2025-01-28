package com.BeatLoop.dao;

import com.BeatLoop.entities.Song;
import com.BeatLoop.entities.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Stateless
public class SongDAO {
	private final static String UNIT_NAME = "BeatLoop-simplePU";
	@PersistenceContext(unitName = UNIT_NAME)
    private EntityManager em;

    //Zapisuje nową piosenkę w bazie danych.
    public void create(Song song) {
        em.persist(song);
    }

    //Usuwa piosenkę z bazy danych.
    public void delete(Song song) {
        em.remove(em.contains(song) ? song : em.merge(song));
    }

    //Pobiera piosenkę na podstawie ID.
    public Song findById(Integer songId) {
        return em.find(Song.class, songId);
    }

    //Pobiera wszystkie piosenki z bazy danych.
    public List<Song> findAll() {
    	List<Song> list = null;

        Query query = em.createQuery("select s from Song s");

        try {
            list = query.getResultList();
            System.out.println("Ilość piosenek: " + list.size());
            for (Song song : list) {
                System.out.println("Tytuł: " + song.getTitle() + ", Artysta: " + song.getArtist());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

 // Pobiera piosenki na podstawie tytułu.
    public List<Song> findByTitle(String title) {
        return em.createNamedQuery("Song.findByTitle", Song.class)
                .setParameter("title", title)
                .getResultList();
    }

    // Pobiera piosenki na podstawie ścieżki do pliku muzycznego (songUrl).
    public List<Song> findBySongUrl(String songUrl) {
        TypedQuery<Song> query = em.createNamedQuery("Song.findBySongUrl", Song.class);
        query.setParameter("songUrl", songUrl);
        return query.getResultList();
    }
}
