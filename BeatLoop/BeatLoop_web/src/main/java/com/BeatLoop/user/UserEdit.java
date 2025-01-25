package com.BeatLoop.user;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.BeatLoop.entities.Role;
import com.BeatLoop.entities.User;
import com.BeatLoop.dao.UserDAO;

@Named
@ViewScoped
public class UserEdit implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_PERSON_LIST = "personList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private User user;
	private List<Role> availableRoles; // Lista dostępnych ról
    private Role selectedRole;  // wybrana rola
    
    @PersistenceContext
    protected EntityManager em;
    
	@EJB
	UserDAO userDAO;

	@Inject
	FacesContext context;

	@Inject
	Flash flash;
	
	public List<Role> getAvailableRoles() {
        if (availableRoles == null) {
            availableRoles = userDAO.getAllRoles(); // Pobierz wszystkie dostępne role
        }
        return availableRoles;
    }

    public Role getSelectedRoles() {
        return selectedRole;
    }

    public void setSelectedRoles(Role selectedRole) {
        this.selectedRole = selectedRole;
    }

    public void onLoad() throws IOException {
        user = (User) flash.get("user");
        if (user != null) {
            // Pobierz rolę użytkownika, jeśli jest przypisana
            selectedRole = userDAO.getRoleForUser(user); // Zakładamy, że masz metodę zwracającą jedną rolę
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
            FacesContext.getCurrentInstance().responseComplete();
            return; // Zatrzymanie dalszej egzekucji
        }
    }

    public String saveRoles() {
        if (user == null) {
            return PAGE_STAY_AT_THE_SAME;
        }

        try {
            // Sprawdzenie, czy rola została wybrana
            if (selectedRole != null) {
                // Przypisanie całego obiektu roli do użytkownika
                user.setRoleRoleId(selectedRole); // Przypisanie obiektu Role do użytkownika
                em.merge(user); // Zapisanie użytkownika w bazie danych
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nie wybrano roli", null));
                FacesContext.getCurrentInstance().responseComplete();
                return PAGE_STAY_AT_THE_SAME;
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wystąpił błąd podczas zapisu roli", null));
            FacesContext.getCurrentInstance().responseComplete();
            return PAGE_STAY_AT_THE_SAME;
        }
        FacesContext.getCurrentInstance().responseComplete();
        return PAGE_PERSON_LIST;
    }

}
