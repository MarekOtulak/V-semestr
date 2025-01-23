package com.BeatLoop.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import com.BeatLoop.entities.Role;
import com.BeatLoop.entities.User;
import com.BeatLoop.entities.Userrole;

//DAO - Data Access Object for Person entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.

@Stateless
public class UserDAO {
	private final static String UNIT_NAME = "BeatLoop-simplePU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(User user) {
		em.persist(user);
	}

	public User merge(User user) {
		return em.merge(user);
	}

	public void remove(User user) {
		em.remove(em.merge(user));
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
	///////////////////////////////////////////////////////
	public User getUserFromDatabase(String login, String pass) {
	    try {
	        // Tworzenie zapytania do bazy danych, które wyszukuje użytkownika po loginie
	        TypedQuery<User> query = em.createQuery(
	            "SELECT u FROM User u WHERE u.username = :username", User.class);
	        query.setParameter("username", login);

	        User user = query.getSingleResult();
	        
	        // Sprawdzenie, czy hasło użytkownika odpowiada temu podanemu
	        if (user != null && user.getPassword().equals(pass)) { // Hasło w bazie jest jawne, zaleca się użycie haszowania
	            return user;
	        }
	    } catch (Exception e) {
	        // Obsługuje wyjątek, jeśli użytkownik nie zostanie znaleziony
	        return null;
	    }
	    return null;
	}

	public List<String> getUserRolesFromDatabase(User user) {
	    List<String> roles = new ArrayList<>();
	    try {
	        // Tworzenie zapytania do bazy danych w celu pobrania ról użytkownika za pomocą tabeli pośredniczącej
	        TypedQuery<Userrole> query = em.createQuery(
	            "SELECT ur FROM Userrole ur WHERE ur.useruserid = :user", Userrole.class);
	        query.setParameter("user", user);

	        List<Userrole> userRoles = query.getResultList();

	        // Przekształcenie obiektów Userrole do listy nazw ról
	        for (Userrole userRole : userRoles) {
	            Role role = userRole.getRoleroleid();
	            if (role != null) {
	                roles.add(role.getRoleName());
	            }
	        }

	    } catch (Exception e) {
	        // Obsługa błędów podczas wykonywania zapytania
	        e.printStackTrace();
	    }

	    return roles;
	}
/////////////
	public void assignRoleToUser(Userrole userRole) {
	    em.persist(userRole); // Zapisz encję Userrole do bazy
	}

	public List<Role> getAllRoles() {
	    return em.createNamedQuery("Role.findAll", Role.class).getResultList();
	}

	public List<Role> getRolesForUser(User user) {
	    // Wyszukaj role przypisane do użytkownika
	    return em.createQuery("SELECT r FROM Role r JOIN r.userroleCollection ur WHERE ur.useruserid = :user", Role.class)
	             .setParameter("user", user)
	             .getResultList();
	}


/////////////////////////////////////////////////////
	public List<User> getList(Map<String, Object> searchParams) {
		List<User> list = null;

		// 1. Build query string with parameters
		String select = "select u, r.roleName "; // Zmieniamy SELECT, by wziąć również rolę
		String from = "from User u ";
		String join = "join u.userroleCollection ur "  // Łączenie z Userrole
		             + "join ur.roleroleid r ";        // Łączenie z Role
		String where = "";  // Tutaj możesz dodać dodatkowe warunki, jeśli chcesz
		String orderby = "order by u.username asc, u.email";

		// search for surname
		String username = (String) searchParams.get("username");
		if (username != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "u.username like :username ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		//Query query = em.createQuery(select + from + where + orderby);
		Query query = em.createQuery(select + from + join + where + orderby);

		// 3. Set configured parameters
		if (username != null) {
			query.setParameter("username", username+"%");
		}

		// ... other parameters ... 

		// 4. Execute query and retrieve list of Person objects
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
