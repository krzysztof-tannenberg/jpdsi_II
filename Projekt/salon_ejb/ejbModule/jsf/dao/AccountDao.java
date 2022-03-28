package jsf.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jsf.entities.Account;
import jsf.entities.Client;
import jsf.entities.Role;

@Stateless
public class AccountDao {
	private final static String UNIT_NAME = "SalonPU";;
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	private Query query;

	Account queryFilter = new Account();

	public void create(Account account) {
		em.persist(account);
	}

	public Account merge(Account account) {
		return em.merge(account);
	}
	
	public void remove(Account account) {
		em.remove(em.merge(account));
	}

	public Account find(Object id) {
		return em.find(Account.class, id);
	}
	
	public Account getAccountFromDatabase(String login, String pass) {
		Account u = null;

		Query query = em.createQuery("FROM Account a where a.login=:login");
		query.setParameter("login", login);

		try {
			Account account = (Account) query.getSingleResult();

			if (login.equals(account.getLogin()) && pass.equals(account.getPassword())) {
				u = new Account();
				u.setLogin(login);
				u.setPassword(pass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return u;
	}

	public List<String> getAccountRolesFromDatabase(Account account) {
		ArrayList<String> roles = new ArrayList<String>();

		Query query = em.createQuery("SELECT r FROM Account a JOIN a.role r where a.login=:login");
		query.setParameter("login", account.getLogin());

		try {
			Role role = (Role) query.getSingleResult();
			roles.add(role.getRoleName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return roles;
	}
	
	public List<Account> getLazyList(Map<String, Object> filterParams, int offset, int pageSize) {
		List<Account> list = null;

		String where = this.setFilter(filterParams);
		String join = "";
		join += "INNER JOIN a.role r ";
		query = em.createQuery("SELECT a FROM Account r " + join + where + "ORDER BY a.login").setFirstResult(offset)
				.setMaxResults(pageSize);
		this.setFilterParam(filterParams);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public long countLazyList(Map<String, Object> filterParams) {
		long count = 0;

		String where = this.setFilter(filterParams);
		String join = "";
		join += "INNER JOIN a.role r ";
		query = em.createQuery("SELECT COUNT(a) FROM Account a " + join + where);
		this.setFilterParam(filterParams);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}
	
	public List<Account> getFullList() {
		List<Account> list = null;

		Query query = em.createQuery("SELECT a FROM Account a");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

private String setFilter(Map<String, Object> filterParams) {
	String where = "";

	queryFilter.setLogin((String) filterParams.get("login"));

	where = this.createWhere("login", queryFilter.getLogin(), where);
	where = this.createWhere("name", "user", where);

	return where;
}

private void setFilterParam(Map<String, Object> filterParams) {

	if (queryFilter.getLogin() != null) {
		query.setParameter("login", "%" + queryFilter.getLogin() + "%");
	}
	query.setParameter("name", "user");
}

private String createWhere(String paramName, String param, String currentWhere) {
	String where = currentWhere;

	if (param != null) {
		if (where.isEmpty()) {
			where = "WHERE ";
		} else {
			where += "AND ";
		}
		if (paramName.equals("login")) {
			where += "a." + paramName + " like :" + paramName + " ";
		}
	}

	return where;
}
public Account getAccount(Client client) {
	Account account = null;

	Query query = em.createQuery("FROM Account a where a.client=:client");
	query.setParameter("client", client);

	try {
		 account = (Account) query.getSingleResult();

	} catch (Exception e) {
		e.printStackTrace();
	}

	return account;
}
}

