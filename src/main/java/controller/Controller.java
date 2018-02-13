/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.*;

import java.io.IOException;

/**
 *
 * @author SAMSUNG
 */
public class Controller {
    
    private static final String sPathControl = "fxml/";

    private static Class<? extends Window> controlledStage;

    //region Static Loader - For non interact Windows
    public static Stage loader(Class<?> Class, StageStyle style, String path, String title) throws IOException {
        Stage stage = createStageInstance(sPathControl + path, Class);
        Controller ls = new Controller();
        stage = ls.icons(stage);
        stage.initStyle(style);
        stage.setTitle(title);

        controlledStage = stage.getClass();
        return stage;
    }

    public Stage icons(Stage stage){
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/icon.jpg")));
        return stage;
    }

    public static Stage loaderModal(Class<?> Class, StageStyle style, String path, String title,
                                    EventHandler<WindowEvent> onShow, EventHandler<WindowEvent> onClose) throws IOException {

        Stage stage = createStageInstance(sPathControl + path, Class);
        Controller ls = new Controller();
        stage = ls.icons(stage);
        stage.initStyle(style);
        stage.setTitle(title);
        stage.setOnShowing(onShow);
        stage.setOnCloseRequest(onClose);
        controlledStage = stage.getClass();
        return stage;
    }

    public static Stage createStageInstance(String path, Class<?> Class) throws IOException {
        Parent root = FXMLLoader.load(Class.getClassLoader().getResource(path));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        return stage;
    }
    //endregion

    //region Object Loader
    public FXMLLoader fxmlLoaderContent(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/"+path));
        fxmlLoader.load();
        return fxmlLoader;
    }

    public Stage buildStage(FXMLLoader fxmlLoader, String title){
        Parent p = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/icon.jpg")));
        stage.setTitle(title);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        return stage;
    }
    //endregion

    //region Event Window
    public void closeEntireApplication() {
        Platform.exit();
    }

    public static void closeApplication(Event e) {
        Stage stage = (Stage) ((Node) e.getTarget()).getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public static void closeAppicationWithoutRequest(Event e){
        Stage stage = (Stage) ((Node) e.getTarget()).getScene().getWindow();
        stage.close();
    }

    public static void hideApplication(Event e) {
        Stage stage = (Stage) ((Node) e.getTarget()).getScene().getWindow();
        stage.hide();
    }

    public static void showApplication(Event e) {
        Stage stage = (Stage) ((Node) e.getTarget()).getScene().getWindow();
        stage.show();
    }

    public static void renameApplication(Event e, String title) {
        Stage stage = (Stage) ((Node) e.getTarget()).getScene().getWindow();
        stage.setTitle(title);
        stage.notify();
    }

    public static Object getControlledStage(){
        return controlledStage.getClass();
    }

    public static Stage getStageFromEvent(Event e){
        Stage stage = (Stage) ((Node) e.getTarget()).getScene().getWindow();
        return stage;
    }
    //endregion

}