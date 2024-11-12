package com.jsfcourse.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.io.Serializable;
import java.util.ResourceBundle;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
//import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("KredytBB")
//@RequestScoped
@SessionScoped
public class KredytBB implements Serializable {
	private Double kwota;
	private Double ilelat;
	private Double oprocentowanie;
	private Double miesiace;
	private Double rata;
	private Double procent_100;
	private Double result;
	private String locale = "pl";

	// Resource injected
	@Inject
	@ManagedProperty("#{txtKredErr}")
	private ResourceBundle txtKredErr;
    
	// Resource injected
	@Inject
	@ManagedProperty("#{txtKred}")
	private ResourceBundle txtKred;

	@Inject
	FacesContext ctx;
	
	@PostConstruct
	public void init() {
        updateLocale();
    }

	public void setLocale(String language) {
        this.locale = language;
        updateLocale();
    }
	
	public String getLocale() {
        return locale;
    }
	
	public void updateLocale() {
	    FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(this.locale));
	}

	public Double getKwota() {
		return kwota;
	}

	public void setKwota(Double kwota) {
		this.kwota = kwota;
	}

	public Double getIlelat() {
		return ilelat;
	}

	public void setIlelat(Double ilelat) {
		this.ilelat = ilelat;
	}

	public Double getOprocentowanie() {
		return oprocentowanie;
	}

	public void setOprocentowanie(Double oprocentowanie) {
		this.oprocentowanie = oprocentowanie;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}
	
	public boolean doTheMath() {
		try {
		//	double kwota = Double.parseDouble(this.kwota);
		//	double ilelat = Double.parseDouble(this.ilelat);
		//	double oprocentowanie = Double.parseDouble(this.oprocentowanie);

			//
			miesiace = ilelat * 12;
			rata = kwota / miesiace;
			procent_100 = oprocentowanie / 100;
			result = rata + rata * procent_100;
			//result = number_format(result, 2);
			//
			// Zaokrąglanie wyniku do 2 miejsc po przecinku
	        BigDecimal roundedResult = BigDecimal.valueOf(result).setScale(2, RoundingMode.HALF_UP);
	        result = roundedResult.doubleValue();
	        
	        ResourceBundle bundle = ResourceBundle.getBundle("resources.txtKredErr", ctx.getViewRoot().getLocale());
	        String successMessage = bundle.getString("done");
			
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successMessage, null));
			return true;
		} catch (Exception e) {
			ResourceBundle bundle = ResourceBundle.getBundle("resources.txtKredErr", ctx.getViewRoot().getLocale());
			String FailMessage = bundle.getString("error");
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, FailMessage, null));
			return false;
		}
	}

	// Go to "showresult" if ok
	public String calc() {
		updateLocale();
		if (doTheMath()) {
			return "showresult";
		}
		return null;
	}

	// Put result in messages on AJAX call
	public String calc_AJAX() {
		if (doTheMath()) {
			ResourceBundle bundle = ResourceBundle.getBundle("resources.txtKred", ctx.getViewRoot().getLocale());
	        String resultMessage = bundle.getString("wynosi");
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, resultMessage + ": " + result, null));
		}
		return null;
	}
	
	/*public void setLocale(String language) {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(language));
	}*/

}
