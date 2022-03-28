
 
package jsf.car;
 

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jsf.dao.CarDao;
import jsf.entities.Car;
import jsf.entities.Model;
 
@Named
@ViewScoped
public class CarNewBB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String PAGE_MAIN_BACK = "CarList.xhtml?faces-redirect=true";
    private static final String PAGE_CAR_NEW = "CarList.xhtml?faces-redirect=true";
    private static final String PAGE_STAY_AT_THE_SAME = null;
    
    private Car car = new Car();
    private Car loaded = null;
    @EJB
    CarDao carDao;
    @Inject
    FacesContext context;
    @Inject
    Flash flash;
 
    public CarNewBB() {
    }
 
    public Car getCar() {
        return this.car;
    }
 
    public void onLoad() throws IOException {
        this.loaded = (Car)this.flash.get("car");
        if (this.loaded != null) {
            this.car = this.loaded;
        } else {
            this.context.addMessage((String)null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wyst¹pi³ b³¹d!", (String)null));
        }
 
    }
 
    public String saveCar() {
        try {
            if (this.car.getId_Car() == 0) {
                this.carDao.create(this.car);
            } else {
                this.carDao.merge(this.car);
            }
 
            return "/pages/salon/CarList.xhtml?faces-redirect=true";
        } catch (Exception var2) {
            var2.printStackTrace();
            this.context.addMessage((String)null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wyst¹pi³ b³¹d podczas zapisu!", (String)null));
            return PAGE_STAY_AT_THE_SAME;
        }
        
    }
 
    @PostConstruct
    public void init() {
        this.car.setStatus(0);
        this.car.setModel(new Model());
    }
 
    public void showError() {
        this.addMessage(FacesMessage.SEVERITY_ERROR, "Error Message", "Message Content");
    }
 
    public void addMessage(Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage((String)null, new FacesMessage(severity, summary, detail));
    }
}