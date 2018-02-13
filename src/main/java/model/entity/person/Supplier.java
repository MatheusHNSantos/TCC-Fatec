package model.entity.person;

import model.dao.person.SupplierDAO;

import java.util.ArrayList;

/**
* 
* @author Matheus Henrique
*/
public class Supplier extends Person {

	private String CNPJ;

	private boolean status;

	public static Supplier load(int id) {
		return SupplierDAO.load( id );
	}

	public static ArrayList<Supplier> loadAll () {
		return SupplierDAO.loadAll();
	}

	public String getCNPJ() {
		return CNPJ;
	}

	public void setCNPJ(String CNPJ) {
		this.CNPJ = CNPJ;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public boolean save(){

		boolean isSaved = false;

		if (super.save()) {

			if (super.getId() == -1) {
				if (SupplierDAO.create(this)) {
					this.setId(SupplierDAO.LAST_ID_INSERT);
					isSaved = true;
				}
			}

			if (SupplierDAO.update(this)) {
				isSaved = true;
			}
		}

		if (super.savePhones()) {
			isSaved = true;
		}

		return isSaved;
	}
}
