package jsf.entities;

import java.io.Serializable;
import javax.persistence.*;


@Entity
@NamedQuery(name="Account.findAll", query="SELECT a FROM Account a")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idaccount;

	private String login;

	private String password;

	
	@ManyToOne
	@JoinColumn(name="id_Client")
	private Client client;

	
	@ManyToOne
	@JoinColumn(name="idRole")
	private Role role;

	public Account() {
	}

	public Integer getIdaccount() {
		return this.idaccount;
	}

	public void setIdaccount(Integer idaccount) {
		this.idaccount = idaccount;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}