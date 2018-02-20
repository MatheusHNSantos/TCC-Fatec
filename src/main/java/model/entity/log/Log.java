package model.entity.log;

import controller.dashboard.DashboardController;
import model.entity.person.employee.Employee;
import model.entity.person.user.User;
import util.Calendar.CalendarUtil;
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
    private User user;

    /*public interface ResetLog {
        public void resetLog(Log log);
    }*/

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
            this.setUser(new User(this.getIdUser()));
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
            stmt = con.prepareStatement("SELECT id_log FROM log ORDER BY id_log DESC");
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

    public static ArrayList<Log> readByDate(String date){

        ArrayList<Log> logList = new ArrayList<>();
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            stmt = con.prepareStatement("SELECT id_log FROM log where log_date = ? ORDER BY id_log DESC");
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while(rs.next()){
                Log log = new Log(rs.getInt("id_log"));
                logList.add(log);
            }
        } catch (SQLException ex) {
            FxDialogs.showException("Erro de Leitura!","class: Log" + " - " + ex.getMessage(),ex);
        }
        finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return logList;
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

    public static void gerarLog(String text){
        Log log = new Log();
        log.setIdUser(DashboardController.getUser().getIdEmployee());
        log.setLogDate(CalendarUtil.getCurrentDateBR());
        log.setUserAction(text + " as " + CalendarUtil.getCurrentHourBR());
        log.create();
        log.setUser(new User(log.getIdUser()));

       /* ResetLog rl = DashboardController.dashboardControllerReference;
        rl.resetLog(log);*/

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
} // END class log

