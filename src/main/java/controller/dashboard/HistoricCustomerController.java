package controller.dashboard;

import com.jfoenix.controls.JFXButton;
import com.sun.istack.internal.NotNull;
import controller.Controller;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.entity.person.Person;
import model.entity.product.Product;
import model.entity.sale.ItemsSale;
import model.entity.sale.Sale;

import java.beans.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HistoricCustomerController implements Initializable{

    public static final String path = "historicCustomer.fxml";
    public static final String title = "Histórico de Compras do Cliente";

    private Person person;

    @FXML    private Label lbl_nameCustomerHistoric; //TextField de campo para função de funcionario
    @FXML    private JFXButton btn_closeHistoric; //Botão Editar

    @FXML    private TableView<Sale> tview_salesHistoric; //Tabela de ingredientes
    @FXML    private TableView<Product> tview_productsHistoric; //Tabela de produtos

    @FXML    private TableColumn <Product,String> productName;
    @FXML    private TableColumn <Product,String> productPrice;

    @FXML    private TableColumn <Sale,Integer> saleId;
    @FXML    private TableColumn <Sale,String> saleDate;
    @FXML    private TableColumn <Sale,Float> saleValue;

    private ObservableList<Product> dataObservableProductHistoric;

    private ObservableList<Sale> dataObservableSaleHistoric;

    @Override
    public void initialize(URL url, ResourceBundle rb){


        //region TableView Product Historic
        dataObservableProductHistoric = FXCollections.observableArrayList();
        productName.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("finalPriceProduct"));
        tview_productsHistoric.setItems(dataObservableProductHistoric);
        //endregion

        //region TableView Sale Historic
        dataObservableSaleHistoric = FXCollections.observableArrayList();
        saleId.setCellValueFactory(new PropertyValueFactory<>("idSale"));
        saleDate.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        saleValue.setCellValueFactory(new PropertyValueFactory<>("saleTotal"));
        tview_salesHistoric.setItems(dataObservableSaleHistoric);

        tview_salesHistoric.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sale>() {
            @Override
            public void changed(ObservableValue<? extends Sale> observableValue, Sale sale, Sale t1) {
                if(!tview_salesHistoric.getSelectionModel().isEmpty()){
                    dataObservableProductHistoric.clear();
                    dataObservableProductHistoric.addAll(ItemsSale.readAllProduct(tview_salesHistoric.getSelectionModel().getSelectedItem().getIdSale()));
                }
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Sale.readSaleByPersonId(person.getIdPerson()).forEach(sale -> dataObservableSaleHistoric.addAll(sale));
            }
        });

        //endregion

        //region Events
        btn_closeHistoric.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Controller.closeApplication(event);
            }
        });
        //endregion


    }

    public void setPerson(Person person){
        this.person = person;
        lbl_nameCustomerHistoric.setText(person.getNamePerson());
    }

    public static Stage loader(javafx.event.EventHandler<WindowEvent> onClose, javafx.event.EventHandler<WindowEvent> onShow) throws IOException {
        return Controller.loaderModal(HistoricCustomerController.class, StageStyle.DECORATED,path, title, onShow, onClose);
    }

}
