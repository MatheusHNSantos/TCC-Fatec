/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.phone;

import model.dao.DAO;
import model.entity.person.Customer;
import model.entity.person.Employee;
import model.entity.person.Person;
import model.entity.person.Supplier;
import model.entity.phone.Phone;
import util.connection.ConnectionFactory;
import util.dialogs.FxDialogs;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author FelipeMantoan
 */
public class PhoneDAO implements DAO {

    public static int LAST_ID_INSERT = -1;

    public static boolean create(Phone phone) {

        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO phone (number_phone, status_phone,id_person) VALUES (?, ?, ?)";

        try {
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, phone.getNumber());
            stmt.setBoolean(2, phone.isStatus());
            stmt.setInt(3, phone.getPerson().getId());

            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            rs.next();
            LAST_ID_INSERT = rs.getInt(1);
            return true;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return false;
    }

    /**
     * Carrega telefone pelo ID.
     * @param id
     * @return
     */
    public static Phone load(int id) {
        Connection conn = ConnectionFactory.getConnection();

        String sql = "SELECT * FROM phone WHERE id_phone = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Phone phone = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            phone = PhoneDAO.createInstance(rs);
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao carregar telefone por Id.","Class: "+ PhoneDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return phone;
    }

    /**
     * Carrega uma lista de pessoas por telefone.
     * @param person
     * @return
     */
    public static ArrayList<Phone> load(Person person) {
        Connection conn = ConnectionFactory.getConnection();

        String sql = "SELECT * FROM phone WHERE id_person = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Phone> phones = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, person.getId());
            rs = stmt.executeQuery();
            phones = new ArrayList();
            while(rs.next()) {
                phones.add(PhoneDAO.createInstance(rs));
            }
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao carregar telefone por pessoa.","Class: "+ PhoneDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return phones;
    }

    /**
     *  Carrega telefone pelo número.
     * @param number
     * @return
     */
    public static Phone load(String number) {
        Connection conn = ConnectionFactory.getConnection();

        String sql = "SELECT * FROM phone WHERE number_phone = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Phone phone = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, number);
            rs = stmt.executeQuery();
            rs.next();
            phone = PhoneDAO.createInstance(rs);
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao carregar pelo número de telefone.","Class: "+ PhoneDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return phone;
    }

    /**
     * Carrega pessoas a partir de um número de telefone.
     * @param number
     * @return
     */
    protected static ArrayList<Integer> loadPersonByPhone(String number) {
        Connection conn = ConnectionFactory.getConnection();

        String sql = "SELECT id_person FROM phone WHERE number_phone = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList ids = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, number);
            rs = stmt.executeQuery();
            ids = new ArrayList();
            while(rs.next()) {
                ids.add(rs.getInt("id_person"));
            }
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao carregar pessoa pelo número de telefone.","Class: "+ PhoneDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return ids;
    }

    /**
     * Carrega clientes a partir de um número de telefone.
     * @param number
     * @return
     */
    public static ArrayList<Customer> loadCustomerByPhone(String number) {
        ArrayList<Customer> customers = new ArrayList();

        for(Integer id : PhoneDAO.loadPersonByPhone(number)) {
            customers.add(Customer.load(id));
        }

        return customers;
    }

    /**
     * Carrega empregados a partir de um número de telefone.
     * @param number
     * @return
     */
    public static ArrayList<Employee> loadEmployeeByPhone(String number) {
        ArrayList<Employee> employees = new ArrayList();

        for(Integer id : PhoneDAO.loadPersonByPhone(number)) {
            employees.add(Employee.load(id));
        }

        return employees;
    }

    public static ArrayList<Supplier> loadSupplierByPhone(String number) {
        ArrayList<Supplier> suppliers = new ArrayList();

        for(Integer id : PhoneDAO.loadPersonByPhone(number)) {
            suppliers.add(Supplier.load(id));
        }

        return suppliers;
    }

    /**
     * Recebe um Result e extrai os dados dele,
     * porém não fecha a conexão, por questão de reaproveitamento de código.
     *
     * @param result
     * @return
     */
    public static Phone createInstance(ResultSet result) {

        Phone phone = null;

        try {
            phone = new Phone();
            phone.setId(result.getInt("id_phone"));
            phone.setNumber(result.getString("number_phone"));
            phone.setStatus(result.getBoolean("status_phone"));
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao criar instância de telefone.","Class: "+ PhoneDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        return phone;
    }

    /**
     * Atualiza um número de telefone.
     * @param phone
     * @return
     */
    public static boolean update(Phone phone) {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "UPDATE phone SET number_phone = ?, status_phone = ? WHERE id_phone = ?";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, phone.getNumber());
            stmt.setBoolean(2, phone.isStatus());
            stmt.setInt(3, phone.getId());
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao atualizar telefone.","Class: "+ PhoneDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }

        return false;
    }
}
