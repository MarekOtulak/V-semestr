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
import org.mindrot.jbcrypt.BCrypt;
import com.BeatLoop.dao.UserDAO;
import com.BeatLoop.entities.Role;
import com.BeatLoop.entities.User;

@Named
@RequestScoped
public class RegisterBB {
	private String username;
	private String email;
	private String password;
	private String confirmPassword;

	private static final String PAGE_MAIN = "/pages/welcome?faces-redirect=true";
	private static final String PAGE_LOGIN = "/pages/login";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	@Inject
	UserDAO userDAO;
	
	public String getUsername() { 
		return username; 
	}
    public void setUsername(String username) { 
    	this.username = username; 
    }
    public String getEmail() { 
    	return email; 
    }
    public void setEmail(String email) { 
    	this.email = email; 
    }
    public String getPassword() { 
    	return password; 
    }
    public void setPassword(String password) { 
    	this.password = password; 
    }
    public String getConfirmPassword() { 
    	return confirmPassword; 
    }
    public void setConfirmPassword(String confirmPassword) { 
    	this.confirmPassword = confirmPassword; 
    }

	public String register() {
		if (!password.equals(confirmPassword)) {
	    	FacesMessage message = new FacesMessage (FacesMessage.SEVERITY_ERROR, "Passwords do not match", null);
	    	FacesContext.getCurrentInstance().addMessage(null, message);
	    	return null;
	    }
		// Hash the password using BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword); // Save the hashed password
        user.setEmail(email);
        Date now = new java.util.Date();  // Create a java.util.Date
        user.setCreatedAt(new java.sql.Date(now.getTime())); // Convert it to java.sql.Date
        user.setUpdatedAt(new java.sql.Date(now.getTime())); // Convert it to java.sql.Date
		
        Role role = getRoleByName("user");
        if (role != null) {
            user.setRoleRoleId(role); // Set the role for the user
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Role not found", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
		
        try {
            userDAO.create(user); // Persist the user to the database
            FacesMessage message = new FacesMessage("Registration successful");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return PAGE_LOGIN;
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            e.printStackTrace();
            return null;
        }
	}
	
	private Role getRoleByName(String name) {
        Role role = userDAO.findByName(name);
        if (role == null) {
            throw new RuntimeException("Role not found: " + name);
        }
        return role;
    }
}
