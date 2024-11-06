package com.jsfcourse.calc;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class KredytBB {
	private Double kwota;
	private Double ilelat;
	private Double oprocentowanie;
	private Double miesiace;
	private Double rata;
	private Double procent_100;
	private Double result;

	@Inject
	FacesContext ctx;

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

			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacja wykonana poprawnie", null));
			return true;
		} catch (Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas przetwarzania parametrów", null));
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

}
