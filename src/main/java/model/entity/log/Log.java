package model.entity.log;

import util.connection.ConnectionFactory;
import util.dialogs.FxDialogs;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Matheus Henrique
 */
public class Log {
    private int idLog;
    private int idUser;
    private String logDate;
    private String userAction;
    private static int LAST_ID_INSERT = -1;

    public Log(){
    }

    public Log(int idLog){
        this.setIdLog(idLog);
        this.load();
    }

    private void load(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            stmt = con.prepareStatement("SELECT * FROM log WHERE id_log = ?");
            stmt.setInt(1, this.getIdLog());
            rs = stmt.executeQuery();
            rs.next();
            this.setIdUser(rs.getInt("id_user"));
            this.setLogDate(rs.getString("log_date"));
            this.setUserAction(rs.getString("user_action"));
        } catch (SQLException ex) {
            FxDialogs.showException("Erro de Leitura!", getClass().getSimpleName() + " - " + ex.getMessage(), ex);
        }
        finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
    }

    public static ArrayList<Log> readAll(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Log> logsList = new ArrayList<>();
        try{
            stmt = con.prepareStatement("SELECT id_log FROM log");
            rs = stmt.executeQuery();
            while(rs.next()){
                Log log = new Log(rs.getInt("id_log"));
                logsList.add(log);
            }
        } catch (SQLException ex) {
            FxDialogs.showException("Erro de Leitura!", "Log.class" + " - " + ex.getMessage(), ex);
        }
        finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return logsList;
    }

    public void save(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement("UPDATE log SET  id_user = ?, log_date = ?, user_action = ? WHERE id_log= ?");
            stmt.setInt(1, this.getIdUser());
            stmt.setString(2, this.getLogDate());
            stmt.setString(3, this.getUserAction());
            stmt.setInt(4, this.getIdLog());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            FxDialogs.showException("Erro de Atualização!", getClass().getSimpleName() + " - " + ex.getMessage(), ex);
        }
        finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public void create(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement("INSERT INTO log (id_user,log_date,user_action) VALUES (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, this.getIdUser());
            stmt.setString(2, this.getLogDate());
            stmt.setString(3, this.getUserAction());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            LAST_ID_INSERT = rs.next() ? rs.getInt(1) : -1;
            this.setIdLog(LAST_ID_INSERT);
        } catch (SQLException ex) {
            FxDialogs.showException("Erro de Gravação! ", getClass().getSimpleName() + " - " + ex.getMessage(), ex);
        }
        finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public void delete(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement("DELETE FROM log WHERE id_log = ?");
            stmt.setInt(1, this.getIdLog());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            FxDialogs.showException("Erro de Exclusão!", getClass().getSimpleName() + " - " + ex.getMessage(), ex);
        }
        finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public void setIdLog(int idLog){
        this.idLog = idLog;
    }

    public int getIdLog(){
        return this.idLog;
    }

    public void setIdUser(int idUser){
        this.idUser = idUser;
    }

    public int getIdUser(){
        return this.idUser;
    }

    public void setLogDate(String logDate){
        this.logDate = logDate;
    }

    public String getLogDate(){
        return this.logDate;
    }

    public void setUserAction(String userAction){
        this.userAction = userAction;
    }

    public String getUserAction(){
        return this.userAction;
    }

} // END class log

