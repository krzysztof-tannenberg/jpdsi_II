package jsf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jsf.entities.Car;
import jsf.entities.Client;
import jsf.entities.Model;
import jsf.entities.Rent;


@Stateless
public class ClientDao {
	private final static String UNIT_NAME = "SalonPU";;
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	private Query query;

	Client queryFilter = new Client();

	public void create(Client client) {
		em.persist(client);
	}

	public Client merge(Client client) {
		return em.merge(client);
	}
	
	public void remove(Client client) {
		em.remove(em.merge(client));
	}

	public Client find(Object id) {
		return em.find(Client.class, id);
	}
	
	public List<Client> getFullList() {
		List<Client> list = null;

		Query query = em.createQuery("SELECT a FROM Client a");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	public List<Client> getLazyList(Map<String, Object> filterParams, int offset, int pageSize) {
		List<Client> list = null;
		System.out.println(filterParams);

		String where = this.setFilter(filterParams);
		String join = "";
		join += " ";
		query = em.createQuery("SELECT c FROM Client c " + join + where + "ORDER BY c.id_Client").setFirstResult(offset)
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
		join += " ";
		query = em.createQuery("SELECT COUNT(c) FROM Client c " + join + where);
		this.setFilterParam(filterParams);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	private String setFilter(Map<String, Object> filterParams) {
		String where = "";
		
			queryFilter.setFirstName((String)filterParams.get("firstName"));

			queryFilter.setSecondName((String)filterParams.get("secondName"));

			queryFilter.setTelefon((String)filterParams.get("telefon")); 

			queryFilter.setNip((String)filterParams.get("nip")); 
		

		where = this.createWhere("firstName", queryFilter.getFirstName(), where);
		where = this.createWhere("secondName", queryFilter.getSecondName(), where);
		where = this.createWhere("telefon", queryFilter.getTelefon(), where);
		where = this.createWhere("nip", queryFilter.getNip(), where);
		
		return where;
	}

	private void setFilterParam(Map<String, Object> filterParams) {
		 			
		
		if (queryFilter.getFirstName() != null) {
			query.setParameter("firstName",queryFilter.getFirstName() + "%");
		}
		if (queryFilter.getSecondName() != null) {
			query.setParameter("secondName", queryFilter.getSecondName() + "%");
		}
		if (queryFilter.getTelefon() != null) {
			query.setParameter("telefon",  queryFilter.getTelefon() + "%");
		}
		if (queryFilter.getNip() != null) {
			query.setParameter("nip",  queryFilter.getNip() + "%");
		}
		
	}

	private String createWhere(String paramName, String param, String currentWhere) {
		String where = currentWhere;

		if (param != null) {
			if (where.isEmpty()) {
				where = "WHERE ";
			} else {
				where += "AND ";
			}			
				where += "c." + paramName + " like :" + paramName + " ";			
		}

		return where;
	}
	
	public boolean checkIfRented (Client client) {
		
		long count = 0;
		String where = " where c.id_Client = :client and r.status=1";
		String join = " inner join r.client c";
		query = em.createQuery("SELECT COUNT(r) FROM Rent r" + join + where);
		query.setParameter("client",client.getId_Client());

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (count!=0) {
			return true;
		}
		
		
		return false;
		
	}
	public List<Rent> getRentInfo(Client client){
		List<Rent> list = null;
		
		String where = " where c.id_Client = :client and r.status=1";
		String join = " inner join r.client c";
		query = em.createQuery("SELECT r FROM Rent r" + join + where);
		query.setParameter("client",client.getId_Client());

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return list;
	}
	public int getLastId() {
		int id=0;
		query=em.createQuery("SELECT c.id_Client from Client c order by c.id_Client DESC").setMaxResults(1);
		
		try {
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return id;
	}
	public boolean checkifexist(String field,String value,Client client) {
		long count = 0;
		String where = " where c."+field+" = :value";
		if(client.getId_Client()!=null) {
			where+=" and c.id_Client !="+client.getId_Client();
		}
		query=em.createQuery("SELECT count(c) from Client c "+where);
		query.setParameter("value", value);
		
		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (count!=0) {
			return true;
		}
		return false;
	}
	
	
}