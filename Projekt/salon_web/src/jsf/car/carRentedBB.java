package jsf.car;

import java.sql.Date;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import jsf.dao.CarDao;
import jsf.dao.RentDao;
import jsf.entities.Car;
import jsf.entities.Rent;



@Named
@ViewScoped
public class carRentedBB implements Serializable {
	
    private static final long serialVersionUID = 1L;
    private static final String PAGE_STAY_AT_THE_SAME = null;
    private static final String PAGE_CLIENT_LIST = "ClientList?faces-redirect=true";
    
	private LazyDataModel<Rent> lazyrent;
	private Rent selectedRent;  
	private String login;
	private byte status=1;
	

	@Inject
	FacesContext ctx;
	@EJB
	RentDao rentDao;
	@EJB
	CarDao carDao;	
	@PostConstruct
	public void init() {
		lazyrent = new LazyDataModel<Rent>() {
			private static final long serialVersionUID = 1L;

			private List<Rent> rents;

			Map<String, Object> filterParams = new HashMap<String, Object>();

			@Override
			public Rent getRowData(String rowKey) {
				for (Rent rent : rents) {
					if (rent.getIdrent() == Integer.parseInt(rowKey)) {
						return rent;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Rent rent) {
				return String.valueOf(rent.getIdrent());
			}

			@Override
			public List<Rent> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				filterParams.clear();
				filterParams.put("login", login);
				filterParams.put("status", status);
				rents = rentDao.getLazyList(filterParams, offset, pageSize);

				int rowCount = (int) rentDao.countLazyList(filterParams);
				setRowCount(rowCount);

				return rents;
			}


		};
	}

	public LazyDataModel<Rent> getLazyrent() {
		return lazyrent;
	}

	public void setLazyrent(LazyDataModel<Rent> lazyrent) {
		this.lazyrent = lazyrent;
	}

	public Rent getSelectedRent() {
		return selectedRent;
	}

	public void setSelectedRent(Rent selectedRent) {
		this.selectedRent = selectedRent;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	
	public String carReturn(Rent rent) {
		Car car=carDao.find(rent.getCar().getId_Car());
		car.setStatus((byte)0);
		rent.setCar(car);
		rent.setStatus((byte)0);
		rent.setReturnDate(java.sql.Date.valueOf(java.time.LocalDate.now()));
		carDao.merge(car);
		rentDao.merge(rent);
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Zwrócono Samochód o numerze Vin: " +car.getVin(), null));
		return null;
	}

	
}
    

