/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.person;

import model.dao.address.AddressDAO;
import model.dao.phone.PhoneDAO;
import model.entity.person.Employee;
import util.connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matheus Henrique
 */
public class EmployeeDAO extends PersonDAO {

    public static int LAST_ID_INSERT = -1;

    /**
     *
     * @param employee
     * @return
     */
    public static boolean create(Employee employee) {
        PersonDAO.create(employee);
        Connection conn = ConnectionFactory.getConnection();
        String sql = "INSERT INTO employee (role_employee, rg_employee, cpf_employee, status_employee, id_person) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, employee.getRole());
            stmt.setString(2, employee.getRG());
            stmt.setString(3, employee.getCPF());
            stmt.setBoolean(4, employee.isStatus());
            stmt.setInt(5, PersonDAO.LAST_ID_INSERT);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            rs.next();
            LAST_ID_INSERT = rs.getInt(1);
            return true;
        }
        catch (SQLException sqlE) {
            Logger.getLogger(EmployeeDAO.class.getName()).log( Level.SEVERE, null, sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return false;
    }

    /**
     *
     * @param employee
     * @return
     */
    public static boolean update(Employee employee) {
        PersonDAO.update(employee);

        Connection conn = ConnectionFactory.getConnection();
        String sql = "UPDATE employee SET " +
                "role_employee = ?, " +
                "rg_employee = ?, " +
                "cpf_employee = ?, " +
                "status_employee = ? " +
                "WHERE id_employee = ?";

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, employee.getRole());
            stmt.setString(2, employee.getRG());
            stmt.setString(3, employee.getCPF());
            stmt.setBoolean(4, employee.isStatus());
            stmt.setInt(5, employee.getIdEmployee());
            stmt.executeUpdate();
        }
        catch (SQLException sqlE) {
            Logger.getLogger(EmployeeDAO.class.getName()).log( Level.SEVERE, null, sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }

        return false;
    }

    /**
     *
     * @param id
     * @return
     */
    public static Employee load(int id) {
        Connection conn = ConnectionFactory.getConnection();

        String sql = "SELECT " +
            "employee.id_employee, employee.role_employee, " +
            "employee.rg_employee, employee.cpf_employee, employee.status_employee, " +
            "person.id_person, person.name_person, " +
            "address.id_address, address.street_address, " +
            "address.number_address, address.cep_address, address.neighborhood_address " +
            "FROM employee " +
            "INNER JOIN person ON person.id_person = employee.id_person " +
            "INNER JOIN address ON address.id_address = person.id_address " +
            "WHERE id_employee = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Employee employee = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            return EmployeeDAO.createInstance(rs);
        }
        catch (SQLException sqlE) {
            Logger.getLogger(EmployeeDAO.class.getName()).log( Level.SEVERE, null, sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return employee;
    }

    /**
     *
     * @return
     */
    public static ArrayList<Employee> loadAll() {

        ArrayList<Employee> employees = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();
        String sql = "SELECT " +
                "employee.id_employee, employee.role_employee, " +
                "employee.rg_employee, employee.cpf_employee, employee.status_employee, " +
                "person.id_person, person.name_person, " +
                "address.id_address, address.street_address, " +
                "address.number_address, address.cep_address, address.neighborhood_address " +
                "FROM employee " +
                "INNER JOIN person ON person.id_person = employee.id_person " +
                "INNER JOIN address ON address.id_address = person.id_address";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                employees.add(EmployeeDAO.createInstance(rs));
            }
        }
        catch (SQLException sqlE) {
            Logger.getLogger(EmployeeDAO.class.getName()).log( Level.SEVERE, null, sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return employees;
    }

    /**
     *
     * @param result
     * @return
     */
    public static Employee createInstance (ResultSet result) {

        Employee employee = new Employee();

        try {
            employee.setId( result.getInt( "id_person" ) );
            employee.setNamePerson( result.getString( "name_person" ) );
            employee.setAddress( AddressDAO.createInstance( result ) );

            employee.setRG(result.getString("rg_employee"));
            employee.setCPF(result.getString("cpf_employee"));
            employee.setStatus(result.getBoolean("status_employee"));
            employee.setIdEmployee( result.getInt( "id_employee" ) );
            employee.setRole(result.getString( "role_employee" ) );
            employee.setPhones(PhoneDAO.load(employee));
        }
        catch (SQLException sqlE) {
            Logger.getLogger(EmployeeDAO.class.getName()).log( Level.SEVERE, null, sqlE);
        }

        return employee;
    }
}
