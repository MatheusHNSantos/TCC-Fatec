/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import controller.Controller;
import controller.dashboard.DashboardController;
import controller.login.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.entity.log.Log;

/**
 *
 * @author SAMSUNG
 */
public class Main extends Application {

    @Override
    public void start(Stage PrimaryStage) {

        try {
           LoginController.loader().show();


        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    @Override
    public void stop() throws Exception {
        if(DashboardController.getUser() != null){
            Log.gerarLog(DashboardController.getUser().getLogin() + " saiu do sistema ");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(app.Main.class, args);
    }
}