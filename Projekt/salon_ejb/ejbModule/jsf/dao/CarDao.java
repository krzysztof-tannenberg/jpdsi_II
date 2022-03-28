package jsf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jsf.entities.Account;
import jsf.entities.Car;
import jsf.entities.Client;
import jsf.entities.Model;

@Stateless
public class CarDao {
	private final static String UNIT_NAME = "SalonPU";;
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	private Query query;

	Car queryFilter = new Car();
	private int PriceMIN = 0;
	private int PriceMAX = 0;

	public void create(Car car) {
		em.persist(car);
	}

	public Car merge(Car car) {
		return em.merge(car);
	}

	public void remove(Car car) {
		em.remove(em.merge(car));
	}

	public Car find(Object id) {
		return em.find(Car.class, id);
	}

	public List<Car> getFullList() {
		List<Car> list = null;

		Query query = em.createQuery("SELECT a FROM Car a");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Car> getLazyList(Map<String, Object> filterParams, int offset, int pageSize) {
		List<Car> list = null;
		System.out.println(filterParams);

		String where = this.setFilter(filterParams);
		String join = "";
		join += "INNER JOIN c.model m ";
		query = em.createQuery("SELECT c FROM Car c " + join + where + "ORDER BY c.id_Car").setFirstResult(offset)
				.setMaxResults(pageSize);
		this.setFilterParam(filterParams);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("SELECT c FROM Car c " + join + where + "ORDER BY c.id_Car");
		return list;
	}

	public long countLazyList(Map<String, Object> filterParams) {
		long count = 0;

		String where = this.setFilter(filterParams);
		String join = "";
		join += "INNER JOIN c.model m ";
		query = em.createQuery("SELECT COUNT(c) FROM Car c " + join + where);
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
		this.PriceMIN = 0;
		this.PriceMAX = 0;
		Model model = new Model();
		if (filterParams.get("brand") != null) {
			model.setBrand((String) filterParams.get("brand"));
		}
		if (filterParams.get("nameModel") != null) {

			model.setNameModel((String) filterParams.get("nameModel"));
		}
		queryFilter.setModel(model);
		if (filterParams.get("PriceMIN") != null) {

			this.PriceMIN = (Integer) filterParams.get("PriceMIN");

		}
		if (filterParams.get("PriceMAX") != null) {

			this.PriceMAX = (Integer) filterParams.get("PriceMAX");

		}		 
			queryFilter.setVin((String)filterParams.get("vin"));
		
		queryFilter.setStatus((int) filterParams.get("status"));
		where += "where c.status =:status ";

		where = this.createWhere("brand", queryFilter.getModel().getBrand(), where);
		where = this.createWhere("nameModel", queryFilter.getModel().getNameModel(), where);
		where = this.createWhere("vin", queryFilter.getVin(), where);
		if (PriceMIN != 0) {
			where += " and c.price >= :pMIN ";
		}
		if (PriceMAX != 0) {
			where += " and c.price <= :pMAX ";
		}

		return where;
	}

	private void setFilterParam(Map<String, Object> filterParams) {

		query.setParameter("status", queryFilter.getStatus());

		if (queryFilter.getModel().getBrand() != null) {
			query.setParameter("brand", "%" + queryFilter.getModel().getBrand() + "%");
		}
		if (queryFilter.getModel().getNameModel() != null) {
			query.setParameter("nameModel", "%" + queryFilter.getModel().getNameModel() + "%");

		}

		if (queryFilter.getVin() != null) {
			query.setParameter("vin", queryFilter.getVin() + "%");
		}
		if (PriceMIN != 0) {
			query.setParameter("pMIN", PriceMIN);
		}
		if (PriceMAX != 0) {
			query.setParameter("pMAX", PriceMAX);
		}
	}

	private String createWhere(String paramName, String param, String currentWhere) {
		String where = currentWhere;

		if (param != null && param.length()>0) {
			if (where.isEmpty()) {
				where = "WHERE ";
			} else {
				where += "AND ";
			}
			if (paramName.equals("brand") || paramName.equals("nameModel")) {
				where += "m." + paramName + " like :" + paramName + " ";
			}

			if (paramName.equals("vin")) {
				where += "c." + paramName + " like :" + paramName + " ";
			}
		}

		return where;
	}
	
	
	
	
	
	
	
	public Car getCar(Model model) {
		Car car = null;

		Query query = em.createQuery("FROM Car a where a.model=:model");
		query.setParameter("model", model);

		try {
			car = (Car) query.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return car;
	}
}