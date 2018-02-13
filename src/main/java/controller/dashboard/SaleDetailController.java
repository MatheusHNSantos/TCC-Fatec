package controller.dashboard;

import controller.Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entity.person.Person;
import model.entity.person.user.User;
import model.entity.product.Product;
import model.entity.sale.ItemsSale;
import model.entity.sale.Sale;

import java.io.IOException;
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

        System.out.println(sale.getIdCustomer());

        lbl_customer.setText(String.valueOf(new Person(sale.getIdCustomer()).getNamePerson()));
        lbl_user.setText(String.valueOf(new User(sale.getIdUser()).getLogin()));
        lbl_date.setText(sale.getSaleDate());
        lbl_time.setText(sale.getSaleTime());
        lbl_price.setText(String.valueOf(sale.getSaleTotal()));
    }
}
