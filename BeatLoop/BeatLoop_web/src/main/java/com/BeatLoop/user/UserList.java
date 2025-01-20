package com.BeatLoop.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.servlet.http.HttpSession;
import com.BeatLoop.entities.User;

import com.BeatLoop.dao.UserDAO;

@Named
@RequestScoped
public class UserList {
	private static final String PAGE_PERSON_EDIT = "personEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String username;
		
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	@EJB
	UserDAO userDAO;
		
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<User> getFullList(){
		return userDAO.getFullList();
	}

	public List<User> getList(){
		List<User> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (username != null && username.length() > 0){
			searchParams.put("username", username);
		}
		
		//2. Get list
		list = userDAO.getList(searchParams);
		
		return list;
	}

	public String newUser(){
		User user = new User();
		
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		
		//2. Pass object through flash	
		flash.put("user", user);
		
		return PAGE_PERSON_EDIT;
	}

	public String editUser(User user){
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		
		//2. Pass object through flash 
		flash.put("user", user);
		
		return PAGE_PERSON_EDIT;
	}

	public String deleteUser(User user){
		userDAO.remove(user);
		return PAGE_STAY_AT_THE_SAME;
	}
	
}
