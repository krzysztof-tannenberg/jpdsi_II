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
import javax.persistence.Query;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import jsf.dao.AccountDao;
import jsf.dao.ClientDao;
import jsf.dao.ModelDao;
import jsf.dao.RentDao;
import jsf.entities.Account;
import jsf.entities.Car;
import jsf.entities.Client;
import jsf.entities.Rent;

@Named
@ViewScoped
public class carClientBB implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_CLIENT_EDIT = "ClientEdit?faces-redirect=true";
	private static final String PAGE_MAIN = "main?faces-redirect=true";

	private String firstName;
	private String secondName;
	private String telefon;
	private String nip;

	private LazyDataModel<Client> lazyclient;
	private Client selectedClient;

	@Inject
	ExternalContext externalContext;
	@Inject
	Flash flash;
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
		lazyclient = new LazyDataModel<Client>() {
			private static final long serialVersionUID = 1L;

			private List<Client> clients;

			Map<String, Object> filterParams = new HashMap<String, Object>();

			@Override
			public Client getRowData(String rowKey) {
				for (Client client : clients) {
					if (client.getId_Client() == Integer.parseInt(rowKey)) {
						return client;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Client client) {
				return String.valueOf(client.getId_Client());
			}

			@Override
			public List<Client> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				filterParams.clear();

				if (firstName != null && firstName.length() > 0) {
					filterParams.put("firstName", firstName);
				}
				if (secondName != null && secondName.length() > 0) {
					filterParams.put("secondName", secondName);
				}

				if (telefon != null && telefon.length() > 0) {
					filterParams.put("telefon", telefon);
				}

				if (nip != null && nip.length() > 0) {
					filterParams.put("nip", nip);
				}

				clients = clientDao.getLazyList(filterParams, offset, pageSize);

				int rowCount = (int) clientDao.countLazyList(filterParams);
				setRowCount(rowCount);

				return clients;
			}

		};
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getNip() {
		return nip;
	}

	public void setNip(String nip) {
		this.nip = nip;
	}

	public Client getSelectedClient() {
		return selectedClient;
	}

	public void setSelectedClient(Client selectedClient) {
		this.selectedClient = selectedClient;
	}
	
	public List<Rent> getRentInfo(Client client) {
		return clientDao.getRentInfo(client);
	}

	public LazyDataModel<Client> getLazyclient() {
		return lazyclient;
	}

	public void clearFilter() {
		firstName = null;
		secondName = null;
		telefon = null;
		nip = null;
	}

	public void onRowSelect(SelectEvent<Car> event) {
		FacesMessage msg = new FacesMessage("Wybrany Klient", String.valueOf(event.getObject().getId_Car()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public boolean checkIfRented(Client client) {
		System.out.println(this.selectedClient);
		if (client != null) {
			return clientDao.checkIfRented(client);
		}
		return false;
	}

	public String editClient(Client client) {
		flash.put("client", client);
		Account account = accountDao.getAccount(client);
		flash.put("account", account);
		return PAGE_CLIENT_EDIT;
	}

	public String newClient() {
		Client client = new Client();
		Account account = new Account();
		flash.put("client", client);
		flash.put("account", account);
		return PAGE_CLIENT_EDIT;
	}
}
