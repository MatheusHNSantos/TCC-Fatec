package controller.dashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entity.person.Customer;
import model.entity.person.Employee;
import model.entity.person.User;
import model.entity.product.Product;
import model.entity.sale.ItemsSale;
import model.entity.sale.Sale;
import java.net.URL;
import java.util.ResourceBundle;

public class SaleDetailController implements Initializable {

    public static final String path = "saleDetail.fxml";
    public static final String title = "Detalhes da Venda";

    private Sale sale;

    @FXML
    private Label lbl_id; //TextField de campo para id venda
    @FXML
    private Label lbl_customer; //TextField de campo para cliente
    @FXML
    private Label lbl_user; //TextField de campo para usuario
    @FXML
    private Label lbl_date; //TextField de campo para data
    @FXML
    private Label lbl_time; //TextField de campo para hora
    @FXML
    private Label lbl_price; //TextField de campo para preço
    @FXML
    private TableView<Product> tview_saleDetail; //Tabela
    @FXML
    private TableColumn<Product, String> nameProduct;
    @FXML
    private TableColumn<Product, String> priceProduct;

    private ObservableList<Product> dataObservableProducts;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        dataObservableProducts = FXCollections.observableArrayList();
        nameProduct.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
        priceProduct.setCellValueFactory(new PropertyValueFactory<>("finalPriceProduct"));
        tview_saleDetail.setItems(dataObservableProducts);
    }

    public void setSale(Sale sale) {
        this.sale = sale;
        setAllData();
    }

    public void setAllData() {
        dataObservableProducts.addAll(ItemsSale.readAllProduct(sale.getIdSale()));
        lbl_id.setText(String.valueOf(sale.getIdSale()));

        User user = User.load(Employee.load(sale.getIdUser()));

        lbl_customer.setText(String.valueOf(Customer.load(sale.getIdCustomer()).getNamePerson()));
        lbl_user.setText(String.valueOf(user.getLogin()));
        lbl_date.setText(sale.getSaleDate());
        lbl_time.setText(sale.getSaleTime());
        lbl_price.setText(String.valueOf(sale.getSaleTotal()));
    }
}
