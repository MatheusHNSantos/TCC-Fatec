package model.entity.person;

import model.dao.person.CustomerDAO;

import java.util.ArrayList;

/**
* 
* @author Matheus Henrique
*/
public class Customer extends Person {

	private boolean status;

	public static Customer load(int id){
		return CustomerDAO.load(id);
	}

	public static Customer search(String name){
		return CustomerDAO.load(name);
	}

	public static ArrayList<Customer> loadAll(){
		return CustomerDAO.loadAll();
	}

	@Override
	public boolean save() {

		boolean isSaved = false;

		if (super.save()) {
			if (super.getId() == -1) {
				if (CustomerDAO.create(this)) {
					super.setId(CustomerDAO.LAST_ID_INSERT);
					isSaved = true;
				}
			}

			if (CustomerDAO.update(this)) {
				isSaved = true;
			}

		}

		if (super.savePhones()) {
			isSaved = true;
		}


		return isSaved;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
