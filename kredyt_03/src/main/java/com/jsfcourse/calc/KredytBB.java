package com.jsfcourse.calc;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class KredytBB {
	private String kwota;
	private String ilelat;
	private String oprocentowanie;
	private Double miesiace;
	private Double rata;
	private Double procent_100;
	private Double result;

	@Inject
	FacesContext ctx;

	public String getKwota() {
		return kwota;
	}

	public void setKwota(String kwota) {
		this.kwota = kwota;
	}

	public String getIlelat() {
		return ilelat;
	}

	public void setIlelat(String ilelat) {
		this.ilelat = ilelat;
	}

	public String getOprocentowanie() {
		return oprocentowanie;
	}

	public void setOprocentowanie(String oprocentowanie) {
		this.oprocentowanie = oprocentowanie;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}
	
	//
	private boolean validateInput() {
		try {
			double kwota = Double.parseDouble(this.kwota);
			if (kwota < 5000) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kwota pozyczki musi wynosić conajmniej 5000 PLN", null));
				return false;
			}
		} catch (NumberFormatException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kwota musi być liczbą", null));
			return false;
		}

		try {
			double ilelat = Double.parseDouble(this.ilelat);
			if (ilelat <= 0) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Liczba lat musi być większa od zera", null));
				return false;
			}
		} catch (NumberFormatException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Liczba lat musi być liczbą", null));
			return false;
		}

		try {
			double oprocentowanie = Double.parseDouble(this.oprocentowanie);
			if (oprocentowanie < 0) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oprocentowanie nie może być ujemne", null));
				return false;
			}
		} catch (NumberFormatException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oprocentowanie musi być liczbą", null));
			return false;
		}

		return true;
	}
	//

	public boolean doTheMath() {
		if (!validateInput()) {
			return false;
		}
		
		try {
			double kwota = Double.parseDouble(this.kwota);
			double ilelat = Double.parseDouble(this.ilelat);
			double oprocentowanie = Double.parseDouble(this.oprocentowanie);

			//
			miesiace = ilelat * 12;
			rata = kwota / miesiace;
			procent_100 = oprocentowanie / 100;
			result = rata + rata * procent_100;
			//result = number_format(result, 2);
			//

			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacja wykonana poprawnie", null));
			return true;
		} catch (Exception e) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas przetwarzania parametrów", null));
			return false;
		}
	}

	// Go to "showresult" if ok
	public String calc() {
		if (doTheMath()) {
			return "showresult";
		}
		return null;
	}

	// Put result in messages on AJAX call
	public String calc_AJAX() {
		if (doTheMath()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Twoja rata kredytu wynosi: " + result, null));

		}
		return null;
	}

	public String info() {
		return "info";
	}
}
