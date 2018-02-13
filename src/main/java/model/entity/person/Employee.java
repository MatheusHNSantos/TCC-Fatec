package model.entity.person;

import model.dao.person.EmployeeDAO;
import model.dao.person.PersonDAO;

import java.util.ArrayList;

/**
* 
* @author Matheus Henrique
*/
public class Employee extends Person {

	private int idEmployee = -1;

	private String RG;

	private String CPF;

	protected String role;

	private boolean status;

	public static Employee load(int id) {
		return EmployeeDAO.load(id);
	}

	public static ArrayList<Employee> loadAll() {
		return EmployeeDAO.loadAll();
	}

	public int getIdEmployee() {
		return idEmployee;
	}

	public void setIdEmployee(int idEmployee) {
		this.idEmployee = idEmployee;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setRG(String rg){
		this.RG = rg;
	}

	public String getRG(){
		return this.RG;
	}

	public void setCPF(String cpf){
		this.CPF = cpf;
	}

	public String getCPF(){
		return this.CPF;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public boolean save() {
		boolean isSaved = false;

		if (super.save()) {
			if (idEmployee == -1) {
				if (EmployeeDAO.create(this)) {
					this.setIdEmployee(EmployeeDAO.LAST_ID_INSERT);
					super.setId(PersonDAO.LAST_ID_INSERT);
					isSaved = true;
				}
			}

			if (EmployeeDAO.update(this)) {
				isSaved = true;
			}
		}

		if (super.savePhones()) {
			isSaved = true;
		}

		return isSaved;
	}

}
