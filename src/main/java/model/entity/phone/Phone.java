package model.entity.phone;

import model.entity.Entity;
import model.entity.person.Person;
import model.dao.phone.PhoneDAO;

/**
* 
* @author Matheus Henrique
*/
public class Phone implements Entity{

	private int id = -1;

	private String number;

	private boolean status;

	private Person person;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public static Phone load(int id) {
		return PhoneDAO.load(id);
	}

	public static Phone load(String number) {
		return PhoneDAO.load(number);
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public boolean save() {
		if (this.id > -1) {
			if (PhoneDAO.update( this )) {
				return true;
			}
			return false;
		}

		if (PhoneDAO.create(this)) {
			this.setId(PhoneDAO.LAST_ID_INSERT);
			return true;
		}
		return false;
	}
}

