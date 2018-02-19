package controller.dashboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import jeanderson.br.util.MaskFormatter;
import model.entity.person.Person;
import model.entity.person.customer.Customer;
import model.entity.product.Product;
import model.entity.sale.ItemsSale;
import model.entity.sale.Sale;
import util.Calendar.CalendarUtil;
import util.MaskField.MaskFieldUtil;
import util.dialogs.FxDialogs;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FinishSaleController implements Initializable {

    public static final String path = "finishSale.fxml";
    public static final String title = "Finalizar Pedido";

    @FXML
    AnchorPane mainAnchor;

    @FXML
    JFXComboBox<String> comboBoxPayMethod;

    @FXML
    JFXTextField txt_paymentAmmount;

    @FXML
    JFXButton finishSaleOnDialog;

    @FXML
    JFXButton btn_cancelSale;

    @FXML
    private Label lbl_totalPriceOnFinishSaleDialog;

    @FXML Label txt_transshipment;

    private Person person;

    private float price;

    private ArrayList<Product> products;

    private Executor executor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

        finishSaleOnDialog.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                saveSale();
                saveItensSale(event);

            }
        });

        btn_cancelSale.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Controller.closeApplication(event);
            }
        });

        txt_paymentAmmount.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if(!txt_paymentAmmount.getText().equals("") && event.getCode().toString().equals(".")) return;

                if(!checkNumeric(event.getCharacter())){
                    event.consume();
                }
            }
        });

        txt_paymentAmmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if(!newValue.equals("") && Float.parseFloat(newValue) > price){
                    final float finalPrice = Float.parseFloat(newValue) - price;
                    txt_transshipment.setText(String.valueOf(finalPrice));
                }else {
                    txt_transshipment.setText("0.0");
                }
            }
        });

    }

    private void saveSale() {

        Sale sale = new Sale();
        if (person != null) {
            Customer customer = new Customer(person.getIdPerson());
            sale.setIdCustomer(customer.getIdPerson());
        }
        sale.setSaleDate(CalendarUtil.getCurrentDateBR());
        sale.setSaleTime(CalendarUtil.getCurrentHourBR());
        sale.setSaleTotal(Float.parseFloat(lbl_totalPriceOnFinishSaleDialog.getText()));
        sale.setSaleTimeEstimate(0);
        sale.setIdUser(DashboardController.getUser().getIdEmployee());
        sale.Create();

    }

    private void saveItensSale(final Event eventOutTask) {


        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                if (Sale.LAST_ID_SALE != -1) {

                    products.forEach(product -> {
                        ItemsSale itemsSale = new ItemsSale();
                        itemsSale.setIdSale(Sale.LAST_ID_SALE);
                        itemsSale.setIdProduct(product.getIdProduct());
                        itemsSale.Create();
                    });

                    return true;
                } else {
                    return false;
                }
            }
        };

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if(task.getValue()){
                    Controller.closeApplication(eventOutTask);
                }else{
                    FxDialogs.showWarning("Erro ao salvar", "Senta e chora");
                }
            }
        });

        task.runningProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Controller.getModalScreenStage().getScene().setCursor(Cursor.WAIT);
                    mainAnchor.setDisable(true);
                }else{
                    Controller.getModalScreenStage().getScene().setCursor(Cursor.DEFAULT);
                }
            }
        });

        executor.execute(task);

    }

    public void setProducts(ArrayList arrayList) {
        products = arrayList;
    }

    public void setPrice(String price) {
        lbl_totalPriceOnFinishSaleDialog.setText(price);
        this.price = Float.parseFloat(price);
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setAllComponents(String price, Person person, ArrayList arrayList) {
        products = arrayList;
        this.lbl_totalPriceOnFinishSaleDialog.setText(price);
        this.person = person;
        this.price = Float.parseFloat(price);
    }

    public boolean checkNumeric(String value)    {
        String number=value.replaceAll("\\s+","");
        for(int j = 0 ; j<number.length();j++){
            if(!(((int)number.charAt(j)>=47 && (int)number.charAt(j)<=57)))
            {
                return false;
            }
        }
        return true;
    }
}
