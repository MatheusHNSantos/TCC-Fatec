package controller.dashboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import controller.Controller;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entity.person.employee.Employee;
import model.entity.person.user.User;
import util.dialogs.FxDialogs;
import util.exception.UserException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManageUsersController implements Initializable {

    public static final String path = "manageUsers.fxml";
    public static final String title = "Gerenciar Usuários";

    //region @FXML Objects
    @FXML    private JFXComboBox<String> cbox_exibitionModeUser; //Combo Box de tipo de Pesquisa de usuarios
    @FXML    private JFXComboBox<String> cbox_levelUser; //Combo Box de tipo de nível de usuario
    @FXML    private JFXToggleButton tbtn_statusUser; //Seletor de Status do usuario
    @FXML    private TableView<User> tview_users; //Tabela de usuarios
    @FXML    private Label lbl_nameEmployee; //TextField de campo para nome de funcionario
    @FXML    private Label lbl_roleEmployee; //TextField de campo para função de funcionario
    @FXML    private JFXTextField txt_userLogin; //TextField de campo para usuário
    @FXML    private JFXTextField txt_passUser; //TextField de campo para senha
    @FXML    private JFXButton btn_saveUser; //Botão Salvar
    @FXML    private JFXButton btn_cancelUser; //Botão Cancelar
    @FXML    private JFXButton btn_editUser; //Botão Editar
    @FXML    private Group group_dataUser; //Agrupamento de TextField atributos cliente
    @FXML    private Group group_dataUserBtnSaveCancel; //Salvar e Cancelar

    @FXML    private TableColumn<User, String> columnUserLogin;
    @FXML    private TableColumn<User, User> columnUserLevel;
    @FXML    private TableColumn<User, User> columnUserEmployee;
    @FXML    private TableColumn<User, User> columnUserStatus;

    //endregion
//region Normal objects
    private String actionUser = "";


    public ObservableList<User> dataObervableUser;
    public ArrayList<User> listUser;


    private int idUserSelected = 0;
    private int idUserEmployeeSelected = 0;

    public ObservableList<String> options;
    //endregion



    @Override
    public void initialize(URL url, ResourceBundle rb){

        btn_editUser.setDisable(true);

        //region TableView
        dataObervableUser = FXCollections.observableArrayList();

        //region columns declarations
        columnUserLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        setCells(columnUserLevel, "level");
        setCells(columnUserEmployee, "name_employee");
        setCells(columnUserStatus, "status");

        //endregion

        listUser = User.ReadAll();
        dataObervableUser.addAll(listUser);
        tview_users.setItems(dataObervableUser);
        //endregion

        //region Employee Details
        cbox_levelUser.getItems().addAll(
                "",
                "Operador",
                "Administrador"
        );

        showUserDetails(null);

        tview_users.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showUserDetails(newValue));
        //endregion

        //region Combo Box Type Search Supplier
        cbox_exibitionModeUser.getItems().addAll("Todos", "Ativos", "Inativos");
        cbox_exibitionModeUser.getSelectionModel().select(0);
        //endregion

        //region Button Search Users
        cbox_exibitionModeUser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                dataObervableUser.clear();

                switch (cbox_exibitionModeUser.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        dataObervableUser.addAll(User.ReadAll());
                        break;
                    case 1:
                        dataObervableUser.addAll(User.readAllByStatus(true));
                        break;
                    case 2:
                        dataObervableUser.addAll(User.readAllByStatus(false));
                        break;

                }
            }
        });

        //endregion


        //region Events
        btn_saveUser.setOnMouseClicked(this::handlerButtonActionSaveUser);
        btn_cancelUser.setOnMouseClicked(this::handlerButtonActionCancelUser);
        btn_editUser.setOnMouseClicked(this::handlerButtonActionEditUser);
        tbtn_statusUser.setOnMouseClicked(this::handlerButtonActionStatusUser);
        //endregion

    }

    private void setCells(TableColumn<User, User> columnDefault, String opc) {

        columnDefault.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue()));

        columnDefault.setCellFactory(column -> new TableCell<User, User>() {
            private VBox graphic;
            private Label label;

            // Anonymous constructor:
            {
                graphic = new VBox();
                label = createLabel();
                graphic.getChildren().addAll(label);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            public void updateItem(User user, boolean empty) {
                if (user == null) {
                    setGraphic(null);
                } else {
                    String text = "";

                    switch (opc) {
                        case "level":
                            if(user.getLevel() == 5){
                                text = "Administrador";
                            }else if (user.getLevel() == 4){
                                text = "Operador";
                            }break;

                        case "name_employee":
                            text = user.getEmployee().getNamePerson();
                            break;

                        case "status":
                            text = (user.getStatus()) ? "Ativo" : "Inativo";
                            break;
                        default:
                            break;

                    }
                    label.setText(text);
                    setGraphic(graphic);
                }
            }
        });
    }

    private final Label createLabel() {
        Label label = new Label();
        VBox.setVgrow(label, Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        return label ;
    }

    //region default methods
    private void setUserActiveButtons(Boolean bool_1, Boolean bool_2,String action){
        group_dataUser.setDisable(bool_1);
        group_dataUserBtnSaveCancel.setDisable(bool_1);
        btn_editUser.setDisable(bool_2);
        if(!action.equals("node"))
            actionUser = action;

    }
    private void handlerButtonActionEditUser(MouseEvent event) {
        setUserActiveButtons(false,true,"Editar");
    }
    private void handlerButtonActionCancelUser(MouseEvent event) {

        if(actionUser.equals("Status")){
            if(tbtn_statusUser.isSelected()){
                tbtn_statusUser.setSelected(false);
            }else{
                tbtn_statusUser.setSelected(true);
            }
        }
        setUserActiveButtons(true,false,"");
    }
    private void handlerButtonActionSaveUser(MouseEvent event) {
        setUserActiveButtons(true,false,"node");
        switch (actionUser ){
            case "Editar":
                try{
                    saveUser();
                } catch (UserException ex) {
                    FxDialogs.showException("Erro ao salvar Usuário",ex.getClass() +  " - " + ex.getMessage(),ex);
                }
                break;
            case "Status":
                try {
                    setStatusUser();
                } catch (UserException ex) {
                    FxDialogs.showException("Erro ao salvar Usuário",ex.getClass() +  " - " + ex.getMessage(),ex);
                }
                break;
            default:
                break;
        }
        actionUser = "";
    }
    private void handlerButtonActionStatusUser(MouseEvent event) {
        group_dataUserBtnSaveCancel.setDisable(false);
        btn_editUser.setDisable(true);
        if(!actionUser.equals("Editar"))
                actionUser = "Status";
    }
    //endregion

    //region methods
    private void showUserDetails(User user) {
        setUserActiveButtons(true, false, "");
        tbtn_statusUser.setSelected(false);
        if (user != null) {
            btn_editUser.setDisable(false);
            // Preenche as labels com informações do objeto user.

            Employee employee = new Employee(user.getIdEmployee());
            employee = new Employee(employee.getIdPerson(), employee.getIdEmployee());
            lbl_nameEmployee.setText(employee.getNamePerson());
            lbl_roleEmployee.setText(employee.getRole());

            //idUserSelected = ;
            idUserEmployeeSelected  = user.getIdEmployee();


            txt_userLogin.setText(user.getLogin());
            txt_passUser.setText(user.getPassword());

            if(user.getLevel() == 5) {
                cbox_levelUser.setValue("Administrador");
            }else if(user.getLevel() == 4) {
                cbox_levelUser.setValue("Operador");
            }else {
                cbox_levelUser.setValue("");
            }


            tbtn_statusUser.setSelected(user.getStatus());
            if(user.getStatus()) tbtn_statusUser.setText("Ativo");
            else tbtn_statusUser.setText("Inativo");


        } else {
            // user é null, remove todo o texto.
            lbl_nameEmployee.setText("");
            lbl_roleEmployee.setText("");
            txt_userLogin.setText("");
            txt_passUser.setText("");
            cbox_levelUser.setValue("");
            tbtn_statusUser.setSelected(false);
            idUserEmployeeSelected = 0;
            btn_editUser.setDisable(true);

        }
    }

    private void saveUser() throws UserException {
        //Código banco aqui
        User user = new User(idUserEmployeeSelected);
        String level = cbox_levelUser.getSelectionModel().getSelectedItem();

        if(!user.getLogin().isEmpty() || !user.getPassword().isEmpty() || !level.equals("")){

            user.setLogin(txt_userLogin.getText());
            user.setPassword(txt_passUser.getText());


            if(level.equals("Administrador")) {
                user.setLevel(5);
            }else if(level.equals("Operador")) {
                user.setLevel(4);
            }

            user.setStatus(tbtn_statusUser.isSelected());

            user.SaveByIdEmployee();

        }else{
            if(user.getLogin().isEmpty() && user.getPassword().isEmpty()){
                throw new UserException("Digite um login e uma senha!") {};
            }else if (user.getLogin().isEmpty()) {
                throw new UserException("Digite um login!") {};
            }else if (user.getPassword().isEmpty()) {
                throw new UserException("Digite uma senha!") {};

            }else if (level.equals("")){
                throw new UserException("Escolha um nivel de acesso!") {};
            }
        }

        listUser = User.ReadAll();
        dataObervableUser.clear();
        dataObervableUser.addAll(listUser);
    }

    private void setStatusUser() throws UserException {
        //Código banco aqui
        saveUser();
    }
    //endregion


    public static Stage loader() throws IOException {

        Stage stage;
        stage = Controller.loader(DashboardController.class, StageStyle.DECORATED, path, title);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        return stage;

    }
}
