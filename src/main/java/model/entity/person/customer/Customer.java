package model.entity.person.customer;

import model.entity.person.Person;
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
public class Customer extends Person {

	public Customer(int idPerson){
        super(idPerson);
        this.Load();
	}

	public Customer(){

    }

	private void Load(){
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement("SELECT * FROM customer WHERE id_person = ?");
			stmt.setInt(1, this.getIdPerson());
			rs = stmt.executeQuery();
			rs.next();
        } catch (SQLException ex) {
			FxDialogs.showException("Erro de Leitura! - Load",getClass().getSimpleName()+ " - " + ex.getMessage(),ex);
		}
		finally{
			ConnectionFactory.closeConnection(con, stmt, rs);
		}
	}

	public static ArrayList<Customer> readByName(String name){
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Customer> customersList = new ArrayList<>();
		try{
			stmt = con.prepareStatement("SELECT CO.id_person, PE.name_person FROM customer CO, person PE WHERE PE.id_person = CO.id_person and PE.name_person LIKE ? ORDER BY CO.id_person ASC");
			stmt.setString(1, "%"+name+"%");
			rs = stmt.executeQuery();
			while(rs.next()){
				Customer customer = new Customer(rs.getInt("id_person"));
				customersList.add(customer);
			}
		} catch (SQLException ex) {
			FxDialogs.showException("Erro de Leitura! - ReadAll","class: Customer" + " - " + ex.getMessage(),ex);
		}
		finally{
			ConnectionFactory.closeConnection(con, stmt, rs);
		}
		return customersList;
	}

	public static ArrayList<Customer> readByPhone(String phone){
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Customer> customersList = new ArrayList<>();
		try{
			stmt = con.prepareStatement("SELECT CO.id_person, PH.phone FROM customer CO, phone PH WHERE CO.id_person = PH.id_person and PH.phone LIKE ? ORDER BY CO.id_person");
			stmt.setString(1, "%"+phone+"%");
			rs = stmt.executeQuery();
			while(rs.next()){
				Customer customer = new Customer(rs.getInt("id_person"));
				customersList.add(customer);
			}
		} catch (SQLException ex) {
			FxDialogs.showException("Erro de Leitura! - ReadAll","class: Customer" + " - " + ex.getMessage(),ex);
		}
		finally{
			ConnectionFactory.closeConnection(con, stmt, rs);
		}
		return customersList;
	}

    public static ArrayList<Customer> ReadAll(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Customer> customersList = new ArrayList<>();
        try{
            stmt = con.prepareStatement("SELECT id_person FROM customer");
            rs = stmt.executeQuery();
            while(rs.next()){
                Customer customer = new Customer(rs.getInt("id_person"));
                customersList.add(customer);
            }
        } catch (SQLException ex) {
            FxDialogs.showException("Erro de Leitura! - ReadAll","class: Customer" + " - " + ex.getMessage(),ex);
        }
        finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return customersList;
    }

	public void Create(){
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		try{
		    super.Create();
			stmt = con.prepareStatement("INSERT INTO customer (id_person) VALUE (?)");
			stmt.setInt(1, this.getIdPerson());
			stmt.executeUpdate();
		} catch (SQLException ex) {
			FxDialogs.showException("Erro de Gravação! " ,getClass().getSimpleName()+ " - " + ex.getMessage(),ex);
		}
		finally{
			ConnectionFactory.closeConnection(con,stmt);
		}
	}

} // END class customer

