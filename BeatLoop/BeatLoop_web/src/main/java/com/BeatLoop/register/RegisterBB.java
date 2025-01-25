package com.BeatLoop.register;

import java.util.Date;
import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.simplesecurity.RemoteClient;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

//import org.mindrot.jbcrypt.BCrypt;
import com.BeatLoop.entities.Role;
import com.BeatLoop.entities.User;
import com.BeatLoop.dao.UserDAO;

@Named
@RequestScoped
public class RegisterBB {
	private String username;
	private String email;
	private String password;
	private String confirmPassword;

	private static final String PAGE_MAIN = "/pages/welcome?faces-redirect=true";
	private static final String PAGE_LOGIN = "/pages/login?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	@Inject
	UserDAO userDAO;
	
	@Valid
    private User user = new User(); // Walidacja na poziomie obiektu User
	
	public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
	public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
    }

    public String getPassword() {
        return user.getPassword();
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String register() {
    	if (!this.getPassword().equals(this.getConfirmPassword())) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hasła nie są zgodne", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return PAGE_STAY_AT_THE_SAME;
        }
        
        // Set up user attributes (just in case they aren't set from the form)
        Date now = new java.util.Date();
        user.setCreatedAt(new java.sql.Date(now.getTime()));
        user.setUpdatedAt(new java.sql.Date(now.getTime()));
        
        // Handle user role and saving
        Role userRole = getRoleByName("user");
        user.setRoleRoleId(userRole);

        try {
            userDAO.create(user); // Save user to database
         // Save the message to the session map
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("registrationMessage", "Registration successful");
            return PAGE_LOGIN; // Redirect to login page
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed: " + e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return PAGE_STAY_AT_THE_SAME;
        }
    }

    private Role getRoleByName(String roleName) {
        return userDAO.findByName(roleName); // Assuming this method returns the role from DB
    }
}
