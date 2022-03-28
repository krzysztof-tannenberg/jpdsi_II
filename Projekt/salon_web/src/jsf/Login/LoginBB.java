package jsf.Login;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jsf.dao.AccountDao;
import jsf.entities.Account;

@Named
@RequestScoped
public class LoginBB {
	@Inject
	AccountDao accountDao;
	
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_LOGIN = "/pages/login?faces-redirect=true";
	private static final String PAGE_MAIN = "/pages/salon/main?faces-redirect=true";
	
	private String login;
	private String password;
			
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String doLogin() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		
		Account account = accountDao.getAccountFromDatabase(login, password);
		
		
		if(account == null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				"Niepoprawny login lub has³o", null));
			return PAGE_STAY_AT_THE_SAME;
		}
		
		
		RemoteClient<Account> client = new RemoteClient<Account>();
		client.setDetails(account);
		
		List<String> roles =accountDao.getAccountRolesFromDatabase(account);
		
		if(roles != null) {
			for(String role: roles) {
				client.getRoles().add(role);
			}
		}
		
		
		HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
		client.store(request);
		
		return PAGE_MAIN;
	}
	
	public String doLogout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
			.getExternalContext().getSession(true);
		
		session.invalidate();
		
		return PAGE_LOGIN;
	}
}
