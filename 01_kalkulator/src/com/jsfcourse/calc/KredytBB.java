package com.jsfcourse.calc;

import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import static java.lang.Math.*;


@Named
@RequestScoped
//@SessionScoped
public class KredytBB {
	private String x;
	private String y;
	private String r;
	private Integer m; 
	private Double q;
	private Double result;
	
	@Inject
	FacesContext ctx;
	
	public String getR() {
		return r;
	}

	public void setR(String r) {
		this.r = r;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public boolean doTheMath() {
		try {
			double x = Double.parseDouble(this.x);
			double y = Double.parseDouble(this.y);
			double r = Double.parseDouble(this.r);
			
			m = 12;
			q = 1 + (r/m);
			
			result = ((x * Math.pow(q,y))*((q-1)) / (Math.pow(q,y)-1));

			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Udało się !", null));
			return true;
		} catch (Exception e) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd zmiennych", null));
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
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Wynik: " + result, null));
		}
		return null;
	}

	public String info() {
		return "info";
	}
}
