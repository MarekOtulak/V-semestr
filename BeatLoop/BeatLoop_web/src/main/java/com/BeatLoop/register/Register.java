/*package com.BeatLoop.register;

import java.sql.Date;
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
import com.jsfcourse.dao.RoleDAO;

import com.BeatLoop.dao.UserDAO;
import com.BeatLoop.entities.Role;
import com.BeatLoop.entities.User;


@Named
@RequestScoped
public class Register {
	private String username;
	private String email;
	private String password;
	private String confirmPassword;

	private static final String PAGE_MAIN = "/pages/main-template-logged?faces-redirect=true";
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
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		
		User user = new User();

		user.setUsername(username);
		//user.setPassword(hashedPassword);
		user.setEmail(email);
		Date now = new Date();
		user.setCreatedAt(now);
		user.setUpdatedAt(now);
		
		Role role = getRoleByName("user");
		if (role != null) {
			role.setUserRoleId(role);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Role not found", null);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		
		try {
			userDAO.create(user);
			FacesMessage message = new FacesMessage("Registration succesfull");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return PAGE_LOGIN;
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed", null);
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			return null;
		}	
	    return PAGE_LOGIN;
	}
	
	private Role getRoleByName(String name) {
        Role role = userDAO.findByName(name);
        if (role == null) {
            throw new RuntimeException("Role not found: " + name);
        }
        return role;
	}
}
*/