/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.person;

import model.dao.address.AddressDAO;
import model.dao.phone.PhoneDAO;
import model.entity.person.Supplier;
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
public class SupplierDAO extends PersonDAO {

    public static int LAST_ID_INSERT = -1;

    public static boolean create (Supplier supplier) {

        PersonDAO.create(supplier);
        Connection conn = ConnectionFactory.getConnection();
        String sql = "INSERT INTO supplier (cnpj_supplier, status_supplier, id_person) VALUES (?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, supplier.getCNPJ());
            stmt.setBoolean(2, supplier.isStatus());
            stmt.setInt(3, PersonDAO.LAST_ID_INSERT);
            LAST_ID_INSERT = PersonDAO.LAST_ID_INSERT;
            stmt.execute();
            return true;
        }
        catch (SQLException sqlE) {
            Logger.getLogger(SupplierDAO.class.getName()).log( Level.SEVERE, null, sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }

        return false;
    }

    public static boolean update (Supplier supplier) {
        PersonDAO.update(supplier);

        Connection conn = ConnectionFactory.getConnection();
        String sql = "UPDATE supplier SET cnpj_supplier = ?, status_supplier = ? WHERE id_person = ?";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, supplier.getCNPJ());
            stmt.setBoolean(2, supplier.isStatus());
            stmt.setInt(3, supplier.getId());
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException sqlE) {
            Logger.getLogger(SupplierDAO.class.getName()).log( Level.SEVERE, null, sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }

        return false;
    }

    public static Supplier load(int id) {
        Connection conn = ConnectionFactory.getConnection();

        String sql = "SELECT supplier.cnpj_supplier, supplier.status_supplier, " +
                "person.name_person, person.id_person, address.id_address, " +
                "address.street_address, address.number_address,address.cep_address, " +
                "address.neighborhood_address " +
                "FROM supplier " +
                "INNER JOIN person ON person.id_person = supplier.id_person " +
                "INNER JOIN address ON address.id_address = person.id_address " +
                "WHERE supplier.id_person = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Supplier supplier = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            supplier = SupplierDAO.createInstance(rs);
        }
        catch (SQLException sqlE) {
            Logger.getLogger(SupplierDAO.class.getName()).log( Level.SEVERE, null, sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return supplier;
    }

    public static ArrayList<Supplier> loadAll () {

        ArrayList<Supplier> suppliers = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();
        String sql = "SELECT supplier.cnpj_supplier, supplier.status_supplier, " +
                "person.name_person, person.id_person ,address.id_address, " +
                "address.street_address, address.number_address, " +
                "address.cep_address, address.neighborhood_address " +
                "FROM supplier " +
                "INNER JOIN person ON person.id_person = supplier.id_person " +
                "INNER JOIN address ON address.id_address = person.id_address";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                suppliers.add(SupplierDAO.createInstance(rs));
            }
        }
        catch (SQLException sqlE) {
            Logger.getLogger(SupplierDAO.class.getName()).log( Level.SEVERE, null, sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return suppliers;
    }

    public static Supplier createInstance (ResultSet result) {

        Supplier supplier = new Supplier ();

        try {
            supplier.setId(result.getInt("id_person"));
            supplier.setNamePerson(result.getString("name_person"));
            supplier.setAddress( AddressDAO.createInstance(result));
            supplier.setCNPJ( result.getString( "cnpj_supplier" ) );
            supplier.setStatus(result.getBoolean("status_supplier"));
            supplier.setPhones(PhoneDAO.load(supplier));
        }
        catch (SQLException sqlE) {
            Logger.getLogger(SupplierDAO.class.getName()).log( Level.SEVERE, null, sqlE);
        }

        return supplier;
    }
}
