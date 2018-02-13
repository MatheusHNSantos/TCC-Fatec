package controller.dashboard;

import com.jfoenix.controls.JFXButton;
import controller.Controller;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import model.entity.person.Person;
import model.entity.person.Customer;
import model.entity.product.Product;
import model.entity.sale.ItemsSale;
import model.entity.sale.Sale;
import util.Calendar.CalendarUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FinishSaleController implements Initializable {

    public static final String path = "finishSale.fxml";
    public static final String title = "Finalizar Pedido";

    @FXML    JFXButton finishSaleOnDialog;

    @FXML    JFXButton btn_cancelSale;

    @FXML private Label lbl_totalPriceOnFinishSaleDialog;

    private Person person;

    private ArrayList<Product> products;

    @Override
    public void initialize(URL url, ResourceBundle rb){

        finishSaleOnDialog.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                saveSale();
                saveItensSale();

                Controller.closeApplication(event);

            }
        });

        btn_cancelSale.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Controller.closeApplication(event);
            }
        });
    }

    private void saveSale(){

        Sale sale = new Sale();
        if(person != null){
            Customer customer = Customer.load(person.getId());
            sale.setIdCustomer(customer.getId());
        }
        sale.setSaleDate(CalendarUtil.getCurrentDateBR());
        sale.setSaleTime(CalendarUtil.getCurrentHourBR());
        sale.setSaleTotal(Float.parseFloat(lbl_totalPriceOnFinishSaleDialog.getText()));
        sale.setSaleTimeEstimate(0);
        sale.setIdUser(DashboardController.getUser().getEmployee().getIdEmployee());
        sale.Create();

    }

    private void saveItensSale(){

        if(Sale.LAST_ID_SALE != -1){
            products.forEach(product -> {
                ItemsSale itemsSale = new ItemsSale();
                itemsSale.setIdSale(Sale.LAST_ID_SALE);
                itemsSale.setIdProduct(product.getIdProduct());
                itemsSale.Create();
            });

            Sale.LAST_ID_SALE = -1;

        }
    }

    public void setProducts(ArrayList arrayList){
        products = arrayList;
    }

    public void setPrice(String price){
        lbl_totalPriceOnFinishSaleDialog.setText(price);
    }

    public void setPerson(Person person){
        this.person = person;
    }

    public void setAllComponents(String price, Person person, ArrayList arrayList){
        products = arrayList;
        this.lbl_totalPriceOnFinishSaleDialog.setText(price);
        this.person = person;
    }
}
