package model.entity.person;

import model.dao.person.CostumerDAO;

import java.util.ArrayList;

/**
* 
* @author Matheus Henrique
*/
public class Costumer extends Person {

	private boolean status;

	public static Costumer load(int id){
		return CostumerDAO.load(id);
	}

	public static ArrayList<Costumer> loadAll(){
		return CostumerDAO.loadAll();
	}

	@Override
	public boolean save() {

		boolean isSaved = false;

		if (super.save()) {
			if (super.getId() == -1) {
				if (CostumerDAO.create(this)) {
					super.setId(CostumerDAO.LAST_ID_INSERT);
					isSaved = true;
				}
			}

			if (CostumerDAO.update(this)) {
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
