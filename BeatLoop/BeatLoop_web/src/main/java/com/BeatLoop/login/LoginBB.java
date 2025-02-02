package com.BeatLoop.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.simplesecurity.RemoteClient;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import com.BeatLoop.dao.UserDAO;
import com.BeatLoop.entities.Role;
import com.BeatLoop.entities.User;


@Named
@RequestScoped
public class LoginBB {
	private static final String PAGE_MAIN = "/pages/main-template-logged?faces-redirect=true";
	private static final String PAGE_LOGIN = "/pages/login";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String login;
	private String pass;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@Inject
	UserDAO userDAO;

	public String doLogin() {
	    FacesContext ctx = FacesContext.getCurrentInstance();
	    
	    // Usuń komunikat rejestracyjny z sesji (jeśli istnieje)
	    ctx.getExternalContext().getSessionMap().remove("registrationMessage");

	 // 1. Pobierz użytkownika z bazy danych na podstawie loginu
	    User user = userDAO.getUserFromDatabase(login);

        // 2. Jeśli użytkownik nie istnieje lub hasło jest niepoprawne
        if (user == null || !BCrypt.checkpw(pass, user.getPassword())) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Niepoprawny login lub hasło", null));
            return PAGE_STAY_AT_THE_SAME;
        }

	    
	    System.out.println("Zalogowano użytkownika: " + user.getUsername());  // Logowanie udane
	    
	    // 3. Jeśli użytkownik zalogowany: pobranie ról użytkownika, zapisanie ich w RemoteClient i przechowanie w sesji
	    RemoteClient<User> client = new RemoteClient<>(); // Utworzenie nowego RemoteClient
	    client.setDetails(user);

	    List<String> roles = new ArrayList<>();
	    List<Role> userRoles = userDAO.getUserRolesFromDatabase(user);

	    if (userRoles != null) {
	        for (Role role : userRoles) {
	            roles.add(role.getRoleName()); // Dodaj nazwę roli do listy
	        }
	    }

	    // Zapisanie ról w RemoteClient
	    client.getRoles().addAll(roles);

	    // Przechowanie RemoteClient z informacjami o sesji
	    HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
	    client.store(request);

	    // Logowanie sesji, aby upewnić się, że sesja została poprawnie ustawiona
	    HttpSession session = request.getSession();
	    System.out.println("Sesja: " + session.getId());  // Powinno pokazać nową sesję
	   
	    return PAGE_MAIN;

	}

	
	public String doLogout(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		session.invalidate();
		return PAGE_LOGIN;
	}
	
}
