package jsf.client;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jsf.entities.Account;
import jsf.entities.Client;
import jsf.entities.Role;
import jsf.dao.AccountDao;
import jsf.dao.ClientDao;
import jsf.dao.RoleDao;

@Named
@ViewScoped
public class ClientEditBB implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_CLIENT_LIST = "ClientList?faces-redirect=true";

	private Client client = new Client();
	private Account account = new Account();
	private Client loaded1 = null;
	private Account loaded2 = null;

	@Inject
	FacesContext ctx;
	@Inject
	Flash flash;

	@EJB
	ClientDao clientDao;
	@EJB
	RoleDao roleDao;
	@EJB
	AccountDao accountDao;

	public Client getclient() {
		return client;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void onLoad() throws IOException {
		loaded1 = (Client) flash.get("client");
		loaded2 = (Account) flash.get("account");

		if (loaded1 != null && loaded2 != null) {
			client = loaded1;
			account = loaded2;
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³êdne u¿ycie systemu", null));
		}
	}

	public String saveData() {
		if (loaded1 == null || loaded2 == null) {
			return PAGE_STAY_AT_THE_SAME;
		}

		if (client.getMail().length() == 0) {
			client.setMail(null);
		}
		boolean error=false;
		if (clientDao.checkifexist("nip", client.getNip(),client)){
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "NIP zosta³ ju¿ u¿yty", null));
			error=true;
		}
		
		if (clientDao.checkifexist("telefon", client.getTelefon(),client)){
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nr Telefonu zosta³ ju¿ u¿yty", null));
			error=true;
		}
		
		if (client.getMail()!=null && clientDao.checkifexist("mail", client.getMail(),client)){
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Adres E-mail zosta³ ju¿ u¿yty", null));
			error=true;
		}
		if(error) {
			return PAGE_STAY_AT_THE_SAME;
		}
		try {
			if (client.getId_Client() == null) {								
				clientDao.merge(client);
			} else {
				clientDao.merge(client);				
			}
			if(account.getIdaccount()==null) {
				Role role = roleDao.find((int) 2);				
				account.setRole(role);
				Client client=clientDao.find(clientDao.getLastId());
				account.setClient(client);
				accountDao.create(account);
			}else {
				accountDao.merge(account);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wyst¹pi³ b³¹d podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_CLIENT_LIST;
	}

}
