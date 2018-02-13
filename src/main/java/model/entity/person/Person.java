package model.entity.person;

import model.entity.Entity;
import model.entity.address.Address;
import model.entity.phone.Phone;
import java.util.ArrayList;

/**
* 
* @author Matheus Henrique
*/
public abstract class Person implements Entity {

	private int idPerson = -1;

	private String namePerson;

    private Address address;

	private ArrayList<Phone> phones = new ArrayList();

	public void setId(int id){
		this.idPerson = id;
	}

	public int getId(){
		return this.idPerson;
	}

	public void setNamePerson(String namePerson){
		this.namePerson = namePerson;
	}

	public String getNamePerson(){
		return this.namePerson;
	}

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ArrayList<Phone> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<Phone> phones) {
        this.phones = phones;
    }

	@Override
	public boolean save() {
		return address.save();
	}


	protected boolean savePhones () {

		for(Phone phone: this.phones) {
			phone.setPerson(this);
			if (!phone.save()) {
				return false;
			}
		}

		return true;
	}
}