package model.entity.address;

import model.entity.Entity;
import model.dao.address.AddressDAO;

import java.util.ArrayList;

/**
* 
* @author FelipeMantoan
*/
public class Address implements Entity {

	private int id = -1;

	private int number;

	private String street;

	private String neighborhood;

	private String cep;

	public static Address load(int id) {
		return AddressDAO.load(id);
	}

	public static ArrayList<Address> loadAll() {
		return AddressDAO.loadAll();
	}

	public void setId(int idAddress){
		this.id = idAddress;
	}

	public int getId(){
		return this.id;
	}

	public void setStreet(String street){
		this.street = street;
	}

	public String getStreet(){
		return this.street;
	}

	public void setNumber(int number){
		this.number = number;
	}

	public int getNumber(){
		return this.number;
	}

	public void setNeighborhood(String neighborhood){
		this.neighborhood = neighborhood;
	}

	public String getNeighborhood(){
		return this.neighborhood;
	}

	public void setCep(String cep){
		this.cep = cep;
	}

	public String getCep(){
		return this.cep;
	}

	@Override
	public boolean save() {
		boolean isSaved = false;

		if (this.id == -1) {
			if (AddressDAO.create(this)) {
				this.setId(AddressDAO.LAST_ID_INSERT);
				isSaved = true;
			}
		}

		if (AddressDAO.update( this )) {
			isSaved = true;
		}

		return isSaved;
	}

}
