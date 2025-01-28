package com.BeatLoop.dao;

import com.BeatLoop.entities.Song;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Query;

@Stateless
public class SongDAO {
	private final static String UNIT_NAME = "BeatLoop-simplePU";

	@PersistenceContext(unitName = UNIT_NAME)
    private EntityManager em;

    public void create(Song song) {  //potrzebne
        em.persist(song);
    }
    
    public void delete(Song song) {  //potrzebne
        em.remove(em.contains(song) ? song : em.merge(song));
    }

    public List<Song> getList(Map<String, Object> searchParams) { //pobieranie danych z tabeli song do wyświetlenia //potrzebne
		List<Song> list = null;

		String select = "select s ";
		String from = "from Song s ";
		String where = "";
		String orderby = "order by s.title asc";

		//wyszukiwanie piosenek
		String title = (String) searchParams.get("title");
		if (title != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "s.title like :title ";
		}
		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (title != null) {
			query.setParameter("title", title+"%");
		}
		// 4. Execute query and retrieve list of Person objects
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
    
    public Song getSongByTitle(String title) { //potrzebne
        try {
            return em.createQuery("SELECT s FROM Song s WHERE s.title = :title", Song.class)
                     .setParameter("title", title)
                     .getSingleResult();
        } catch (NoResultException e) {
        	System.out.println("Piosenka o nazwie " + title + " nie znaleziona.");
            return null;
        }
    }
}
