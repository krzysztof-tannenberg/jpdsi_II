package jsf.car;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import jsf.dao.AccountDao;
import jsf.dao.CarDao;
import jsf.dao.ClientDao;
import jsf.dao.ModelDao;
import jsf.dao.RentDao;
import jsf.entities.Account;
import jsf.entities.Car;
import jsf.entities.Client;
import jsf.entities.Rent;

@Named
@ViewScoped
public class CarListBB implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_MAIN = "main?faces-redirect=true";
	private static final String PAGE_CONFIRM_RENT = "RentConfirm?faces-redirect=true";
	private static final String PAGE_EDIT_CAR = "CarEdit?faces-redirect=true";
	private static final String PAGE_CAR_LIST = "CarList?faces-redirect=true";

	private String brand;
	private String nameModel;
	private int PriceMIN = 0;
	private int PriceMAX = 0;
	private int status = 0;
	private String login;
	private String vin;

	private LazyDataModel<Car> lazycars;
	private Car selectedCar;

	@Inject
	ExternalContext externalContext;
	@Inject
	FacesContext ctx;
	@Inject
	Flash flash;
	@EJB
	CarDao carDao;
	@EJB
	ModelDao modelDao;
	@EJB
	RentDao rentDao;
	@EJB
	ClientDao clientDao;
	@EJB
	AccountDao accountDao;

	@PostConstruct
	public void init() {
		lazycars = new LazyDataModel<Car>() {
			private static final long serialVersionUID = 1L;

			private List<Car> cars;

			Map<String, Object> filterParams = new HashMap<String, Object>();

			@Override
			public Car getRowData(String rowKey) {
				for (Car car : cars) {
					if (car.getId_Car() == Integer.parseInt(rowKey)) {
						return car;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Car car) {
				return String.valueOf(car.getId_Car());
			}

			@Override
			public List<Car> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				filterParams.clear();

				if (vin != null && vin.length() > 0) {
					filterParams.put("vin", vin);
				}
				if (brand != null && brand.length() > 0) {
					filterParams.put("brand", brand);
				}
				if (nameModel != null && nameModel.length() > 0) {
					filterParams.put("nameModel", nameModel);
				}
				if (PriceMIN != 0) {
					filterParams.put("PriceMIN", PriceMIN);
				}
				if (PriceMAX != 0) {
					filterParams.put("PriceMAX", PriceMAX);
				}
				filterParams.put("status", status);

				cars = carDao.getLazyList(filterParams, offset, pageSize);

				int rowCount = (int) carDao.countLazyList(filterParams);
				setRowCount(rowCount);

				return cars;
			}

		};
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getNameModel() {
		return nameModel;
	}

	public void setNameModel(String nameModel) {
		this.nameModel = nameModel;
	}

	public int getPriceMIN() {
		return PriceMIN;
	}

	public void setPriceMIN(int priceMIN) {
		PriceMIN = priceMIN;
	}

	public int getPriceMAX() {
		return PriceMAX;
	}

	public void setPriceMAX(int priceMAX) {
		PriceMAX = priceMAX;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public LazyDataModel<Car> getLazycars() {
		return lazycars;
	}

	public void setLazycars(LazyDataModel<Car> lazycars) {
		this.lazycars = lazycars;
	}

	public Car getSelectedCar() {
		return selectedCar;
	}

	public void setSelectedCar(Car selectedCar) {
		this.selectedCar = selectedCar;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public List<Rent> getRentInfo(Car car) {
		return rentDao.getRentInfo(car);
	}

	public void clearFilter() {
		nameModel = null;
		brand = null;
		PriceMIN = 0;
		PriceMAX = 0;
		status = 0;
		vin = null;
	}

	public void onRowSelect(SelectEvent<Car> event) {
		FacesMessage msg = new FacesMessage("Wybrany Samochód", String.valueOf(event.getObject().getId_Car()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String rentCar(Car car) {
		flash.put("car", car);

		return PAGE_CONFIRM_RENT;
	}

	public String confirmRent(Car car) {
		Account account = accountDao.find(rentDao.getAccountId(login));

		Client client = clientDao.find(account.getClient().getId_Client());
		Rent rent = new Rent();

		Date today = java.sql.Date.valueOf(java.time.LocalDate.now());
		Date returnDate = null;

		rent.setCar(car);
		rent.setClient(client);
		rent.setBorrowDate(today);
		rent.setReturnDate(null);
		rent.setStatus((byte) 1);

		car.setStatus((byte) 1);

		rentDao.create(rent);
		carDao.merge(car);

		System.out.println(login);
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Pomyœlnie Wypo¿yczono Samochód " + car.getModel().getBrand() + " " + car.getModel().getNameModel(),null));
		return null;

	}	
	
	public String deleteCar(Car car) {
        this.carDao.remove(car);
        
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuniêto Samochód o numerze Vin: " +car.getVin(), null));
        
        return "CarList?faces-redirect=true";
    }
	
	public String editCar(Car car) {
        this.flash.put("car", car);
        
        System.out.println(login);
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Pomyœlnie Edytowano Samochód " + car.getModel().getBrand() + " " + car.getModel().getNameModel(),null));
        
        return "CarList?faces-redirect=true";
    }
	
	
	
}
