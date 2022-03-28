package jsf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jsf.entities.Account;
import jsf.entities.Car;
import jsf.entities.Client;
import jsf.entities.Rent;

@Stateless
public class RentDao {
	private final static String UNIT_NAME = "SalonPU";;
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	private Query query;
	@EJB
	AccountDao accountDao;
	
	@EJB
	ClientDao clientDao;

	public void create(Rent rent) {
		em.persist(rent);
	}

	public Rent merge(Rent rent) {
		return em.merge(rent);
	}

	public void remove(Rent rent) {
		em.remove(em.merge(rent));
	}

	public Rent find(Object id) {
		return em.find(Rent.class, id);
	}

	public List<Rent> getFullList() {
		List<Rent> list = null;

		Query query = em.createQuery("SELECT a FROM Rent a");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Rent> getRentInfo(Car car) {
		List<Rent> list = null;

		String where = "WHERE c.id_Car =:idCar AND r.status = 1";
		String join = "";
		join += "INNER JOIN r.car c ";
		query = em.createQuery("SELECT r FROM Rent r " + join + where);
		query.setParameter("idCar", car.getId_Car());

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Rent> getLazyList(Map<String, Object> filterParams, int offset, int pageSize) {

		List<Rent> list = null;
		Account account = accountDao.find(getAccountId((String) filterParams.get("login")));
		int id=account.getClient().getId_Client();
		
		String where = " where c.id_Client=:client and r.status=:status";		
		String join = " inner join r.client c ";
		query = em.createQuery("SELECT r FROM Rent r " + join + where + " ORDER BY r.idrent").setFirstResult(offset)
				.setMaxResults(pageSize);
		query.setParameter("client",id);
		query.setParameter("status", filterParams.get ("status"));
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		
		return list;

	}

	public long countLazyList(Map<String, Object> filterParams) {
		
		long count = 0;
		Account account = accountDao.find(getAccountId((String) filterParams.get("login")));
		int id=account.getClient().getId_Client();
		
		String where = " where c.id_Client=:client and r.status=:status";
		String join = " inner join r.client c ";
		
		query = em.createQuery("SELECT COUNT(r) FROM Rent r " + join + where);
		query.setParameter("client",id);
		query.setParameter("status", filterParams.get ("status"));
		
	
		
		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getAccountId(String login) {
		int id = 0;
		String where = " where a.login=:login";
		query = em.createQuery("SELECT a.idaccount FROM Account a " + where);
		query.setParameter("login", login);
		
	
		try {
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}

	public int getRentID(Car car) {
		int id=0;
		String where = " where c.id_Car=:car";
		String join= " inner join r.car c ";
		query = em.createQuery("SELECT r.idrent FROM Rent r " + join + where);
		query.setParameter("car", car.getId_Car());	
		
		try {
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return id;
				
	}

}