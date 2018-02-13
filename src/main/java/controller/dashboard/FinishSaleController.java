package controller.dashboard;

import com.jfoenix.controls.JFXButton;
import controller.Controller;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FinishSaleController implements Initializable {

    private static final String path = "finishSale.fxml";
    private static final String title = "Finalizar Pedido";

    public static DashboardController dashboardController;

    @FXML    JFXButton finishSaleOnDialog;

    public interface OnClickFinishButton{
        void finishButton();
    }

    OnClickFinishButton onClickFinishButton;

    @FXML private Label lbl_totalPriceOnFinishSaleDialog;

    @Override
    public void initialize(URL url, ResourceBundle rb){

        finishSaleOnDialog.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onClickFinishButton = (OnClickFinishButton) dashboardController;
                onClickFinishButton.finishButton();

                Controller.closeApplication(event);

            }
        });



    }

    public static Stage loader() throws IOException {
        return Controller.loader(FinishSaleController.class,StageStyle.DECORATED,path,title);
    }

    public void setPrice(String price){
        this.lbl_totalPriceOnFinishSaleDialog.setText(price);
    }
}
