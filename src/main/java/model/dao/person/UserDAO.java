package model.dao.person;

import model.dao.DAO;
import model.entity.person.Employee;
import model.entity.person.User;
import util.connection.ConnectionFactory;
import util.dialogs.FxDialogs;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author FelipeMantoan
 */
public class UserDAO implements DAO{

    public static int LAST_ID_INSERT = -1;


    /**
     * Efetua o login e retorna um valor booelano.
     *
     * @param login
     * @param pass
     * @return
     */
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
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao efetuar o login.","Class: "+ UserDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return false;
    }

    /**
     * Insere um usuário na tabela.
     *
     * @param user
     * @return
     */
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
            // Faz uso o ultimo ID inserido.
            LAST_ID_INSERT = rs.getInt(1);

            return true;
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao criar usuário.","Class: "+ UserDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(con, stmt, rs);

        }

        return false;

    }

    /**
     * Carrega um usuário por login e pode/deve ser usada para pesquisa ou validação.
     * @param login
     * @return
     */
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
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao carregar usuário pelo login.","Class: "+ UserDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return user;
    }

    /**
     * Retorna um usuário por empregado.
     * @param employee
     * @return
     */
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
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao selecionar usuário pelo ID do empregado.","Class: "+ UserDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return user;
    }

    /**
     * Carrega um usuário por id.
     * @param idUser
     * @return
     */
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
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao criar usuário pelo ID.","Class: "+ UserDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return user;
    }

    /**
     * Carrega todos os usuários.
     * @return
     */
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
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao listar usuários.","Class: "+ UserDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return users;
    }

    /**
     * Atualiza um usuário com base na classe.
     * @param user
     * @return
     */
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
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao atualizar usuário.","Class: "+ UserDAO.class + " - " + sqlE.getMessage(), sqlE);
        }
        finally{
            ConnectionFactory.closeConnection(con, stmt);
        }

        return false;
    }

    /**
     * Cria a instância de um usuário mas não fecha a conexão.
     * @param result
     * @return
     */
    public static User createInstance (ResultSet result) {

        User user = null;

        try {
            user = new User();
            user.setId(result.getInt("id_user"));
            user.setLogin(result.getString( "login_user" ) );
            user.setPassword( result.getString( "password_user" ) );
            user.setStatus( result.getBoolean( "status_user" ) );
            user.setLevel(result.getInt("level_user"));

            // Chama a classe employeeDao para criar a instância
            user.setEmployee(EmployeeDAO.createInstance(result));
        }
        catch (SQLException sqlE) {
            FxDialogs.showException("Falha ao criar instância de usuário.","Class: "+ UserDAO.class + " - " + sqlE.getMessage(), sqlE);
        }

        return user;
    }

}
