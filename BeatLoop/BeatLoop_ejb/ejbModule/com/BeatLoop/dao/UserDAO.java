package com.BeatLoop.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import com.BeatLoop.entities.Role;
import com.BeatLoop.entities.User;
import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortOrder;

//DAO - Data Access Object for Person entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.

@Stateless
public class UserDAO {
	private final static String UNIT_NAME = "BeatLoop-simplePU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(User user) {
		em.persist(user);
	}

	public User merge(User user) {
		return em.merge(user);
	}
	@Transactional
	public void remove(User user) {
		if (user != null) {
	        System.out.println("Usuwam użytkownika: " + user.getUsername());
	        em.remove(em.merge(user));
	        em.flush(); // Wymuszenie zapisu
	    } else {
	        System.out.println("Użytkownik do usunięcia jest null");
	    }
	}

	public User find(Object id) {
		return em.find(User.class, id);
	}

	public List<User> getFullList() {
		List<User> list = null;

		Query query = em.createQuery("select u from User u");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<User> getList(Map<String, Object> searchParams) { //pobieranie danych z tabeli user do wyświetlenia w personList.xhtml
		List<User> list = null;

		// 1. Build query string with parameters
		String select = "select u ";
		String from = "from User u ";
		String where = "";
		String orderby = "order by u.username asc, u.email";

		// search for username
		String username = (String) searchParams.get("username");
		if (username != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "u.username like :username ";
		}
		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (username != null) {
			query.setParameter("username", username+"%");
		}
		// 4. Execute query and retrieve list of Person objects
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public User getUserFromDatabase(String login) {
	    try {
	        return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
	                 .setParameter("username", login)
	                 .getSingleResult();
	    } catch (NoResultException e) {
	        // Logowanie błędu, gdy brak wyniku
	        System.out.println("Brak użytkownika z podanym loginem lub hasłem");
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
    public User getUserByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                     .setParameter("username", username)
                     .getSingleResult();
        } catch (NoResultException e) {
        	System.out.println("Użytkownik o nazwie " + username + " nie znaleziony.");
            return null;
        }
    }
	
	public List<Role> getUserRolesFromDatabase(User user) {
	    List<Role> roles = new ArrayList<>();
	    if (user.getRoleRoleId() != null) {
	        roles.add(user.getRoleRoleId()); // Dodaj obiekt Role do listy
	    }
	    return roles; // Zwróć listę obiektów Role
	}
	
	public Role getRoleForUser(User user) {
	    // Zakładamy, że user.getRoleRoleId() przechowuje ID przypisanej roli
	    if (user.getRoleRoleId() != null) {
	        return em.find(Role.class, user.getRoleRoleId()); // Pobierz rolę na podstawie ID
	    }
	    return null; // Jeśli brak przypisanej roli
	}
/*
	public List<Object[]> getUsersWithRoles() {
	    return em.createQuery("SELECT u.username, u.roleRoleId.roleName FROM User u", Object[].class).getResultList();
	}*/
	public List<User> getUsersWithRoles() {
	    return em.createQuery("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roleRoleId", User.class).getResultList();
	}
    
    public List<Role> getAllRoles() {
        try {
            return em.createQuery("SELECT r FROM Role r", Role.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public Role findByName(String roleName) {
        try {
            return em.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class)
                     .setParameter("roleName", roleName)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null; // Return null if no role is found
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle any other exceptions
        }
    }
    
    public List<User> getUsersWithPagination(int offset, int pageSize) { //W getUsersWithPagination() zapytanie JPA używa setFirstResult(offset) i setMaxResults(pageSize), co pozwala na pobieranie tylko tych użytkowników, którzy są potrzebni dla konkretnej strony tabeli. Paginacja działa na poziomie bazy danych - dane nie są ładowane w całości, tylko odpowiednia ich część
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public int countUsers() {
        return ((Long) em.createQuery("SELECT COUNT(u) FROM User u").getSingleResult()).intValue();
    }
    
    public List<User> getUsersWithPaginationAndFilter(int offset, int pageSize, String usernameFilter) {
        String query = "SELECT u FROM User u WHERE (:username IS NULL OR u.username LIKE :username)";
        TypedQuery<User> typedQuery = em.createQuery(query, User.class);

        if (usernameFilter != null && !usernameFilter.isEmpty()) {
            typedQuery.setParameter("username", "%" + usernameFilter + "%");
        } else {
            typedQuery.setParameter("username", null);
        }

        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    public int countUsersWithFilter(String usernameFilter) {
        String query = "SELECT COUNT(u) FROM User u WHERE (:username IS NULL OR u.username LIKE :username)";
        TypedQuery<Long> typedQuery = em.createQuery(query, Long.class);
        typedQuery.setParameter("username", usernameFilter != null ? "%" + usernameFilter + "%" : null);
        return typedQuery.getSingleResult().intValue();
    }


}
