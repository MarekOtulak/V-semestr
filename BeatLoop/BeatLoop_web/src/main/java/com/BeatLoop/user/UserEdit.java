package com.BeatLoop.user;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

import com.BeatLoop.entities.Role;
import com.BeatLoop.entities.User;
import com.BeatLoop.entities.Userrole;
import com.BeatLoop.dao.UserDAO;

@Named
@ViewScoped
public class UserEdit implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_PERSON_LIST = "personList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private User user = new User();
	private User loaded = null;
	
	/////
	private List<Role> availableRoles; // Lista dostępnych ról
    private List<Role> selectedRoles;  // Lista wybranych ról przez użytkownika
    /////
    
    
	@EJB
	UserDAO userDAO;

	@Inject
	FacesContext context;

	@Inject
	Flash flash;

	public User getUser() {
		return user;
	}
	
	public List<Role> getAvailableRoles() {
        if (availableRoles == null) {
            availableRoles = userDAO.getAllRoles(); // Pobierz wszystkie dostępne role
        }
        return availableRoles;
    }

    public List<Role> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<Role> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

	public void onLoad() throws IOException {
		// 1. load person passed through session
		// HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		// loaded = (Person) session.getAttribute("person");

		// 2. load person passed through flash
		loaded = (User) flash.get("user");

		// cleaning: attribute received => delete it from session
		if (loaded != null) {
			user = loaded;
			// session.removeAttribute("person");
			
			//// Jeśli użytkownik ma już przypisane role, wczytaj je
			selectedRoles = userDAO.getRolesForUser(user); // Pobierz przypisane role
			////
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}
	}

	public String saveData() {
		
		// no Person object passed
		if (loaded == null) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try { // Zapisz dane użytkownika
			if (user.getUserId() == null) {
				// Nowy użytkownik - ustawiamy createdAt i updatedAt
	            user.setCreatedAt(new Date());  // Ustawienie daty utworzenia
	            user.setUpdatedAt(new Date());  // Ustawienie daty ostatniej aktualizacji
				userDAO.create(user);
			} else {
				// Istniejący użytkownik - tylko aktualizujemy updatedAt
	            user.setUpdatedAt(new Date());  // Aktualizacja daty ostatniej zmiany
				userDAO.merge(user);
			}
			///////////////
			// Zapisz role użytkownika
            if (selectedRoles != null && !selectedRoles.isEmpty()) {
                for (Role role : selectedRoles) {
                    Userrole userRole = new Userrole();
                    userRole.setUseruserid(user); // Ustaw użytkownika
                    userRole.setRoleroleid(role); // Ustaw rolę
                    userRole.setAssignedAt(new Date()); // Data przypisania roli
                    userDAO.assignRoleToUser(userRole); // Przypisz rolę użytkownikowi
                }
            }
            ////////////
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_PERSON_LIST;
	}
}
