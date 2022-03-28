package jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;



@Entity
@NamedQuery(name="Rent.findAll", query="SELECT r FROM Rent r")
public class Rent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idrent;

	@Temporal(TemporalType.DATE)
	@Column(name="borrow_date")
	private Date borrowDate;

	@Temporal(TemporalType.DATE)
	@Column(name="return_date")
	private Date returnDate;

	private byte status;

	
	@ManyToOne
	@JoinColumn(name="id_Car")
	private Car car;

	
	@ManyToOne
	@JoinColumn(name="id_Client")
	private Client client;

	public Rent() {
	}

	public Integer getIdrent() {
		return this.idrent;
	}

	public void setIdrent(Integer idrent) {
		this.idrent = idrent;
	}

	public Date getBorrowDate() {
		return this.borrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public Date getReturnDate() {
		return this.returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Car getCar() {
		return this.car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}