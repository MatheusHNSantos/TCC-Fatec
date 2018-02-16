/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.person;

import model.dao.DAO;
import model.entity.person.Person;
import util.connection.ConnectionFactory;
import util.dialogs.FxDialogs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Felipemantoan
 */
public abstract class PersonDAO implements DAO {

    public static int LAST_ID_INSERT = -1;

    /**
     *
     * @param person
     * @return
     */
    public static boolean create(Person person) {

        Connection conn = ConnectionFactory.getConnection();
        String sql = "INSERT INTO person (id_address, name_person) VALUES (?, ?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, person.getAddress().getId());
            stmt.setString(2, person.getNamePerson());
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            rs.next();
            LAST_ID_INSERT = rs.getInt(1);
            return true;
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao criar pessoa.","Class: "+ PersonDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return false;
    }

    /**
     *
     * @param person
     * @return
     */
    public static boolean update(Person person) {

        Connection conn = ConnectionFactory.getConnection();
        String sql = "UPDATE person SET id_address = ?, name_person = ? WHERE id_person = ?";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, person.getAddress().getId());
            stmt.setString(2, person.getNamePerson());
            stmt.setInt(3, person.getId());

            stmt.execute();
            return true;
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao atualizar pessoa.","Class: "+ PersonDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }

        return false;
    }
}
