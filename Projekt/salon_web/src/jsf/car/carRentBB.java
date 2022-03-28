package jsf.car;
import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jsf.dao.CarDao;
import jsf.entities.Car;

@Named
@ViewScoped
public class carRentBB implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String PAGE_STAY_AT_THE_SAME = null;
    private static final String PAGE_CAR_LIST = "carList?faces-redirect=true";

    private Car car = new Car();
    private Car loaded = null;

    @Inject
    FacesContext ctx;
    @Inject
    Flash flash;

    @EJB
    CarDao carDao;

    public Car getCar() {
        return car;
    }

    public void onLoad() throws IOException {
        loaded = (Car) flash.get("car");

        if (loaded != null) {
            car = loaded;
        } else {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³êdne u¿ycie systemu", null));
        }
   }
    }