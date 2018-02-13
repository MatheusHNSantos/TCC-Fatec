/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.address;

import model.dao.DAO;
import model.entity.address.Address;
import util.connection.ConnectionFactory;
import util.dialogs.FxDialogs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author FelipeMantoan
 */
public class AddressDAO implements DAO {

    public static int LAST_ID_INSERT = -1;

    /**
     * Este método insere um endereço
     *
     * @param address Address
     * @return boolean
     */
    public static boolean create(Address address) {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "INSERT INTO address (street_address, number_address, neighborhood_address, cep_address) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, address.getStreet());
            stmt.setInt(2, address.getNumber());
            stmt.setString(3, address.getNeighborhood());
            stmt.setString(4, address.getCep());
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            rs.next();
            LAST_ID_INSERT = rs.getInt(1);
            return true;
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao inserir um Endereço.","Class: "+ AddressDAO.class + " - " + sqlE.getMessage(), sqlE);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return false;
    }

    /**
     * Este método atualiza todos os dados de um endereço pré selecionado
     *
     * @param address Address
     * @return boolean
     */
    public static boolean update(Address address) {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "UPDATE address SET " +
                "street_address = ?, number_address = ?, neighborhood_address = ?, " +
                "cep_address = ? WHERE id_address = ?";

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, address.getStreet());
            stmt.setInt(2, address.getNumber());
            stmt.setString(3, address.getNeighborhood());
            stmt.setString(4, address.getCep());
            stmt.setInt(5, address.getId());
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao atualizar o Endereço.","Class: "+ AddressDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }

        return false;
    }

    /**
     * Esta função seleciona apenas 1 endereço por indice
     * @param id int
     * @return Address
     */
    public static Address load(int id) {
        Connection conn = ConnectionFactory.getConnection();

        String sql = "SELECT * FROM address WHERE id_address = ? LIMIT 1";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Address address = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.next();
            address = AddressDAO.createInstance(rs);
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao selecionar um Endereço.","Class: "+ AddressDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return address;
    }

    /**
     * Este método faz o load de todos os endereços e cria uma lista contendo as classes.
     *
     * @return ArrayList<Address>
     */
    public static ArrayList<Address> loadAll() {

        ArrayList<Address> addresses = new ArrayList<>();

        Connection conn = ConnectionFactory.getConnection();
        String sql = "SELECT * FROM address";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {
                addresses.add(AddressDAO.createInstance(rs));
            }
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao selecionar endereços.","Class: "+ AddressDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return addresses;
    }

    /**
     * Este método cria uma instância da classe Address.
     *
     * @param result
     * @return Address
     */
    public static Address createInstance(ResultSet result) {

        Address address = null;

        try {
            address = new Address();
            address.setId(result.getInt("id_address"));
            address.setStreet(result.getString("street_address"));
            address.setNumber(result.getInt("number_address"));
            address.setNeighborhood(result.getString("neighborhood_address"));
            address.setCep(result.getString("cep_address"));
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao criar instância.","Class: "+ AddressDAO.class + " - " + sqlE.getMessage(), sqlE);
        }

        return address;
    }
}
