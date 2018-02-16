/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.person;

import model.dao.address.AddressDAO;
import model.dao.phone.PhoneDAO;
import model.entity.person.Customer;
import util.connection.ConnectionFactory;
import util.dialogs.FxDialogs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Felipe
 */
public class CustomerDAO extends PersonDAO {

    public static int LAST_ID_INSERT = -1;

    /**
     * Insere um registro no banco de dados.
     *
     * @param customer
     * @return
     */
    public static boolean create(Customer customer) {
        PersonDAO.create(customer);
        Connection conn = ConnectionFactory.getConnection();
        String sql = "INSERT INTO customer (status_customer, id_person) VALUES (?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, customer.isStatus());
            stmt.setInt(2, PersonDAO.LAST_ID_INSERT);
            // Resgata o ultimo id inserido :P
            LAST_ID_INSERT = PersonDAO.LAST_ID_INSERT;
            stmt.execute();
            return true;
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao inserir um Cliente.","Class: "+ CustomerDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }

        return false;
    }

    /**
     *
     * @param customer
     * @return
     */
    public static boolean update(Customer customer) {
        PersonDAO.update(customer);

        Connection conn = ConnectionFactory.getConnection();
        String sql = "UPDATE customer SET status_customer = ? WHERE id_person = ?";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, customer.isStatus());
            stmt.setInt(2, customer.getId());
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao atualizar cliente.","Class: "+ CustomerDAO.class + " - " + sqlE.getMessage(), sqlE);
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
    public static Customer load(int id) {
        Connection conn = ConnectionFactory.getConnection();

        String sql = "SELECT customer.status_customer, " +
                "person.name_person, person.id_person, address.id_address, " +
                "address.street_address, address.number_address,address.cep_address, " +
                "address.neighborhood_address " +
                "FROM customer " +
                "INNER JOIN person ON person.id_person = customer.id_person " +
                "INNER JOIN address ON address.id_address = person.id_address " +
                "WHERE customer.id_person = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Customer customer = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            customer = CustomerDAO.createInstance(rs);
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao selecionar cliente por ID.","Class: "+ CustomerDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return customer;
    }

    /**
     *
     * @param name
     * @return
     */
    public static Customer load(String name) {
        Connection conn = ConnectionFactory.getConnection();

        String sql = "SELECT customer.status_customer, " +
                "person.name_person, person.id_person, address.id_address, " +
                "address.street_address, address.number_address,address.cep_address, " +
                "address.neighborhood_address " +
                "FROM customer " +
                "INNER JOIN person ON person.id_person = customer.id_person " +
                "INNER JOIN address ON address.id_address = person.id_address " +
                "WHERE person.name_person LIKE %?%";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Customer customer = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            rs.next();
            customer = CustomerDAO.createInstance(rs);
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao pesquisar cliente.","Class: "+ CustomerDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return customer;
    }

    /**
     *
     * @return
     */
    public static ArrayList<Customer> loadAll () {

        ArrayList<Customer> customers = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();
        String sql = "SELECT customer.status_customer, " +
                "person.name_person, person.id_person ,address.id_address, " +
                "address.street_address, address.number_address, " +
                "address.cep_address, address.neighborhood_address " +
                "FROM customer " +
                "INNER JOIN person ON person.id_person = customer.id_person " +
                "INNER JOIN address ON address.id_address = person.id_address";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                // Acrescenta a instância de usuário na lista.
                customers.add(CustomerDAO.createInstance(rs));
            }
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao listar cliente.","Class: "+ CustomerDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return customers;
    }

    public static Customer createInstance(ResultSet result) {
        Customer customer = null;

        try {
            customer = new Customer();
            customer.setId(result.getInt("id_person"));
            customer.setStatus(result.getBoolean("status_customer"));
            customer.setNamePerson(result.getString("name_person"));
            customer.setAddress( AddressDAO.createInstance(result));
            // Usando a mágica
            customer.setPhones(PhoneDAO.load(customer));
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao criar instância de cliente.","Class: "+ CustomerDAO.class + " - " + sqlE.getMessage(), sqlE);
        }

        return customer;
    }
}
