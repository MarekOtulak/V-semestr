/*package com.BeatLoop.converter;

import com.BeatLoop.dao.GenreDAO;
import com.BeatLoop.entities.Genre;

import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(forClass = Genre.class)
public class GenreConverter implements Converter {
	@EJB
    private GenreDAO genreDAO;
    
    public GenreConverter() {
        this.genreDAO = new GenreDAO();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Integer genreId = Integer.valueOf(value);
            return genreDAO.findById(genreId);  // Pobierz Genre z bazy po ID
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Genre) {
            Genre genre = (Genre) value;
            return String.valueOf(genre.getGenreId());  // Zwróć ID gatunku jako String
        }
        return "";
    }
}*/
