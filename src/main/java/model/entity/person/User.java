package model.entity.person;

import model.entity.Entity;
import model.dao.person.UserDAO;

import java.util.List;

public class User implements Entity {

	private int id = -1;

	private String login;

	private boolean status;

	private String password;

	private int level;

	private Employee employee;


	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;

	}

	public static boolean auth(String login, String pass) {
        return UserDAO.login(login, pass);
    }

    public static User load(String login) {
	    return UserDAO.load(login);
    }

	public static User load(int id) {
		return UserDAO.load(id);
	}

	public static User load(Employee employee) {
		return UserDAO.load(employee);
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin(){
		return this.login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setEmployee(Employee employee){
		this.employee = employee;
	}

	public Employee getEmployee(){
		return this.employee;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public boolean save() {

		if (employee.save()) {
			if (this.getId() == -1) {
				if (UserDAO.create(this)) {
					this.setId( UserDAO.LAST_ID_INSERT );
					return true;
				}

				return false;
			}

			if (UserDAO.update(this)) {
				return true;
			}
		}

		return false;
	}

    public static List<User> loadAll() {
		return UserDAO.loadAll();
    }
}
