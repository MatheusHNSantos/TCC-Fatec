package model.dao.person;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import model.dao.DAO;
import model.entity.person.Employee;
import model.entity.person.User;
import util.connection.ConnectionFactory;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Matheus Henrique
 */
public class UserDAO implements DAO{

    public static int LAST_ID_INSERT = -1;

    public static boolean login(String login, String pass) {

        Connection con = ConnectionFactory.getConnection();

        String sql = "SELECT * FROM user WHERE login_user = ? AND password_user = ? AND status_user = true LIMIT 1";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, pass);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            }

        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao verificar login: " + ex.getMessage());
        }
        finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return false;
    }

    public static boolean create(User user) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        String sql = "INSERT INTO user (login_user, password_user, status_user, id_employee) VALUES (?, ?, ?, ?)";
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            stmt.setBoolean(3, user.isStatus());
            stmt.setInt(4, user.getEmployee().getIdEmployee());

            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            rs.next();
            LAST_ID_INSERT = rs.getInt(1);
            return true;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao Salvar: " + ex.getMessage());
        }
        finally {
            ConnectionFactory.closeConnection(con, stmt, rs);

        }

        return false;

    }


    public static User load(String login) {
        Connection con = ConnectionFactory.getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        User user = null;

        String sql = "SELECT id_user, user.login_user, user.password_user, user.status_user, user.level_user, " +
                "employee.id_employee, employee.role_employee, " +
                "employee.rg_employee, employee.cpf_employee, employee.status_employee, " +
                "employee.id_person, employee.role_employee, " +
                "person.id_person, person.name_person, " +
                "address.id_address, address.street_address, " +
                "address.number_address, address.cep_address, address.neighborhood_address " +
                "FROM user " +
                "INNER JOIN employee ON employee.id_employee = user.id_employee " +
                "INNER JOIN person ON person.id_person = employee.id_person " +
                "INNER JOIN address ON address.id_address = person.id_address " +
                "WHERE login_user = ? LIMIT 1";

        try {

            stmt = con.prepareStatement(sql);
            stmt.setString(1, login);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return UserDAO.createInstance(rs);
            }

        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao consultar: " + ex.getMessage());
        }
        finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return user;
    }

    public static User load(Employee employee) {
        Connection con = ConnectionFactory.getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        User user = null;

        String sql = "SELECT user.id_user, user.login_user, user.password_user, user.status_user, user.level_user, " +
                "employee.id_employee, employee.role_employee, " +
                "employee.rg_employee, employee.cpf_employee, employee.status_employee, " +
                "employee.id_person, employee.role_employee, " +
                "person.id_person, person.name_person, " +
                "address.id_address, address.street_address, " +
                "address.number_address, address.cep_address, address.neighborhood_address " +
                "FROM user " +
                "INNER JOIN employee ON employee.id_employee = user.id_employee " +
                "INNER JOIN person ON person.id_person = employee.id_person " +
                "INNER JOIN address ON address.id_address = person.id_address " +
                "WHERE employee.id_employee = ? LIMIT 1";

        try {

            stmt = con.prepareStatement(sql);
            stmt.setInt(1, employee.getIdEmployee());
            rs = stmt.executeQuery();
            if (rs.next()) {
                return UserDAO.createInstance(rs);
            }

        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao consultar: " + ex.getMessage());
        }
        finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return user;
    }

    public static User load(int idUser) {
        Connection con = ConnectionFactory.getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;

        User user = null;

        String sql = "SELECT id_user, user.login_user, user.password_user, user.status_user, user.level_user, " +
                "employee.id_employee, employee.role_employee, " +
                "employee.rg_employee, employee.cpf_employee, employee.status_employee, " +
                "employee.id_person, employee.role_employee, " +
                "person.id_person, person.name_person, " +
                "address.id_address, address.street_address, " +
                "address.number_address, address.cep_address, address.neighborhood_address " +
                "FROM user " +
                "INNER JOIN employee ON employee.id_employee = user.id_employee " +
                "INNER JOIN person ON person.id_person = employee.id_person " +
                "INNER JOIN address ON address.id_address = person.id_address " +
                "WHERE id_user = ? LIMIT 1";

        try {

            stmt = con.prepareStatement(sql);
            stmt.setInt(1, idUser);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return UserDAO.createInstance(rs);
            }
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao consultar: " + ex.getMessage());
        }
        finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return user;
    }

    public static ArrayList<User> loadAll() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<User> users = new ArrayList();

        String sql = "SELECT id_user, user.login_user, user.password_user, user.status_user, user.level_user, " +
                "employee.id_employee, employee.role_employee, " +
                "employee.rg_employee, employee.cpf_employee, employee.status_employee, " +
                "employee.id_person, employee.role_employee, " +
                "person.id_person, person.name_person, " +
                "address.id_address, address.street_address, " +
                "address.number_address, address.cep_address, address.neighborhood_address " +
                "FROM user " +
                "INNER JOIN employee ON employee.id_employee = user.id_employee " +
                "INNER JOIN person ON person.id_person = employee.id_person " +
                "INNER JOIN address ON address.id_address = person.id_address";

        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while(rs.next()) {
                users.add(UserDAO.createInstance(rs));
            }
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao consultar: " + ex.getMessage());
        }
        finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return users;
    }

    public static boolean update(User user) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        String sql = "UPDATE user SET login_user = ?, password_user = ?, status_user = ? WHERE id_user= ?";

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            stmt.setBoolean(3, user.isStatus());
            stmt.setInt(4, user.getId());
            
            stmt.executeUpdate();
            
            return true;
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Atualizar: " + ex.getMessage());
            return false;
        }
        finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
      
    }


    public static User createInstance (ResultSet result) {

        User user = new User();

        try {
            user.setId(result.getInt("id_user"));
            user.setLogin(result.getString( "login_user" ) );
            user.setPassword( result.getString( "password_user" ) );
            user.setStatus( result.getBoolean( "status_user" ) );
            user.setLevel(result.getInt("level_user"));
            user.setEmployee(EmployeeDAO.createInstance(result));
        }
        catch (SQLException sqlE) {
            JOptionPane.showMessageDialog(null, "Deu ruim aqui: " + sqlE.getMessage());
        }

        return user;
    }

}
