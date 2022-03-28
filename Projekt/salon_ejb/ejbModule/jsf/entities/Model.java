package jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;



@Entity
@NamedQuery(name="Model.findAll", query="SELECT m FROM Model m")
public class Model implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idmodel;

	private String brand;

	@Column(name="name_model")
	private String nameModel;

	
	@OneToMany(mappedBy="model")
	private List<Car> cars;

	public Model() {
	}

	public Integer getIdmodel() {
		return this.idmodel;
	}

	public void setIdmodel(Integer idmodel) {
		this.idmodel = idmodel;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getNameModel() {
		return this.nameModel;
	}

	public void setNameModel(String nameModel) {
		this.nameModel = nameModel;
	}

	public List<Car> getCars() {
		return this.cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

	public Car addCar(Car car) {
		getCars().add(car);
		car.setModel(this);

		return car;
	}

	public Car removeCar(Car car) {
		getCars().remove(car);
		car.setModel(null);

		return car;
	}

}