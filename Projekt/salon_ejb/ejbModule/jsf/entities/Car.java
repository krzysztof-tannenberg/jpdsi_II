package jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


@Entity
@NamedQuery(name="Car.findAll", query="SELECT c FROM Car c")
public class Car implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_Car;

	private String img;

	private String mileage;

	private String price;

	private int status;

	private String vin;

	private String year_of_production;

	
	@ManyToOne
	@JoinColumn(name="idmodel")
	private Model model;

	
	@OneToMany(mappedBy="car")
	private List<Rent> rents;

	public Car() {
	}

	public int getId_Car() {
		return this.id_Car;
	}

	public void setId_Car(int id_Car) {
		this.id_Car = id_Car;
	}

	public String getImg() {
		return this.img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getMileage() {
		return this.mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getPrice() {
		return this.price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getVin() {
		return this.vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getYear_of_production() {
		return this.year_of_production;
	}

	public void setYear_of_production(String year_of_production) {
		this.year_of_production = year_of_production;
	}

	public Model getModel() {
		return this.model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public List<Rent> getRents() {
		return this.rents;
	}

	public void setRents(List<Rent> rents) {
		this.rents = rents;
	}

	public Rent addRent(Rent rent) {
		getRents().add(rent);
		rent.setCar(this);

		return rent;
	}

	public Rent removeRent(Rent rent) {
		getRents().remove(rent);
		rent.setCar(null);

		return rent;
	}

}