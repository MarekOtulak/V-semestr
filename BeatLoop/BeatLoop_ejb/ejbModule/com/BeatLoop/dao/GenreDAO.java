package com.BeatLoop.dao;

import com.BeatLoop.entities.Genre;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

@Stateless
public class GenreDAO {

	@PersistenceContext(unitName = "BeatLoop-simplePU")
	protected EntityManager em;
	
	@PostConstruct
	public void init() {
	    if (em == null) {
	        System.out.println("EntityManager is NULL!");
	    } else {
	        System.out.println("EntityManager is correctly injected.");
	    }
	}
	
    public List<Genre> findAll() {
        List<Genre> list = null;

        try {
            Query query = em.createQuery("select g from Genre g");
            list = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Genre findById(Integer genreId) {
        return em.find(Genre.class, genreId);
    }

}
