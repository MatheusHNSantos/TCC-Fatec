package model.entity.voucher;

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
public class Voucher {
    private int idVoucher;
    private int idSale;
    private String unifiedCode;
    private String sequentialCode;
    private float totalOrder;
    private String dateOrder;
    private String hourOrder;
    private int idCustomer;
    private String nameCustomer;
    private String addressCustomer;
    private String phoneCustomer;
    private static int LAST_ID_INSERT = -1;

    public Voucher(){
    }

    public Voucher(int idVoucher){
        this.setIdVoucher(idVoucher);
        this.load();
    }

    private void load(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            stmt = con.prepareStatement("SELECT * FROM voucher WHERE id_voucher = ?");
            stmt.setInt(1, this.getIdVoucher());
            rs = stmt.executeQuery();
            rs.next();
            this.setIdSale(rs.getInt("id_sale"));
            this.setUnifiedCode(rs.getString("unified_code"));
            this.setSequentialCode(rs.getString("sequential_code"));
            this.setTotalOrder(rs.getFloat("total_order"));
            this.setDateOrder(rs.getString("date_order"));
            this.setHourOrder(rs.getString("hour_order"));
            this.setIdCustomer(rs.getInt("id_customer"));
            this.setNameCustomer(rs.getString("name_customer"));
            this.setAddressCustomer(rs.getString("address_customer"));
            this.setPhoneCustomer(rs.getString("phone_customer"));
        } catch (SQLException ex) {
            FxDialogs.showException("Erro de Leitura!",getClass().getSimpleName()+ " - " + ex.getMessage(),ex);
        }
        finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
    }

    public static ArrayList<Voucher> readAll(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Voucher> vouchersList = new ArrayList<>();
        try{
            stmt = con.prepareStatement("SELECT id_voucher FROM voucher");
            rs = stmt.executeQuery();
            while(rs.next()){
                Voucher voucher = new Voucher(rs.getInt("id_voucher"));
                vouchersList.add(voucher);
            }
        } catch (SQLException ex) {
            FxDialogs.showException("Erro de Leitura!", "Voucher.java" + " - " + ex.getMessage(), ex);
        }
        finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return vouchersList;
    }

    public void save(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement("UPDATE voucher SET  id_sale = ?, unified_code = ?, sequential_code = ?, total_order = ?, date_order = ?, hour_order = ?, id_customer = ?, name_customer = ?, address_customer = ?, phone_customer = ? WHERE id_voucher= ?");
            stmt.setInt(1, this.getIdSale());
            stmt.setString(2, this.getUnifiedCode());
            stmt.setString(3, this.getSequentialCode());
            stmt.setFloat(4, this.getTotalOrder());
            stmt.setString(5, this.getDateOrder());
            stmt.setString(6, this.getHourOrder());
            stmt.setInt(7, this.getIdCustomer());
            stmt.setString(8, this.getNameCustomer());
            stmt.setString(9, this.getAddressCustomer());
            stmt.setString(10, this.getPhoneCustomer());
            stmt.setInt(11, this.getIdVoucher());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            FxDialogs.showException("Erro de Atualização!", getClass().getSimpleName() + " - " + ex.getMessage(), ex);
        }
        finally{
            ConnectionFactory.closeConnection(con,stmt);
        }
    }

    public void create(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement("INSERT INTO voucher (id_sale,unified_code,sequential_code,total_order,date_order,hour_order,id_customer,name_customer,address_customer,phone_customer) VALUES (?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, this.getIdSale());
            stmt.setString(2, this.getUnifiedCode());
            stmt.setString(3, this.getSequentialCode());
            stmt.setFloat(4, this.getTotalOrder());
            stmt.setString(5, this.getDateOrder());
            stmt.setString(6, this.getHourOrder());
            stmt.setInt(7, this.getIdCustomer());
            stmt.setString(8, this.getNameCustomer());
            stmt.setString(9, this.getAddressCustomer());
            stmt.setString(10, this.getPhoneCustomer());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            LAST_ID_INSERT = rs.next() ? rs.getInt(1) : -1;
            this.setIdVoucher(LAST_ID_INSERT);
        } catch (SQLException ex) {
            FxDialogs.showException("Erro de Gravação! ", getClass().getSimpleName() + " - " + ex.getMessage(), ex);
        }
        finally{
            ConnectionFactory.closeConnection(con,stmt);
        }
    }

    public void delete(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement("DELETE FROM voucher WHERE id_voucher = ?");
            stmt.setInt(1, this.getIdVoucher());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            FxDialogs.showException("Erro de Exclusão!", getClass().getSimpleName() + " - " + ex.getMessage(), ex);
        }
        finally{
            ConnectionFactory.closeConnection(con,stmt);
        }
    }

    public void setIdVoucher(int idVoucher){
        this.idVoucher = idVoucher;
    }

    public int getIdVoucher(){
        return this.idVoucher;
    }

    public void setIdSale(int idSale){
        this.idSale = idSale;
    }

    public int getIdSale(){
        return this.idSale;
    }

    public void setUnifiedCode(String unifiedCode){
        this.unifiedCode = unifiedCode;
    }

    public String getUnifiedCode(){
        return this.unifiedCode;
    }

    public void setSequentialCode(String sequentialCode){
        this.sequentialCode = sequentialCode;
    }

    public String getSequentialCode(){
        return this.sequentialCode;
    }

    public void setTotalOrder(float totalOrder){
        this.totalOrder = totalOrder;
    }

    public float getTotalOrder(){
        return this.totalOrder;
    }

    public void setDateOrder(String dateOrder){
        this.dateOrder = dateOrder;
    }

    public String getDateOrder(){
        return this.dateOrder;
    }

    public void setHourOrder(String hourOrder){
        this.hourOrder = hourOrder;
    }

    public String getHourOrder(){
        return this.hourOrder;
    }

    public void setIdCustomer(int idCustomer){
        this.idCustomer = idCustomer;
    }

    public int getIdCustomer(){
        return this.idCustomer;
    }

    public void setNameCustomer(String nameCustomer){
        this.nameCustomer = nameCustomer;
    }

    public String getNameCustomer(){
        return this.nameCustomer;
    }

    public void setAddressCustomer(String addressCustomer){
        this.addressCustomer = addressCustomer;
    }

    public String getAddressCustomer(){
        return this.addressCustomer;
    }

    public void setPhoneCustomer(String phoneCustomer){
        this.phoneCustomer = phoneCustomer;
    }

    public String getPhoneCustomer(){
        return this.phoneCustomer;
    }

} // END class voucher