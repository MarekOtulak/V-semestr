package com.BeatLoop.user;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.BeatLoop.dao.UserDAO;
import com.BeatLoop.entities.User;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("dtLazyView")
@ViewScoped
public class LazyView implements Serializable {
	
	private static final long serialVersionUID = 1L;  // Dodaj serialVersionUID
	
    private LazyDataModel<User> lazyModel;
    
    @Inject
    private UserDAO userDAO; // Wstrzyknięcie DAO
    
    //userList.list
    @PostConstruct
    public void init() {
        lazyModel = new LazyDataModel<User>() { //klasa LazyDataModel z metodą load(), która ładuje dane użytkowników z bazy danych na żądanie (tzw. lazy loading)

            @Override
            public List<User> load(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filters) { //load() odpowiada za pobieranie tylko tych rekordów, które są wymagane do wyświetlenia na obecnej stronie (z uwzględnieniem parametrów paginacji), dane są ładowane na żądanie z użyciem lazy loadingu.
                // Pobierz użytkowników z bazy danych z uwzględnieniem paginacji
                List<User> users = userDAO.getUsersWithPagination(offset, pageSize); //Parametry offset i pageSize w metodzie load() wskazują na paginację (pobieranie danych tylko dla danej strony wraz z odpowiednim limitem wierszy).

                // Ustaw liczbę rekordów (total count) bez filtrów
                setRowCount(userDAO.countUsers());

                return users;
            }

            @Override
            public int count(Map<String, FilterMeta> filters) {
                // Zwróć liczbę wszystkich użytkowników (bez filtrów)
                return userDAO.countUsers();
            }
        };
    }

    public LazyDataModel<User> getLazyModel() {
        return lazyModel;
    }
}