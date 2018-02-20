/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.dashboard;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.css.PseudoClass;
import javafx.event.EventType;
import javafx.scene.*;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jeanderson.br.util.MaskFormatter;
import util.MaskField.MaskFieldUtil;
import com.jfoenix.controls.*;
import controller.Controller;
import controller.login.LoginController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import model.entity.address.Address;
import model.entity.person.Person;
import model.entity.person.customer.Customer;
import model.entity.person.employee.Employee;
import model.entity.person.supplier.Supplier;
import model.entity.person.user.User;
import model.entity.phone.Phone;
import model.entity.product.Ingredient;
import model.entity.product.Product;
import model.entity.product.ProductIngredient;
import model.entity.product.ProductType;
import model.entity.sale.Sale;
import util.dialogs.FxDialogs;
import util.exception.UserException;
import util.viacep.Endereco;
import util.viacep.ViaCEP;

import util.Functions.UpCase;

import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.lang.String.valueOf;
import static util.Functions.UpCase.upCaseFirst;

/**
 * FXML Controller class
 * JtablePaneAdm
 *
 * @author Matheus Henrique
 */

public class DashboardController implements Initializable {

    public static DashboardController dashboardControllerReference;

    final PseudoClass errorClass = PseudoClass.getPseudoClass("error");

    private static final String path = "dashboard.fxml";
    private static final String title = "PAVG Apetitoso - v1.0.1118";

    private static User user;

    private Executor executor;

    //region general objects
    @FXML
    public AnchorPane mainAnchorPane;
    @FXML
    private JFXTabPane paneTab;
    @FXML
    private Tab welcomeTab;
    @FXML
    private Tab salesTab;
    @FXML
    private JFXTabPane tablePaneAdm;

    @FXML
    private Label label_date;
    @FXML
    private Label label_user;
    @FXML
    private Label label_time;

    @FXML
    private Tab tabCustomer;
    @FXML
    private Tab tabOrder;
    //endregion

    //region Tab "Inicio" Objects

    @FXML
    public Hyperlink hl_finish;
    @FXML
    public Hyperlink hl_logout;
    @FXML
    private Label welcome_user;

    //endregion

    //region Tab "Pedido" Objects

    //region @FXML Objects
    @FXML
    private JFXButton btn_finishSale;
    @FXML
    private Hyperlink hyperlinkVerifyRegister;
    @FXML
    private JFXButton btn_newOrder;
    @FXML
    private JFXButton btn_clearOrderProducts;
    @FXML
    private JFXButton btn_removeProductOnOrder;
    @FXML
    private JFXButton btn_addProductOnOrder;
    @FXML
    private JFXToggleButton toggleButton_gerarCupomFiscal;
    @FXML
    private Label lbl_codeOfOrder;
    @FXML
    private TextField tfield_name;
    @FXML
    private TextField tfield_telephone;
    @FXML
    private TextField tfield_adress;
    @FXML
    private ImageView img_selectionedProduct;
    @FXML
    private TextArea tarea_orderObservations;
    @FXML
    private TableView tview_products;
    @FXML
    private Label lbl_total;
    @FXML
    public Label lbl_score;


    @FXML
    private JFXListView listView_categories;
    @FXML
    private JFXListView listView_products;
    @FXML
    private JFXTextField txt_searchOrder;
    @FXML
    private JFXButton btn_clearSearchOrder;
    @FXML
    private JFXCheckBox checkboxOrderCustomer;


    @FXML
    private Hyperlink hl_historicCustomer;
    @FXML
    private TableView<Product> tViewOrderProducts;
    @FXML
    private TableColumn<Product, String> columnOrderProductName;
    @FXML
    private TableColumn<Product, String> columnOrderProductPrice;
    //endregion

    //region Normal objects
    private float totalPrice = 0;
    public ObservableList<Product> dataObervableProductsOnCart;

    public ArrayList<ProductType> productType;
    public ObservableList<String> dataObservableProductType;

    public ObservableList<String> dataObservableProductsFinded;
    public ArrayList<Product> productsFinded;

    private Property<Person> propertyPerson;
    //endregion

    //endregion

    //region Tab "Clientes" Objects

    //region @FXML Objects
    @FXML
    private TableView<Customer> tview_customer; //Tabela de cliente
    @FXML
    private JFXComboBox<String> cbox_typeSearchCustomer; //Combo Box de tipo de Pesquisa de cliente
    @FXML
    private JFXTextField txt_searchCustomer; //TextField de campo para pesquisa de cliente
    @FXML
    private JFXButton btn_searchCustomer; //Botão Pesquisar
    @FXML
    private JFXButton btn_resetCustomer;

    //---
    @FXML
    private JFXToggleButton tbtn_statusCustomer; //Seletor de Status do cliente
    @FXML
    private Group group_dataCustomer; //Agrupamento de TextField atributos cliente
    @FXML
    private Group group_dataCustomerBtnSaveCancel; //Salvar e Cancelar
    @FXML
    private Group group_customerBtnEditNew; //Editar e Adicionar
    @FXML
    private Label lbl_pointsCustomer; //TextField de campo para nome cliente
    @FXML
    private JFXTextField txt_nameCustomer; //TextField de campo para nome cliente
    @FXML
    private JFXTextField txt_cepCustomer; //TextField de campo para CEP cliente
    @FXML
    private JFXTextField txt_bairroCustomer; //TextField de campo para bairro cliente
    @FXML
    private JFXTextField txt_streetCustomer; //TextField de campo para rua cliente
    @FXML
    private JFXTextField txt_numberCustomer; //TextField de campo para numero cliente
    @FXML
    private JFXTextField txt_phone1Customer; //TextField de campo para telefone1 cliente
    @FXML
    private JFXTextField txt_phone2Customer; //TextField de campo para telefone2 cliente
    @FXML
    private JFXButton btn_saveCustomer; //Botão Salvar
    @FXML
    private JFXButton btn_cancelCustomer; //Botão Cancelar
    @FXML
    private JFXButton btn_newCustomer; //Botão Criar
    @FXML
    private JFXButton btn_editCustomer; //Botão Editar
    @FXML
    private TableColumn<Person, Person> columnCustomerName;
    @FXML
    private TableColumn<Person, Person> columnCustomerPhone1;
    @FXML
    private TableColumn<Person, Person> columnCustomerPhone2;
    @FXML
    private TableColumn<Person, Person> columnCustomerCep;

    //endregion

    //region Normal objects
    private String actionCustomer = "";
    private boolean comeBackSearchByTelephone = false;

    public ObservableList<Customer> dataObservableCustomer;

    private int idCustomerSelected = 0;
    //endregion

    //endregion

    //region Tab "Administração" Objects

    @FXML
    private Tab tabAdmin;

    //region Tab "Faturamento" Objects
    /**
     * tab "Faturamento" Objects
     */
    @FXML
    private JFXComboBox<?> cbox_scopeBarChart; //Combo Box de intervalo
    @FXML
    private JFXComboBox<?> cbox_scopePieChart; //Combo Box de intervalo
    @FXML
    private PieChart pc_pieChart;
    @FXML
    private BarChart bar_barChart;
    //endregion

    //region Tab "Vendas" Object
    /**
     * tab "Vendas" Objects
     */

    //region @FXML Objects
    @FXML
    private JFXButton btn_showSaleDetail; //Detalhes da venda
    @FXML
    private JFXButton btn_searchSale; //Botão Pesquisar
    @FXML
    private JFXButton btn_resetSaleTable;
    @FXML
    private JFXDatePicker datePicker_sale; //Date Picker Venda
    @FXML
    private JFXComboBox<String> cbox_typeSearchSale; //Combo Box de tipo de Pesquisa de Vendas
    @FXML
    private JFXTextField txt_searchSale; //TextField de campo para pesquisa de Vendas
    @FXML
    private TableView<Sale> tViewSale;
    @FXML
    private TableColumn<Sale, String> columnSaleID;
    @FXML
    private TableColumn<Sale, String> columnSaleCustomer;
    @FXML
    private TableColumn<Sale, String> columnSalePrice;
    @FXML
    private TableColumn<Sale, String> columnSaleDate;

    //endregion

    //region Normal Objects
    public ArrayList<Sale> dataModelSale;
    public ObservableList<Sale> dataObservableSale;
    //endregion

    //endregion

    //region Tab "Fornecedores" Objects
    /**
     * tab "Fornecedores" Objects
     */

    //region @FXML Objects
    @FXML
    private TableView<Supplier> tview_supplier; //Tabela de fornecedores
    @FXML
    private JFXComboBox<String> cbox_typeSearchSupplier; //Combo Box de tipo de Pesquisa de fornecedores
    @FXML
    private JFXTextField txt_searchSupplier; //TextField de campo para pesquisa de fornecedores
    @FXML
    private JFXButton btn_searchSupplier; //Botão Pesquisar
    //---
    @FXML
    private JFXToggleButton tbtn_statusSupplier; //Seletor de Status do fornecedores
    @FXML
    private Group group_dataSupplier; //Agrupamento de TextField atributos fornecedores
    @FXML
    private Group group_supplierBtnSaveCancel; //Salvar e Cancelar
    @FXML
    private Group group_supplierBtnEditNew; //Editar e Adicionar
    @FXML
    private JFXTextField txt_nameSupplier; //TextField de campo para nome fornecedores
    @FXML
    private JFXTextField txt_cnpjSupplier; //TextField de campo para CNPJ fornecedores
    @FXML
    private JFXTextField txt_cepSupplier; //TextField de campo para CEP fornecedores
    @FXML
    private JFXTextField txt_bairroSupplier; //TextField de campo para bairro fornecedores
    @FXML
    private JFXTextField txt_streetSupplier; //TextField de campo para rua fornecedores
    @FXML
    private JFXTextField txt_numberSupplier; //TextField de campo para numero fornecedores
    @FXML
    private JFXTextField txt_phone1Supplier; //TextField de campo para telefone1 fornecedores
    @FXML
    private JFXTextField txt_phone2Supplier; //TextField de campo para telefone2 fornecedores
    @FXML
    private JFXButton btn_saveSupplier; //Botão Salvar
    @FXML
    private JFXButton btn_cancelSupplier; //Botão Cancelar
    @FXML
    private JFXButton btn_newSupplier; //Botão Criar
    @FXML
    private JFXButton btn_editSupplier; //Botão Editar

    @FXML
    private TableColumn<Person, Person> columnSupplierName;
    @FXML
    private TableColumn<Person, Person> columnSupplierPhone1;
    @FXML
    private TableColumn<Person, Person> columnSupplierPhone2;
    @FXML
    private TableColumn<Person, Person> columnSupplierCep;
    @FXML
    private TableColumn<Supplier, String> columnSupplierCnpj;
    //endregion

    //region Normal objects
    private String actionSupplier = "";

    public ObservableList<Supplier> dataObervableSupplier = FXCollections.observableArrayList();
    public ArrayList<Supplier> listSupplier = new ArrayList<>();


    private int idSupplierSelected = 0;

    //endregion

    //endregion

    //region Tab "Funcionários/Usuários" Objects
    /**
     * tab "Funcionários/Usuários" Objects
     * <p>
     * <p>
     * btn_manageUsers -> "Gerenciar Usuários (Login)"
     */

    //region @FXML Objects
    @FXML
    private JFXTextField txt_rgEmployee;
    @FXML
    private Hyperlink btn_manageUsers;
    @FXML
    private Hyperlink btn_editEmployee; //Editar Ingredientes
    @FXML
    private JFXToggleButton select_loginStatus;
    @FXML
    private JFXToggleButton select_typeStatusFunc; //select status func
    @FXML
    private JFXComboBox<String> cbox_typeSearchEmployee; //Combo Box de tipo de Pesquisa de Funcionário
    @FXML
    private Group group_dataFunc; //Agrupamento de TextField atributos do funcionario
    @FXML
    private Group group_dataFunc_btn; //Salvar e cancelar
    @FXML
    private Group group_editAddFunc; //Agrupamento Editar de Cadastrar funcionario
    @FXML
    private JFXButton btn_searchEmployee; //Botão Pesquisar
    @FXML
    private JFXButton btn_saveEmployee; //Botão Salvar
    @FXML
    private JFXButton btn_cancelEmployee; //Botão Cancelar
    @FXML
    private JFXButton btn_addEmployee; //Botão Cadastrar
    @FXML
    private JFXTextField txt_cpfEmployee;
    @FXML
    private JFXTextField txt_searchEmployee; //TextField de campo para pesquisa
    @FXML
    private JFXTextField txt_nameEmployee; //TextField de campo para nome funcionario
    @FXML
    private JFXTextField txt_roleEmployee; //TextField de campo para função funcionario
    @FXML
    private JFXTextField txt_cepEmployee; //TextField de campo para CEP funcionario
    @FXML
    private JFXTextField txt_bairroEmployee; //TextField de campo para bairro funcionario
    @FXML
    private JFXTextField txt_streetEmployee; //TextField de campo para rua funcionario
    @FXML
    private JFXTextField txt_numberEmployee; //TextField de campo para numero funcionario
    @FXML
    private JFXTextField txt_phone1Employee; //TextField de campo para telefone1 funcionario
    @FXML
    private JFXTextField txt_phone2Employee; //TextField de campo para telefone2 funcionario
    @FXML
    private TableView<Employee> tview_func; //Tabela de funcionarios
    @FXML
    private TableColumn<Person, Person> columnEmployeeName;
    //@FXML    private TableColumn<Employee, String> columnEmployeePhone1;
    //@FXML    private TableColumn<Employee, String> columnEmployeePhone2;
    @FXML
    private TableColumn<Person, Person> columnEmployeePhone1;
    @FXML
    private TableColumn<Person, Person> columnEmployeePhone2;
    @FXML
    private TableColumn<Employee, String> columnEmployeeRole;
    @FXML
    private TableColumn<Person, Person> columnEmployeeStatus;
    //login
    @FXML
    private TitledPane tp_login;
    @FXML
    private Group group_dataUser; //Agrupamento de TextField atributos user
    @FXML
    private Group group_dataUserBtnEditarCriar; //Editar e Criar
    @FXML
    private JFXTextField txt_userLogin; //TextField de campo para usuario login
    @FXML
    private JFXPasswordField txt_passLogin; //TextField de campo para senha login
    @FXML
    private JFXComboBox<String> cbox_typeLevelLogin; //Combo Box de tipo de Nivel de usuario
    @FXML
    private JFXButton btn_saveLogin; //Botão Salvar
    @FXML
    private JFXButton btn_cancelLogin; //Botão Cancelar
    @FXML
    private JFXButton btn_criarLogin; //Botão Criar
    @FXML
    private JFXButton btn_editarLogin; //Botão Editar
    //endregion

    //region Normal objects
    private String actionEmployee = "";
    private String actionLogin = "";
    public ObservableList<Employee> dataObervableEmployee;
    public ArrayList<Employee> listEmployee;
    private int idEmployeeSelected = 0;
    private int idPersonEmployeeSelected = 0;

    public ObservableList<String> options;
    //endregion

    //endregion

    //region Tab "Produtos" Objects
    /**
     * tab "Produtos" Objects
     **/

    //region @FXML Objects
    @FXML
    private Hyperlink btn_editProductType; //Editar Categoria
    @FXML
    private Hyperlink btn_editIngredients; //Editar Ingredientes
    @FXML
    private Hyperlink btn_alterImageProduct; //Alterar Imagem do Produto
    @FXML
    private Hyperlink btn_removeImageProduct; //Remover Imagem do Produto
    @FXML
    private JFXButton btn_listIngredients; //Adicionar Ingredientes ao Produto
    @FXML
    private JFXButton btn_dropIngredients; //Remover Ingredientes do Produto
    @FXML
    private JFXButton btn_saveProduct; //Botão Salvar
    @FXML
    private JFXButton btn_cancelProduct; //Botão Cancelar
    @FXML
    private JFXButton btn_editProduct; //Botão Editar
    @FXML
    private JFXButton btn_addProduct; //Botão Adicionar
    @FXML
    private JFXButton btn_searchProduct; //Botão Pesquisar
    @FXML
    private JFXToggleButton tbtn_statusProduct; //Seletor de Status do Produto
    @FXML
    private JFXComboBox<String> cbox_categoryProduct; //Combo Box de escolha de Categoria
    @FXML
    private JFXComboBox<String> cbox_typeSearchProduct; //Combo Box de tipo de Pesquisa de Produto
    @FXML
    private JFXTextField txt_searchProduct; //TextField de campo para pesquisa
    @FXML
    private JFXTextField txt_nameProduct; //TextField de Nome do Produto
    @FXML
    private JFXTextField txt_finalPriceProduct; //TextField Preço final do produto
    @FXML
    private JFXTextField txt_weightProduct; //TextField peso do produto
    @FXML
    private ImageView imageview_product; //Imagem do produto
    @FXML
    private TableView<Product> tview_product; //Tabela de produtos
    @FXML
    private TableColumn<Product, Product> columnProductName;
    @FXML
    private TableColumn<Product, Product> columnProductFinalPrice;
    @FXML
    private TableColumn<Product, Product> columnProductWeight;
    @FXML
    private TableColumn<Product, Product> columnProductType;
    @FXML
    private TableColumn<Product, Product> columnProductStatus;
    @FXML
    private TableView<Ingredient> tview_productIngredient; //Tabela de Ingredientes do produto
    @FXML
    private TableColumn<Ingredient, Ingredient> columnIngredientName;
    @FXML
    private TableColumn<Ingredient, Ingredient> columnIngredientPrice;
    @FXML
    private TableColumn<Ingredient, Ingredient> columnIngredientStatus;
    @FXML
    private Group group_dataProduct; //Agrupamento de TextField atributos do produto
    @FXML
    private Group group_cancelSaveProduct; //Agrupamento botão Cancelar e Salvar
    @FXML
    private Group group_addDropIngredients; //Agrupamento botão de Adição e Remoção de ingredientes no produto
    @FXML
    private Group group_editAddProduct; //Agrupamento Editar de Adicionar produto
    //endregion

    //region Normal objects
    private String actionProduct = "";

    private ObservableList<Product> dataObervableProduct = FXCollections.observableArrayList();
    private ArrayList<Product> listProduct = new ArrayList<>();

    private ObservableList<String> dataObervableProductType = FXCollections.observableArrayList();
    private ArrayList<String> listProductType = new ArrayList<>();

    private static ObservableList<Ingredient> dataObervableIngredient = FXCollections.observableArrayList();
    private ArrayList<Ingredient> listIngredient = new ArrayList<>();

    private int idProductSelected = 0;
    private int idIngredientSelected = 0;
    public static boolean refreshCategory = false;
    //endregion


    //endregion

    //region Tab "Log" Objects
    /**
     * tab "Log" Objects
     */
    @FXML
    private TableView<?> tview_log; //Tabela de logs
    @FXML
    private JFXButton btn_filterLog; //Botão Filtrar Log
    @FXML
    private JFXDatePicker datePicker_log; //Date Picker Log
    //endregion
    //endregion

    //region SimpleDataFormat
    //private SimpleDateFormat formatador = new SimpleDateFormat("hh:mm:ss a");
    private SimpleDateFormat formatador = new SimpleDateFormat("hh:mm:ss");
    //endregion

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        dashboardControllerReference = this;

        //region Setup Inicial

        if(user.getLevel() != 5){
            paneTab.getTabs().remove(tabAdmin);
        }

        label_user.setText(upCaseFirst(new Employee(user.getIdEmployee()).getNamePerson()));

        //region Executor to Query database

        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

        //endregion

        tp_login.setVisible(false); //complementos
        //paneTab.getTabs().remove(salesTab);
        //paneTab.getTabs().remove(usersTab);

        //Data
        Date dataSistema = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        label_date.setText(formato.format(dataSistema));


        KeyFrame frame = new KeyFrame(Duration.millis(1000), e -> atualizaHoras2());
        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        //Hora //future examples
        // LocalDateTime dt = LocalDateTime.now();
        //String dataAtual = dt.getDayOfMonth() + "/" + dt.getMonthValue() + "/" + dt.getYear();
        //String horaAtual = dt.getHour() + ":" + dt.getMinute();
        //label_date.setText(dataAtual);
        //label_time.setText(horaAtual);
        //Timer timer = new Timer(1000, new hora());
        //timer.start();
        //Timer tm = new Timer();

        //tm.scheduleAtFixedRate(task,1000,1000);


        //dataModelTests = new ArrayList<>();

        //endregion

        //region Tab "Inicio"
        welcome_user.setText("Bem Vindo " + upCaseFirst(user.getLogin()) + "!");

        //region Tab "Incio" Events
        hl_logout.setOnMouseClicked(this::handlerHyperlinkActionLogout);
        hl_finish.setOnMouseClicked(this::handlerHyperlinkActionFinish);
        //endregion
        //endregion

        //region Tab "Pedido"

        //region Tab "Pedido" Initialize Variables

        propertyPerson = new SimpleObjectProperty<>();

        propertyPerson.setValue(null);

        lbl_codeOfOrder.setText(String.valueOf(Sale.getLastIdSale() + 1));

        hl_historicCustomer.setDisable(true);

        lbl_total.setText("0.0");

        //run later to fire event property of CheckBox
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkboxOrderCustomer.setSelected(true);
            }
        });

        //region Tool Tips
        txt_searchOrder.setPromptText("Digite aqui o nome do produto a ser buscado automaticamente...");
        checkboxOrderCustomer.setTooltip(new Tooltip("Mudar habilitadores dos campos"));
        //endregion

        //region Text Fields
        MaskFieldUtil.foneField(tfield_telephone);
        //endregion

        //region TableView Products on Order
        dataObervableProductsOnCart = FXCollections.observableArrayList();
        columnOrderProductName.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
        columnOrderProductPrice.setCellValueFactory(new PropertyValueFactory<>("finalPriceProduct"));
        tViewOrderProducts.setItems(dataObervableProductsOnCart);
        //endregion

        //region ListView Categories
        productType = ProductType.ReadAll();
        dataObservableProductType = FXCollections.observableArrayList(Arrays.asList());
        productType.forEach(productType -> dataObservableProductType.add(productType.getNameProductType()));
        listView_categories.setItems(dataObservableProductType);
        //endregion

        //region ListView PorductsFinded
        productsFinded = new ArrayList<>();
        dataObservableProductsFinded = FXCollections.observableArrayList(Arrays.asList());
        listView_products.setItems(dataObservableProductsFinded);
        //endregion

        //endregion

        //region Tab "Pedido" - Events
        btn_finishSale.setOnMouseClicked(this::handlerButtonActionFinishOrder);
        btn_newOrder.setOnMouseClicked(this::handlerButtonActionNewOrder);
        btn_clearOrderProducts.setOnMouseClicked(this::handlerButtonActionClearOrderProducts);
        btn_addProductOnOrder.setOnMouseClicked(this::handlerButtonActionAddProductOnOrder);
        btn_removeProductOnOrder.setOnMouseClicked(this::handlerButtonActionRemoveProductOnOrder);
        hl_historicCustomer.setOnMouseClicked(this::handlerButtonActionHistoricCustomer);
        hyperlinkVerifyRegister.setOnMouseClicked(this::handlerHyperlinkVerifyRegister);

        propertyPerson.addListener(new ChangeListener<Person>() {
            @Override
            public void changed(ObservableValue<? extends Person> observable, Person oldValue, Person newValue) {
                if (newValue == null) {
                    hl_historicCustomer.setDisable(true);
                    btn_finishSale.setDisable(true);
                } else {
                    hl_historicCustomer.setDisable(false);
                    btn_finishSale.setDisable(false);
                }
            }
        });

        dataObervableProductsOnCart.addListener(new ListChangeListener<Product>() {
            @Override
            public void onChanged(Change<? extends Product> change) {

                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved()) {

                        totalPrice = 0;

                        if (dataObervableProductsOnCart.size() > 0) {
                            btn_removeProductOnOrder.setDisable(false);
                        } else {
                            btn_removeProductOnOrder.setDisable(true);
                        }

                        dataObervableProductsOnCart.forEach(product -> totalPrice += product.getFinalPriceProduct());
                        lbl_total.setText(String.valueOf(totalPrice));


                    }
                }
            }
        });

        listView_products.getFocusModel().focusedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!listView_products.isFocused()) {
                            listView_products.scrollTo(listView_products.getFocusModel().getFocusedIndex());
                        }
                    }
                });
            }
        });

        listView_products.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().toString().equals("ENTER")) {
                    if (!listView_products.getSelectionModel().isEmpty()) {
                        dataObervableProductsOnCart.add(productsFinded.get(listView_products.getFocusModel().getFocusedIndex()));
                    }
                }
            }
        });

        listView_products.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if ((event.getClickCount() % 2 == 0) && !listView_products.getSelectionModel().isEmpty()) {
                    dataObervableProductsOnCart.add(productsFinded.get(listView_products.getFocusModel().getFocusedIndex()));
                }
            }
        });

        listView_categories.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (!listView_categories.getSelectionModel().isEmpty()) {
                    dataObservableProductsFinded.clear();
                    ProductType productTypeObj = productType.get(listView_categories.getSelectionModel().getSelectedIndex());
                    productsFinded = Product.readByCategory(productTypeObj.getIdProductType());
                    productsFinded.forEach(product -> dataObservableProductsFinded.add(product.getNameProduct()));
                }
            }
        });

        //region Text Field Search Order
        txt_searchOrder.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1 && txt_searchOrder.getText().equals("")) {
                    listView_categories.getSelectionModel().clearSelection();
                    listView_categories.setDisable(true);
                    dataObservableProductsFinded.clear();
                } else if (txt_searchOrder.getText().equals("")) {
                    listView_categories.setDisable(false);
                }
            }
        });

        txt_searchOrder.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {

                    case UP:
                        listView_products.getSelectionModel().selectPrevious();
                        keyEvent.consume();
                        break;
                    case DOWN:
                        listView_products.getSelectionModel().selectNext();
                        keyEvent.consume();
                        break;

                }
            }
        });

        txt_searchOrder.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().toString().equals("ENTER")) {
                    if (!dataObservableProductsFinded.isEmpty())
                        dataObervableProductsOnCart.add(productsFinded.get(listView_products.getFocusModel().getFocusedIndex()));
                }
            }
        });

        txt_searchOrder.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

                //double check


                if (!txt_searchOrder.getText().equals("")) {


                    executeSearchProductsOrder(t1);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (txt_searchOrder.getText().equals("")) dataObservableProductsFinded.clear();
                        }
                    });


                } else {
                    dataObservableProductsFinded.clear();
                }


            }
        });

        //endregion

        btn_clearSearchOrder.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!txt_searchOrder.getText().equals("")) {
                    txt_searchOrder.clear();
                    listView_categories.setDisable(false);
                }

            }
        });

        checkboxOrderCustomer.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!t1) {
                    clientDataDisableSearchByTelephone();

                    tfield_telephone.setOnKeyPressed(null);

                } else {

                    clientDataEnableSearchByTelephone();
                    tfield_telephone.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if (event.getCode().toString().equals("ENTER")) {
                                searchByTelephone();
                            }
                        }
                    });

                }
            }
        });

        tabOrder.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                ArrayList<ProductType> newProductTypes = ProductType.ReadAll();
                if(productType.size() != newProductTypes.size()){
                    productType = newProductTypes;
                    dataObservableProductType.clear();
                    productType.forEach(productType1 -> dataObservableProductType.add(productType1.getNameProductType()));
                    System.out.println("SIZE CHANGED");
                }
            }
        });

        //endregion

        //endregion

        //region Tab "Clientes"

        btn_editCustomer.setDisable(true);

        //region CustomMask Fields
        MaskFieldUtil.cepField(txt_cepCustomer);
        MaskFieldUtil.numericField(txt_numberCustomer);
        MaskFieldUtil.foneField(txt_phone1Customer);
        MaskFieldUtil.foneField(txt_phone2Customer);
        //endregion

        //region Combo Box Type Search Customer
        cbox_typeSearchCustomer.getItems().addAll("Nome do cliente", "Telefone");
        cbox_typeSearchCustomer.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                txt_searchCustomer.clear();
            }
        });
        cbox_typeSearchCustomer.getSelectionModel().select(0);
        //endregion

        //region Text Search Customer
        txt_searchCustomer.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                switch (cbox_typeSearchCustomer.getSelectionModel().getSelectedIndex()){
                    case 1:
                        if(!checkNumeric(event.getCharacter())){
                            event.consume();
                        }

                        break;
                }

            }
        });
        //endregion

        //region Button Search Customer
        btn_searchCustomer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (txt_searchCustomer.getText().equals("")) {
                    return;
                }



                switch (cbox_typeSearchCustomer.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        dataObservableCustomer.clear();
                        dataObservableCustomer.addAll(Customer.readByName(txt_searchCustomer.getText()));

                        break;

                    case 1:

                        dataObservableCustomer.clear();
                        dataObservableCustomer.addAll(Customer.readByPhone(txt_searchCustomer.getText()));
                        break;
                }
            }
        });
        //endregion

        //region Button Reset Customer
        btn_resetCustomer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resetTableViewCustomer();
            }
        });
        //endregion

        //region TableView
        dataObservableCustomer = FXCollections.observableArrayList();

        //region columns declarations

        setCells(columnCustomerName, "namePerson");
        setCells(columnCustomerPhone1, "phone1");
        setCells(columnCustomerPhone2, "phone2");
        setCells(columnCustomerCep, "cep");

        dataObservableCustomer.addAll(Customer.ReadAll());
        tview_customer.setItems(dataObservableCustomer);
        //endregion

        //region Supplier Details

        showCustomerDetails(null);

        tview_customer.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCustomerDetails(newValue));

        //tview_customer.getSelectionModel().getSelectedItem().getIdPerson();

        //endregion

        //region Events
        btn_editCustomer.setOnMouseClicked(this::handlerButtonActionEditCustomer);
        btn_newCustomer.setOnMouseClicked(this::handlerButtonActionNewCustomer);
        btn_cancelCustomer.setOnMouseClicked(this::handlerButtonActionCancelCustomer);
        btn_saveCustomer.setOnMouseClicked(this::handlerButtonActionSaveCustomer);
        tbtn_statusCustomer.setOnMouseClicked(this::handlerButtonActionStatusCustomer);
        txt_cepCustomer.setOnKeyPressed(this::handleronKeyEnterPressedCEP);
        //endregion

        //endregion

        //region Tab Selection Event

        //region ON TAB WORK

        //region get ID NODE example
        /*
        Node source = (Node)event.getSource();

        Node target = (Node)event.getSource();

        String id = target / source .getId();
        */
        //endregion

        //Listen for clicks when Search by Telephone is being used
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                TabPane tabPane = findTabPaneForNode(tabCustomer.getContent());
                tabPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        if (comeBackSearchByTelephone) {

                            if (!event.getTarget().toString().contains("Clientes")) {

                                if (FxDialogs.showConfirmYesNo("Deseja cancelar o cadastro do cliente?", "", FxDialogs.NO, FxDialogs.YES).equals(FxDialogs.YES)) {
                                    tfield_telephone.clear();
                                    comeBackSearchByTelephone = false;
                                    clearCustomerDetails();
                                    setCustomerActiveButtons(true, false, "node");
                                    resetTableViewCustomer();

                                    paneTab.getTabs().forEach(tab -> tab.setDisable(false));

                                    if (event.getTarget().toString().contains("Administração")) {
                                        paneTab.getSelectionModel().select(tabAdmin);
                                    } else if (event.getTarget().toString().contains("Pedido")) {
                                        paneTab.getSelectionModel().select(tabOrder);
                                    } else if (event.getTarget().toString().contains("Inicio")) {
                                        paneTab.getSelectionModel().select(welcomeTab);
                                    }

                                } else {
                                    System.out.println(event.getTarget());
                                }
                            }
                        }
                    }
                });
            }
        });


        //region "Working but not all"
        /*
        tabCustomer.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {

                if(comeBackSearchByTelephone && !t1){

                    SingleSelectionModel<Tab> selectionModel = paneTab.getSelectionModel();
                    selectionModel.select(tabCustomer);


                    if (FxDialogs.showConfirmYesNo("Deseja cancelar o cadastro do cliente?", "", FxDialogs.NO, FxDialogs.YES).equals(FxDialogs.YES)){
                        tfield_telephone.clear();
                        comeBackSearchByTelephone = false;
                        clearCustomerDetails();
                        setCustomerActiveButtons(true, false, "node");
                        resetTableViewCustomer();
                        System.out.println("Cadastro hibrido cancelado");
                    }else{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                selectionModel.clearAndSelect(2);
                            }
                        });
                    }
                }
            }
        });
        */
        //endregion

        //endregion

        //endregion

        //endregion

        //region Tab "Administração"

        //region Tab "Faturamento" Events
        /**
         * tab "Faturamento" ButtonAction
         */
        //endregion

        //region Tab "Venda"

        //region Date Picker
        datePicker_sale.setEditable(false);
        datePicker_sale.setConverter(new StringConverter<LocalDate>() {
            String pattern = "dd/MM/yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                //datePicker_sale.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate localDate) {
                if (localDate != null) {
                    return dateFormatter.format(localDate);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        datePicker_sale.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (datePicker_sale.getValue() != null) {

                    String pattern = "dd/MM/yyyy";
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                    dataObservableSale.clear();
                    dataObservableSale.addAll(Sale.readSaleByDate(dateFormatter.format(datePicker_sale.getValue())));

                }


            }
        });
        //endregion

        //region Reset Sale Table
        btn_resetSaleTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dataObservableSale.clear();
                dataObservableSale.addAll(Sale.ReadAll());
                txt_searchSale.clear();
                tViewSale.requestFocus();
            }
        });
        //endregion

        //region Show Detail Button
        btn_showSaleDetail.setDisable(true);

        btn_showSaleDetail.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    //SaleDetailController.loader().show();

                    Controller controller = new Controller();
                    FXMLLoader fxmlLoader = controller.fxmlLoaderContent(SaleDetailController.path);
                    SaleDetailController saleDetailController = fxmlLoader.getController();
                    saleDetailController.setSale(tViewSale.getSelectionModel().getSelectedItem());
                    Stage stage = controller.buildStage(fxmlLoader, SaleDetailController.title);
                    stage.setResizable(false);
                    stage.setOnShowing(onShow());
                    stage.setOnCloseRequest(onClose());
                    stage.showAndWait();


                } catch (IOException ex) {
                    FxDialogs.showWarning(ex.getMessage(), "Tente novamente.");
                }
            }
        });
        //endregion

        //region Search Button
        btn_searchSale.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (txt_searchSale.getText().equals("")) {
                    return;
                }

                switch (cbox_typeSearchSale.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        dataObservableSale.clear();
                        dataObservableSale.addAll(Sale.readSaleById(Integer.parseInt(txt_searchSale.getText())));
                        break;

                    case 1:
                        executeSearchSaleByName(txt_searchSale.getText());
                        break;

                }

            }
        });
        //endregion

        //region Text Search
        txt_searchSale.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {

            private boolean consume = false;

            @Override
            public void handle(KeyEvent event) {

                if (consume) {
                    event.consume();
                }

                if(event.getCode().equals(KeyCode.BACK_SPACE)){
                    return;
                }else if(cbox_typeSearchSale.getSelectionModel().getSelectedIndex() == 0 && !checkNumeric(event.getCharacter())){
                    event.consume();
                }

                if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                    if (event.getText().equals("0") && txt_searchSale.getText().isEmpty()) {
                        consume = true;
                    }
                } else {
                    consume = false;
                }
            }
        });
        //endregion

        //region Combo Box Type Data to Search
        cbox_typeSearchSale.getItems().addAll("ID", "Nome do cliente");
        cbox_typeSearchSale.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                txt_searchSale.clear();
            }
        });
        cbox_typeSearchSale.getSelectionModel().select(0);

        //endregion

        //region Table View Sales
        dataObservableSale = FXCollections.observableArrayList();
        columnSaleID.setCellValueFactory(new PropertyValueFactory<>("idSale"));
        columnSaleCustomer.setCellValueFactory(new PropertyValueFactory<>("namePerson"));
        columnSaleCustomer.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Sale, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Sale, String> saleStringCellDataFeatures) {
                return new SimpleStringProperty(new Person(saleStringCellDataFeatures.getValue().getIdCustomer()).getNamePerson());
            }
        });
        columnSalePrice.setCellValueFactory(new PropertyValueFactory<>("saleTotal"));
        columnSaleDate.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        tViewSale.setItems(dataObservableSale);
        dataObservableSale.addAll(Sale.ReadAll());

        tViewSale.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sale>() {
            @Override
            public void changed(ObservableValue<? extends Sale> observableValue, Sale sale, Sale t1) {
                if (tViewSale.getSelectionModel().isEmpty()) {
                    btn_showSaleDetail.setDisable(true);
                } else {
                    btn_showSaleDetail.setDisable(false);
                }
            }
        });

        //endregion

        //endregion

        //region Tab "Fornecedores" Events

        btn_editSupplier.setDisable(true);

        MaskFieldUtil.cnpjField(txt_cnpjSupplier);
        MaskFieldUtil.cepField(txt_cepSupplier);
        MaskFieldUtil.numericField(txt_numberSupplier);
        MaskFieldUtil.foneField(txt_phone1Supplier);
        MaskFieldUtil.foneField(txt_phone2Supplier);


        txt_cepSupplier.setOnKeyPressed(this::handleronKeyEnterPressedCEP);


        //region TableView
        dataObervableSupplier = FXCollections.observableArrayList();

        //region columns declarations

        //column name
        //columnSupplierName.setCellValueFactory(new PropertyValueFactory<>("namePerson"));
        setCells(columnSupplierName, "namePerson");
        setCells(columnSupplierPhone1, "phone1");
        setCells(columnSupplierPhone2, "phone2");
        setCells(columnSupplierCep, "cep");
        //column cnpj
        columnSupplierCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        //endregion


        listSupplier = Supplier.ReadAll();
        dataObervableSupplier.addAll(listSupplier);
        tview_supplier.setItems(dataObervableSupplier);
        //endregion

        //region Combo Box Type Search Supplier
        cbox_typeSearchSupplier.getItems().addAll("Nome", "Telefone", "CNPJ");
        cbox_typeSearchSupplier.getSelectionModel().select(0);
        //endregion

        //region Button Search Supplier
        btn_searchSupplier.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dataObervableSupplier.clear();

                if (txt_searchSupplier.getText().equals("")) {
                    dataObervableSupplier.addAll(Supplier.ReadAll());
                    return;
                }

                dataObervableSupplier.addAll(
                        Supplier.customReadAll(
                            cbox_typeSearchSupplier.getSelectionModel().getSelectedIndex(),
                                txt_searchSupplier.getText()
                        ));


            }
        });
        //endregion

        //region Supplier Details

        showSupplierDetails(null);

        tview_supplier.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSupplierDetails(newValue));
        //endregion

        //region Events
        btn_editSupplier.setOnMouseClicked(this::handlerButtonActionEditSupplier);
        btn_newSupplier.setOnMouseClicked(this::handlerButtonActionNewSupplier);
        btn_cancelSupplier.setOnMouseClicked(this::handlerButtonActionCancelSupplier);
        btn_saveSupplier.setOnMouseClicked(this::handlerButtonActionSaveSupplier);
        tbtn_statusSupplier.setOnMouseClicked(this::handlerButtonActionStatusSupplier);
        //btn_searchSupplier.setOnMouseClicked(this::handlerButtonActionSearchSupplier);
        //endregion



        //endregion

        //region Tab "Funcionários/Usuários" Events

        MaskFormatter maskFormatter = new MaskFormatter(txt_rgEmployee);
        maskFormatter.setMask(MaskFormatter.RG);

        btn_editEmployee.setDisable(true);

        MaskFieldUtil.cpfField(txt_cpfEmployee);
        MaskFieldUtil.cepField(txt_cepEmployee);
        MaskFieldUtil.numericField(txt_numberEmployee);
        MaskFieldUtil.foneField(txt_phone1Employee);
        MaskFieldUtil.foneField(txt_phone2Employee);


        //region TableView Employee
        dataObervableEmployee = FXCollections.observableArrayList();

        columnEmployeeName.setCellValueFactory(new PropertyValueFactory<>("namePerson"));
        setCells(columnEmployeeName, "namePerson");
        setCells(columnEmployeePhone1, "phone1");
        setCells(columnEmployeePhone2, "phone2");
        setCells(columnEmployeeStatus, "status");
        columnEmployeeRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        listEmployee = Employee.ReadAll();
        dataObervableEmployee.addAll(listEmployee);
        tview_func.setItems(dataObervableEmployee);
        //endregion

        //region Combo Box Type Search Employee
        cbox_typeSearchEmployee.getItems().addAll("Nome", "Telefone", "Função");
        cbox_typeSearchEmployee.getSelectionModel().select(0);
        //endregion

        //region Button Search Employee
        btn_searchEmployee.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dataObervableEmployee.clear();

                if (txt_searchEmployee.getText().equals("")) {
                    dataObervableEmployee.addAll(Employee.ReadAll());
                    return;
                }

                dataObervableEmployee.addAll(
                        Employee.customReadAll(
                                cbox_typeSearchEmployee.getSelectionModel().getSelectedIndex(),
                                txt_searchEmployee.getText()
                        ));


            }
        });
        //endregion

        cbox_typeLevelLogin.getItems().addAll(
                "",
                "Operador",
                "Administrador"
        );


        // Limpa os detalhes da pessoa.
        showEmployeeDetails(null);

        tview_func.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showEmployeeDetails(newValue));


        //region Events
        btn_manageUsers.setOnMouseClicked(this::handlerButtonActionManageUsers);
        select_loginStatus.setOnMouseClicked(this::handlerButtonSelectLoginStatus);
        btn_addEmployee.setOnMouseClicked(this::handlerButtonActionAddEmployee);
        btn_editEmployee.setOnMouseClicked(this::handlerButtonActionEditEmployee);
        btn_saveEmployee.setOnMouseClicked(this::handlerButtonActionSaveEmployee);
        btn_cancelEmployee.setOnMouseClicked(this::handlerButtonActionCancelEmployee);
        select_typeStatusFunc.setOnMouseClicked(this::handlerButtonActionStatusEmployee);
        txt_cepEmployee.setOnKeyPressed(this::handleronKeyEnterPressedCEP);

        //Login Events
        btn_saveLogin.setOnMouseClicked(this::handlerButtonActionSaveLogin);
        btn_editarLogin.setOnMouseClicked(this::handlerButtonActionEditLogin);
        btn_criarLogin.setOnMouseClicked(this::handlerButtonActionAddLogin);
        btn_cancelLogin.setOnMouseClicked(this::handlerButtonActionCancelLogin);

        //endregion
        //endregion

        //region Tab "Produtos" Events

        btn_editProduct.setDisable(true);

        MaskFieldUtil.numericField(txt_weightProduct);
        MaskFieldUtil.monetaryField_MAX100(txt_finalPriceProduct);

        //region TableView Products

        //region columns declarations
        setCellsProduct(columnProductName, "nameProduct");
        setCellsProduct(columnProductFinalPrice, "finalPriceProduct");
        setCellsProduct(columnProductWeight, "weightProduct");
        setCellsProduct(columnProductType, "productType");
        setCellsProduct(columnProductStatus, "statusProduct");

        //endregion

        listProduct = Product.ReadAll();
        dataObervableProduct.addAll(listProduct);
        tview_product.setItems(dataObervableProduct);
        //endregion

        //region Combo Box Type Search Supplier
        cbox_typeSearchProduct.getItems().addAll("Nome", "Categoria");
        cbox_typeSearchProduct.getSelectionModel().select(0);
        //endregion

        //region Button Search Product
        btn_searchProduct.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dataObervableProduct.clear();

                if (txt_searchProduct.getText().equals("")) {
                    dataObervableProduct.addAll(Product.ReadAll());
                    return;
                }

                switch (cbox_typeSearchProduct.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        dataObervableProduct.addAll(Product.readByName(txt_searchProduct.getText()));
                        break;

                    case 1:
                        dataObervableProduct.addAll(Product.readByCategory(txt_searchProduct.getText()));
                        break;

                }

            }
        });
        //endregion

        listProductType = ProductType.readAllToString();
        dataObervableProductType.add("");
        dataObervableProductType.addAll(listProductType);
        cbox_categoryProduct.setItems(dataObervableProductType);

        //cbox_categoryProduct.getItems().add("");

        /*for (ProductType productType : ProductType.ReadAll()) {
            cbox_categoryProduct.getItems().add(productType.getNameProductType());
        }*/



        //region TableView Ingredients
        setCellsIngredient(columnIngredientName, "nameIngredient");
        setCellsIngredient(columnIngredientPrice, "priceIngredient");
        setCellsIngredient(columnIngredientStatus, "statusIngredient");


        tview_productIngredient.setItems(dataObervableIngredient);
        //endregion


        //region Product Details
        showProductDetails(null);

        tview_product.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProductDetails(newValue));
        //endregion


        //region Events
        btn_editProductType.setOnMouseClicked(this::handlerButtonActionEditProductType); //Editar Categoria
        btn_editIngredients.setOnMouseClicked(this::handlerButtonActionEditIngredients); //Editar Ingredientes
        btn_listIngredients.setOnMouseClicked(this::handlerButtonActionListIngredients); //Adicionar Ingredients
        btn_dropIngredients.setOnMouseClicked(this::handlerButtonActionDropIngredients);
        btn_editProduct.setOnMouseClicked(this::handlerButtonActionEditProduct); //Editar Produto
        btn_addProduct.setOnMouseClicked(this::handlerButtonActionAddProduct); //Adicionar Produto
        btn_cancelProduct.setOnMouseClicked(this::handlerButtonActionCancelProduct); //Cancelar
        btn_saveProduct.setOnMouseClicked(this::handlerButtonActionSaveProduct); //Salvar
        tbtn_statusProduct.setOnMouseClicked(this::handlerButtonActionStatusProduct); //Seletor de Status
        //btn_searchProduct.setOnMouseClicked(this::handlerButtonActionSearchProduct);
        cbox_categoryProduct.setOnMouseClicked(this::handlerButtonActionCheckBoxProductType);
        //endregion

        //endregion

        //region Tab "Log" Events
        /**
         * tab "Log" ButtonAction
         */
        //endregion

        //endregion

    }

    //region Setup Initial methods
    private final Label createLabel() {
        Label label = new Label();
        VBox.setVgrow(label, Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    //region date & time (include tests)
    class hora implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Calendar now = Calendar.getInstance();
            label_time.setText(String.format("%1$tH:%1$tM:%1$tS", now));
            //lb_time.setText(String.format("%1$tH:%1$tM", now));
        }

    }


    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Calendar now = Calendar.getInstance();
                    label_time.setText(String.format("%1$tH:%1$tM:%1$tS", now));
                }
            });
        }
    };

    private void atualizaHoras() {
        Date agora = new Date();
        label_time.setText(formatador.format(agora));
    }

    private void atualizaHoras2() {
        Calendar now = Calendar.getInstance();
        label_time.setText(String.format("%1$tH:%1$tM:%1$tS", now));
    }
    //endregion

    public static void setUser(User newUser) {
        user = newUser;
    }

    public static User getUser() {
        return user;
    }
    //endregion

    //region Tab "Inicio" methods
    private void handlerHyperlinkActionLogout(MouseEvent event) {
        try {
            Controller.closeApplication(event);
            LoginController.loader().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlerHyperlinkActionFinish(MouseEvent event) {
        try {
            Controller.closeApplication(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Tab "Pedido" methods
    private void handlerButtonActionRemoveProductOnOrder(MouseEvent event) {

        //Tratamento contra não seleção da tabela
        if (!tViewOrderProducts.getSelectionModel().isEmpty()) {
            dataObervableProductsOnCart.remove(tViewOrderProducts.getFocusModel().getFocusedIndex());
        } else if (dataObervableProductsOnCart.size() > 0) {
            dataObervableProductsOnCart.remove(0);
        }
    }

    private void handlerButtonActionAddProductOnOrder(MouseEvent event) {

        //Tratamento contra nada selecionado
        if (!listView_products.getSelectionModel().isEmpty()) {
            dataObervableProductsOnCart.add(productsFinded.get(listView_products.getSelectionModel().getSelectedIndex()));
        }
    }

    private void handlerButtonActionClearOrderProducts(MouseEvent event) {
        totalPrice = 0;
        dataObervableProductsOnCart.clear();
    }

    private void handlerButtonActionNewOrder(MouseEvent event) {
        resetAllComponentsOrder();
    }

    private void handlerButtonActionFinishOrder(MouseEvent event) {
        try {

            if (checkboxOrderCustomer.isSelected() && propertyPerson.getValue() == null) {
                FxDialogs.showWarning("Cliente não selecionado", "Por favor selecione um cliente ou desmarque a caixa de" +
                        " seleção \"Seleção automatica via telefone\"!");
                checkboxOrderCustomer.requestFocus();
                return;

            } else if (dataObervableProductsOnCart.isEmpty() || tfield_adress.getText().equals("") || tfield_name.getText().equals("") || tfield_telephone.getText().equals("")) {
                FxDialogs.showWarning("Pedido em branco", "Por favor adicione produtos e/ou Insira os dados nos campos solicitados!");

                //tfield_name.pseudoClassStateChanged(errorClass, true);

                return;
            }

            Controller controller = new Controller();
            FXMLLoader fxmlLoader = controller.fxmlLoaderContent(FinishSaleController.path);

            FinishSaleController finishSaleController = fxmlLoader.getController();
            ArrayList<Product> products = new ArrayList<>();
            dataObervableProductsOnCart.forEach(product -> products.add(product));

            if (propertyPerson.getValue() != null) {
                finishSaleController.setAllComponents(lbl_total.getText(), propertyPerson.getValue(), products);
            } else {
                finishSaleController.setPrice(lbl_total.getText());
                finishSaleController.setProducts(products);
                finishSaleController.setPerson(null);
            }

            Stage stage = controller.buildStage(fxmlLoader, FinishSaleController.title);
            stage.setOnShowing(onShow());
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    mainAnchorPane.setDisable(false);

                    if (Sale.LAST_ID_SALE != -1) {
                        resetAllComponentsOrder();
                        resetTableViewSale();
                        lbl_codeOfOrder.setText(String.valueOf(Sale.getLastIdSale() + 1));

                        System.out.println(Sale.getLastIdSale());

                        Sale.LAST_ID_SALE = -1;
                    }

                }
            });

            stage.showAndWait();


        } catch (IOException ex) {
            FxDialogs.showWarning(ex.getMessage(), "Tente novamente.");
        }
    }

    private void handlerButtonActionHistoricCustomer(MouseEvent event) {

        try {

            Controller controller = new Controller();
            FXMLLoader fxmlLoader = controller.fxmlLoaderContent(HistoricCustomerController.path);

            HistoricCustomerController historicCustomerController = fxmlLoader.getController();
            historicCustomerController.setPerson(propertyPerson.getValue());

            Stage stage = controller.buildStage(fxmlLoader, HistoricCustomerController.title);
            stage.setOnShowing(onShow());
            stage.setOnCloseRequest(onClose());

            stage.showAndWait();

        } catch (IOException ex) {
            FxDialogs.showWarning(ex.getMessage(), "Tente novamente.");
            ex.printStackTrace();
        }
    }

    private void handlerHyperlinkVerifyRegister(MouseEvent event) {
        searchByTelephone();
    }

    //region default methods
    private void executeSearchProductsOrder(final String t1) {

        Task<ArrayList<Product>> productTask = new Task<ArrayList<Product>>() {
            @Override
            protected ArrayList<Product> call() throws Exception {
                return Product.readByName(t1);
            }
        };

        productTask.setOnSucceeded(event -> {
            productsFinded.clear();
            dataObservableProductsFinded.clear();
            productsFinded = productTask.getValue();
            productsFinded.forEach(product -> dataObservableProductsFinded.add(product.getNameProduct()));

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (txt_searchOrder.getText().equals("")) dataObservableProductsFinded.clear();
                }
            });

        });

        productTask.setOnFailed(event -> {
            productTask.getException().printStackTrace();
        });

        productTask.runningProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Controller.getCurrentStage().getScene().setCursor(Cursor.WAIT);
                } else {
                    Controller.getCurrentStage().getScene().setCursor(Cursor.DEFAULT);
                }
            }
        });

        executor.execute(productTask);

    }

    private void searchByTelephone() {

        propertyPerson.setValue(Phone.searchByTelephone(MaskFieldUtil.onlyDigitsValue(tfield_telephone)));

        if (propertyPerson.getValue().getIdPerson() == 0 || propertyPerson.getValue() == null) {

            if (FxDialogs.showConfirmYesNo("Cliente não cadastrado, deseja cadastrar?", "", FxDialogs.NO, FxDialogs.YES).equals(FxDialogs.YES)) {
                SingleSelectionModel<Tab> selectionModel = paneTab.getSelectionModel();
                selectionModel.select(tabCustomer);
                setCustomerActiveButtons(false, true, "Adicionar");
                txt_phone1Customer.setText(tfield_telephone.getText());
                txt_nameCustomer.setText(tfield_name.getText());
                comeBackSearchByTelephone = true;

                paneTab.getTabs().forEach(tab -> tab.setDisable(true));
                tabCustomer.setDisable(false);

            }

        } else {
            tfield_name.setText(propertyPerson.getValue().getNamePerson());
            tfield_adress.setText(propertyPerson.getValue().getAddress().getStreet() + ", " + propertyPerson.getValue().getAddress().getNeighborhood() + ", " + propertyPerson.getValue().getAddress().getNumber());
            hl_historicCustomer.setDisable(false);
            tfield_telephone.setDisable(true);
            listView_categories.requestFocus();

        }


    }

    private void resetAllComponentsOrder() {
        tfield_name.clear();
        tfield_adress.clear();
        tfield_telephone.clear();
        tarea_orderObservations.clear();
        dataObervableProductsOnCart.clear();

        checkboxOrderCustomer.setSelected(true);

        tfield_name.setDisable(true);
        tfield_telephone.setDisable(false);
        tfield_adress.setDisable(true);
        hyperlinkVerifyRegister.setDisable(false);
        hl_historicCustomer.setDisable(true);

        totalPrice = 0;

        txt_searchOrder.clear();
        listView_categories.setDisable(false);
        listView_categories.requestFocus();

        propertyPerson.setValue(null);
    }

    private void clientDataDisableSearchByTelephone() {
        clearTextFieldsFromOrder();

        tfield_name.setDisable(false);
        tfield_telephone.setDisable(false);
        tfield_adress.setDisable(false);

        hyperlinkVerifyRegister.setDisable(true);

        hl_historicCustomer.setDisable(true);

        propertyPerson.setValue(null);
    }

    private void clientDataEnableSearchByTelephone() {
        clearTextFieldsFromOrder();

        tfield_name.setDisable(true);
        tfield_telephone.setDisable(false);
        tfield_adress.setDisable(true);

        hyperlinkVerifyRegister.setDisable(false);
    }

    private void clearTextFieldsFromOrder() {
        tfield_name.clear();
        tfield_adress.clear();
        tfield_telephone.clear();
    }

    //endregion

    //endregion

    //region Tab "Clientes" methods

    /**
     * @param bool_1 afeta a disponibilidade dos campos de inserção de dados e confirmação
     * @param bool_2 afeta a disponibilidade dos botões Editar e Adicionar
     * @param action seta o Action Employee
     */
    private void setCustomerActiveButtons(Boolean bool_1, Boolean bool_2, String action) {
        group_dataCustomer.setDisable(bool_1);
        group_dataCustomerBtnSaveCancel.setDisable(bool_1);
        group_customerBtnEditNew.setDisable(bool_2);
        if (!action.equals("node"))
            actionCustomer = action;

    }

    private void handlerButtonActionNewCustomer(MouseEvent event) {
        setCustomerActiveButtons(false, true, "Adicionar");
        clearCustomerDetails();
        tbtn_statusCustomer.setSelected(true);
        tbtn_statusCustomer.setText("Ativo");
    }

    private void handlerButtonActionEditCustomer(MouseEvent event) {
        setCustomerActiveButtons(false, true, "Editar");
    }

    private void handlerButtonActionStatusCustomer(MouseEvent event) {
        group_dataCustomerBtnSaveCancel.setDisable(false);
        group_customerBtnEditNew.setDisable(true);
        if (!actionCustomer.equals("Editar"))
            if (!actionCustomer.equals("Adicionar"))
                actionCustomer = "Status";
    }

    private void handlerButtonActionCancelCustomer(MouseEvent event) {

        if (actionCustomer.equals("Status")) {
            if (tbtn_statusCustomer.isSelected()) {
                tbtn_statusCustomer.setSelected(false);

            } else {
                tbtn_statusCustomer.setSelected(true);
            }
        }
        setCustomerActiveButtons(true, false, "");
        paneTab.getTabs().forEach(tab -> tab.setDisable(false));

        comeBackSearchByTelephone = false;
    }

    private void handlerButtonActionSaveCustomer(MouseEvent event) {
        setCustomerActiveButtons(true, false, "node");
        switch (actionCustomer) {
            case "Editar":
                saveCustomer();
                break;
            case "Adicionar":
                newCustomer();
                break;
            case "Status":
                setStatusCustomer();
                break;
            default:
                break;
        }
        actionCustomer = "";
    }

    //region default methods
    private void clearCustomerDetails() {
        txt_nameCustomer.setText("");
        txt_cepCustomer.setText("");
        txt_bairroCustomer.setText("");
        txt_streetCustomer.setText("");
        txt_numberCustomer.setText("");
        txt_phone1Customer.setText("");
        txt_phone2Customer.setText("");
        tbtn_statusCustomer.setSelected(false);
        tbtn_statusCustomer.setText("Inativo");
        idCustomerSelected = 0;
        btn_editCustomer.setDisable(true);
    }

    private void resetTableViewCustomer() {
        dataObservableCustomer.clear();
        dataObservableCustomer.addAll(Customer.ReadAll());
    }

    private void showCustomerDetails(Customer customer) {
        setCustomerActiveButtons(true, false, "");
        tbtn_statusCustomer.setSelected(false);
        if (customer != null) {
            Address address = customer.getAddress();
            ArrayList<Phone> listPhone = customer.getListPhone();
            btn_editCustomer.setDisable(false);
            txt_nameCustomer.setText(customer.getNamePerson());
            //txt_cpfCustomer.setText(customer.getCpf());
            txt_cepCustomer.setText(address.getCep());
            txt_bairroCustomer.setText(address.getNeighborhood());
            txt_streetCustomer.setText(address.getStreet());
            txt_numberCustomer.setText(String.valueOf(address.getNumber()));

            if (listPhone.size() > 0) {
                txt_phone1Customer.setText(listPhone.get(0).getPhone());
            }
            if (listPhone.size() > 1) {
                txt_phone2Customer.setText(listPhone.get(1).getPhone());
            } else {
                txt_phone2Customer.setText("");
            }

            idCustomerSelected = customer.getIdPerson();

            tbtn_statusCustomer.setSelected(customer.getStatus());
            if (customer.getStatus()) tbtn_statusCustomer.setText("Ativo");
            else tbtn_statusCustomer.setText("Inativo");

        } else {
            this.clearCustomerDetails();

        }
    }

    private void saveCustomer() {

        Customer customer = new Customer(idCustomerSelected);
        Address address = customer.getAddress();
        ArrayList<Phone> listPhone = new ArrayList();
        listPhone = customer.getListPhone();

        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        if (listPhone.size() > 0) {
            phone1 = listPhone.get(0);
        }
        if (listPhone.size() > 1) {
            phone2 = listPhone.get(1);
        } else {

        }



        customer.setNamePerson(txt_nameCustomer.getText());
        //customer.setCpf(txt_cpfCustomer.getText());
        customer.setStatus(tbtn_statusCustomer.isSelected());
        address.setCep(MaskFieldUtil.onlyDigitsValue(txt_cepCustomer));
        address.setNeighborhood(txt_bairroCustomer.getText());
        address.setStreet(txt_streetCustomer.getText());
        address.setNumber(Integer.parseInt(txt_numberCustomer.getText()));
        phone1.setPhone(MaskFieldUtil.onlyDigitsValue(txt_phone1Customer));
        phone2.setPhone(MaskFieldUtil.onlyDigitsValue(txt_phone2Customer));

        customer.setAddress(address);
        customer.Save();


        if (listPhone.size() > 0) {
            phone1.Save();
        } else if (!txt_phone1Customer.getText().equals("")) {
            phone1.setIdPerson(customer.getIdPerson());
            phone1.Create();
        }

        if (listPhone.size() > 1) {
            phone2.Save();
        } else if (!txt_phone2Customer.getText().equals("")) {
            phone2.setIdPerson(customer.getIdPerson());
            phone2.Create();
        }

        resetTableViewCustomer();

    }

    private void newCustomer() {
        //Código banco aqui

        if (comeBackSearchByTelephone) {
            SingleSelectionModel<Tab> selectionModel = paneTab.getSelectionModel();
            selectionModel.select(tabOrder);

            System.out.println("Come back called");

            //Passar o obj do novo cliente adicionado
            //person = novo obj cliente

            //habilitar o hyperlink
            newCustomerGeneral();

            if (propertyPerson.getValue() != null) {
                hl_historicCustomer.setDisable(false);
                tfield_telephone.setDisable(true);
                tfield_name.setText(propertyPerson.getValue().getNamePerson());
                tfield_adress.setText(propertyPerson.getValue().getAddress().getStreet() + ", " + propertyPerson.getValue().getAddress().getNeighborhood() + " " + propertyPerson.getValue().getAddress().getNumber());
            }

            paneTab.getTabs().forEach(tab -> tab.setDisable(false));

        } else {
            //só salva o cliente de forma normal sem voltar pra tela de pedido

            newCustomerGeneral();

        }


    }

    private void newCustomerGeneral() {
        Customer customer = new Customer();
        Address address = new Address();
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        customer.setNamePerson(txt_nameCustomer.getText());
        customer.setStatus(tbtn_statusCustomer.isSelected());
        address.setCep(MaskFieldUtil.onlyDigitsValue(txt_cepCustomer));
        address.setNeighborhood(txt_bairroCustomer.getText());
        address.setStreet(txt_streetCustomer.getText());
        address.setNumber(Integer.parseInt(txt_numberCustomer.getText()));
        phone1.setPhone(MaskFieldUtil.onlyDigitsValue(txt_phone1Customer));
        phone2.setPhone(MaskFieldUtil.onlyDigitsValue(txt_phone2Customer));

        customer.setAddress(address);

        customer.setPhone1(phone1);
        customer.setPhone2(phone2);

        customer.Create();

        propertyPerson.setValue(customer);

        this.resetTableViewCustomer();
    }

    private void setStatusCustomer() {
        //Código banco aqui
        Customer customer = new Customer(idCustomerSelected);
        customer.setStatus(tbtn_statusCustomer.isSelected());
        customer.Save();

        resetTableViewCustomer();
    }
    //endregion

    //endregion

    //region Tab "Administração" methods

    //region Tab "Faturamento" methods

    /**
     * tab "Faturamento" methods
     */
    //endregion

    //region Tab "Vendas" methods
    private void resetTableViewSale() {
        dataObservableSale.addAll(Sale.ReadAll());
        txt_searchSale.clear();
    }

    private void executeSearchSaleByName(final String name){

        Task<ArrayList<Sale>> task = new Task<ArrayList<Sale>>() {
            @Override
            protected ArrayList<Sale> call() throws Exception {
                return Sale.readSaleByPersonName(name);
            }
        };

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                dataObservableSale.clear();
                dataObservableSale.addAll(task.getValue());
            }
        });

        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                task.getException().printStackTrace();
            }
        });

        task.runningProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Controller.getCurrentStage().getScene().setCursor(Cursor.WAIT);
                } else {
                    Controller.getCurrentStage().getScene().setCursor(Cursor.DEFAULT);
                }
            }
        });

        executor.execute(task);

    }

    //endregion

    //region Tab "Fornecedores" methods
    /**
     * tab "Fornecedores" methods
     */


    //region default methods

    /**
     * @param bool_1 afeta a disponibilidade dos campos de inserção de dados e confirmação
     * @param bool_2 afeta a disponibilidade dos botões Editar e Adicionar
     * @param action seta o Action Employee
     */
    private void setSupplierActiveButtons(Boolean bool_1, Boolean bool_2, String action) {
        group_dataSupplier.setDisable(bool_1);
        group_supplierBtnSaveCancel.setDisable(bool_1);
        group_supplierBtnEditNew.setDisable(bool_2);
        if (!action.equals("node"))
            actionSupplier = action;
    }

    private void handlerButtonActionNewSupplier(MouseEvent event) {
        setSupplierActiveButtons(false, true, "Adicionar");
        clearSupplierDetails();
        tbtn_statusSupplier.setSelected(true);
        tbtn_statusSupplier.setText("Ativo");

    }

    private void handlerButtonActionEditSupplier(MouseEvent event) {
        setSupplierActiveButtons(false, true, "Editar");
    }

    private void handlerButtonActionStatusSupplier(MouseEvent event) {
        group_supplierBtnSaveCancel.setDisable(false);
        group_supplierBtnEditNew.setDisable(true);
        if (!actionSupplier.equals("Editar"))
            if (!actionSupplier.equals("Adicionar"))
                actionSupplier = "Status";
    }

    private void handlerButtonActionCancelSupplier(MouseEvent event) {

        if (actionSupplier.equals("Status")) {
            if (tbtn_statusSupplier.isSelected()) {
                tbtn_statusSupplier.setSelected(false);

            } else {
                tbtn_statusSupplier.setSelected(true);
            }
        }
        setSupplierActiveButtons(true, false, "");
    }

    private void handlerButtonActionSaveSupplier(MouseEvent event) {
        setSupplierActiveButtons(true, false, "node");
        switch (actionSupplier) {
            case "Editar":
                saveSupplier();
                break;
            case "Adicionar":
                newSupplier();
                break;
            case "Status":
                setStatusSupplier();
                break;
            default:
                break;
        }
        actionSupplier = "";
    }

    /*private void handlerButtonActionSearchSupplier(MouseEvent event) {
        listSupplier = Supplier.ReadAll();
        dataObervableSupplier.clear();
        dataObervableSupplier.addAll(listSupplier);
    }*/
    //endregion

    private void clearSupplierDetails() {
        txt_nameSupplier.setText("");
        txt_cnpjSupplier.setText("");
        txt_cepSupplier.setText("");
        txt_bairroSupplier.setText("");
        txt_streetSupplier.setText("");
        txt_numberSupplier.setText("");
        txt_phone1Supplier.setText("");
        txt_phone2Supplier.setText("");
        tbtn_statusSupplier.setSelected(false);
        tbtn_statusSupplier.setText("Inativo");
        idSupplierSelected = 0;
        btn_editSupplier.setDisable(true);
    }

    private void showSupplierDetails(Supplier supplier) {
        setSupplierActiveButtons(true, false, "");
        tbtn_statusSupplier.setSelected(false);
        if (supplier != null) {
            btn_editSupplier.setDisable(false);
            Address address = supplier.getAddress();
            ArrayList<Phone> listPhone = supplier.getListPhone();

            txt_nameSupplier.setText(supplier.getNamePerson());
            txt_cnpjSupplier.setText(supplier.getCnpj());

            txt_cepSupplier.setText(address.getCep());
            txt_bairroSupplier.setText(address.getNeighborhood());
            txt_streetSupplier.setText(address.getStreet());
            txt_numberSupplier.setText(String.valueOf(address.getNumber()));

            if (listPhone.size() > 0) {
                txt_phone1Supplier.setText(listPhone.get(0).getPhone());
            }
            if (listPhone.size() > 1) {
                txt_phone2Supplier.setText(listPhone.get(1).getPhone());
            } else {
                txt_phone2Supplier.setText("");
            }

            idSupplierSelected = supplier.getIdPerson();

            tbtn_statusSupplier.setSelected(supplier.getStatus());
            if (supplier.getStatus()) tbtn_statusSupplier.setText("Ativo");
            else tbtn_statusSupplier.setText("Inativo");

        } else {
            this.clearSupplierDetails();

        }
    }

    private void saveSupplier() {
        Supplier supplier = new Supplier(idSupplierSelected);
        Address address = supplier.getAddress();
        ArrayList<Phone> listPhone = new ArrayList();
        listPhone = supplier.getListPhone();


        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        if (listPhone.size() > 0) {
            phone1 = listPhone.get(0);
        }
        if (listPhone.size() > 1) {
            phone2 = listPhone.get(1);
        } else {

        }

        supplier.setNamePerson(txt_nameSupplier.getText());
        supplier.setCnpj(MaskFieldUtil.onlyDigitsValue(txt_cnpjSupplier));
        supplier.setStatus(tbtn_statusSupplier.isSelected());
        address.setCep(MaskFieldUtil.onlyDigitsValue(txt_cepSupplier));
        address.setNeighborhood(txt_bairroSupplier.getText());
        address.setStreet(txt_streetSupplier.getText());
        address.setNumber(Integer.parseInt(txt_numberSupplier.getText()));
        phone1.setPhone(MaskFieldUtil.onlyDigitsValue(txt_phone1Supplier));
        phone2.setPhone(MaskFieldUtil.onlyDigitsValue(txt_phone2Supplier));

        supplier.setAddress(address);
        supplier.Save();


        if (listPhone.size() > 0) {
            phone1.Save();
        } else if (!txt_phone1Supplier.getText().equals("")) {
            phone1.setIdPerson(supplier.getIdPerson());
            phone1.Create();
        }

        if (listPhone.size() > 1) {
            phone2.Save();
        } else if (!txt_phone2Supplier.getText().equals("")) {
            phone2.setIdPerson(supplier.getIdPerson());
            phone2.Create();
        }

        this.resetTableViewSupplier();
    }

    private void newSupplier() {
        Supplier supplier = new Supplier();
        Address address = new Address();
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        supplier.setNamePerson(txt_nameSupplier.getText());
        supplier.setCnpj(MaskFieldUtil.onlyDigitsValue(txt_cnpjSupplier));
        supplier.setStatus(tbtn_statusSupplier.isSelected());
        address.setCep(MaskFieldUtil.onlyDigitsValue(txt_cepSupplier));
        address.setNeighborhood(txt_bairroSupplier.getText());
        address.setStreet(txt_streetSupplier.getText());
        address.setNumber(Integer.parseInt(txt_numberSupplier.getText()));
        phone1.setPhone(MaskFieldUtil.onlyDigitsValue(txt_phone1Supplier));
        phone2.setPhone(MaskFieldUtil.onlyDigitsValue(txt_phone2Supplier));

        supplier.setAddress(address);

        supplier.setPhone1(phone1);
        supplier.setPhone2(phone2);


        supplier.Create();


        this.resetTableViewSupplier();

    }

    private void setStatusSupplier() {
        Supplier supplier = new Supplier(idSupplierSelected);
        supplier.setStatus(tbtn_statusSupplier.isSelected());
        supplier.Save();

        this.resetTableViewSupplier();

    }

    private void resetTableViewSupplier() {
        listSupplier = Supplier.ReadAll();
        dataObervableSupplier.clear();
        dataObervableSupplier.addAll(listSupplier);
    }

    //endregion

    //region Tab "Funcionários/Usuários" methods

    //region teste
    private void showEmployeeDetails(Employee employee) {
        setEmployeeActiveButtons(true, false, "");
        if (employee != null) {
            btn_editEmployee.setDisable(false);
            txt_nameEmployee.setText(employee.getNamePerson());
            txt_roleEmployee.setText(employee.getRole());
            txt_cepEmployee.setText(employee.getAddress().getCep());
            txt_bairroEmployee.setText(employee.getAddress().getNeighborhood());
            txt_streetEmployee.setText(employee.getAddress().getStreet());
            txt_numberEmployee.setText(valueOf(employee.getAddress().getNumber()));
            ArrayList<Phone> listPhone;
            listPhone = employee.getListPhone();
            if (listPhone.size() > 0) {
                txt_phone1Employee.setText(listPhone.get(0).getPhone());
            }
            if (listPhone.size() > 1) {
                txt_phone2Employee.setText(listPhone.get(1).getPhone());
            } else {
                txt_phone2Employee.setText("");
            }
            idEmployeeSelected = employee.getIdEmployee();
            idPersonEmployeeSelected = employee.getIdPerson();

            select_typeStatusFunc.setSelected(employee.getStatus());
            if (employee.getStatus()) select_typeStatusFunc.setText("Ativo");
            else select_typeStatusFunc.setText("Inativo");

            User user = new User(idEmployeeSelected);
            if (!user.getLogin().isEmpty()) {
                btn_editarLogin.setDisable(false);
                btn_criarLogin.setDisable(true);
                txt_userLogin.setText(user.getLogin());
                txt_passLogin.setText(user.getPassword());

                if (user.getLevel() == 5) {
                    cbox_typeLevelLogin.setValue("Administrador");
                } else if (user.getLevel() == 4) {
                    cbox_typeLevelLogin.setValue("Operador");
                } else {
                    cbox_typeLevelLogin.setValue("");
                }


                select_loginStatus.setSelected(user.getStatus());
                if (user.getStatus()) select_loginStatus.setText("Ativo");
                else select_loginStatus.setText("Inativo");

                this.selectLoginStatusAction();

            } else {
                btn_editarLogin.setDisable(true);
                btn_criarLogin.setDisable(false);
                txt_userLogin.setText("");
                txt_passLogin.setText("");
                cbox_typeLevelLogin.setValue("");
                select_loginStatus.setSelected(false);
                select_loginStatus.setText("Inativo");
                this.selectLoginStatusAction();
            }


        } else {
            // Employee é null, remove todo o texto.
            this.clearEmployeeDetails();


        }
    }
    //endregion

    private void clearEmployeeDetails() {
        txt_nameEmployee.setText("");
        txt_roleEmployee.setText("");
        txt_cepEmployee.setText("");
        txt_bairroEmployee.setText("");
        txt_streetEmployee.setText("");
        txt_numberEmployee.setText("");
        txt_phone1Employee.setText("");
        txt_phone2Employee.setText("");
        txt_userLogin.setText("");
        txt_passLogin.setText("");
        btn_editarLogin.setDisable(true);
        btn_criarLogin.setDisable(false);
        cbox_typeLevelLogin.setValue("");
        select_loginStatus.setSelected(false);
        select_typeStatusFunc.setSelected(false);
        select_typeStatusFunc.setText("Inativo");
        this.selectLoginStatusAction();
        idEmployeeSelected = 0;
        btn_editEmployee.setDisable(true);
    }

    private void resetTableViewEmployee() {
        listEmployee = Employee.ReadAll();
        dataObervableEmployee.clear();
        dataObervableEmployee.addAll(listEmployee);
    }

    //region default methods
    private void handlerButtonActionManageUsers(MouseEvent event) {
        try {
            //ManageUsersController.loader().show();

            Controller controller = new Controller();
            FXMLLoader fxmlLoader = controller.fxmlLoaderContent(ManageUsersController.path);
            Stage stage = controller.buildStage(fxmlLoader, ManageUsersController.title);
            stage.setResizable(false);
            stage.setOnShowing(onShow());
            stage.setOnCloseRequest(onClose());
            stage.showAndWait();
        } catch (IOException ex) {
            FxDialogs.showWarning(ex.getMessage(), "Tente novamente.");
        }
    }

    private void selectLoginStatusAction() {
        if (select_loginStatus.isSelected()) {
            select_loginStatus.setText("Habilitado");
            tp_login.setVisible(true);
            tp_login.setDisable(false);
        } else {
            select_loginStatus.setText("Desabilitado");
            tp_login.setVisible(false);
            tp_login.setDisable(true);
        }
        if (!actionEmployee.equals("Editar"))
            if (!actionEmployee.equals("Adicionar"))
                actionEmployee = "Login";


    }

    private void handlerButtonSelectLoginStatus(MouseEvent event) {
        this.selectLoginStatusAction();
        group_dataFunc_btn.setDisable(false);
    }

    /**
     * @param bool_1 afeta a disponibilidade dos campos de inserção de dados e confirmação
     * @param bool_2 afeta a disponibilidade dos botões Editar e Adicionar
     * @param action seta o Action Employee
     */
    private void setEmployeeActiveButtons(Boolean bool_1, Boolean bool_2, String action) {
        group_dataFunc.setDisable(bool_1);
        group_dataFunc_btn.setDisable(bool_1);
        group_editAddFunc.setDisable(bool_2);
        if (!action.equals("node"))
            actionEmployee = action;

    }

    private void handlerButtonActionAddEmployee(MouseEvent event) {
        setEmployeeActiveButtons(false, true, "Adicionar");
        this.clearEmployeeDetails();
        select_typeStatusFunc.setSelected(true);
        select_typeStatusFunc.setText("Ativo");

    }

    private void handlerButtonActionEditEmployee(MouseEvent event) {
        setEmployeeActiveButtons(false, true, "Editar");
    }

    private void handlerButtonActionStatusEmployee(MouseEvent event) {
        group_dataFunc_btn.setDisable(false);
        group_editAddFunc.setDisable(true);
        if (!actionEmployee.equals("Editar"))
            if (!actionEmployee.equals("Adicionar"))
                actionEmployee = "Status";
    }

    private void handlerButtonActionCancelEmployee(MouseEvent event) {

        if (actionEmployee.equals("Status")) {
            select_typeStatusFunc.setSelected(!select_typeStatusFunc.isSelected());
        }
        if (actionEmployee.equals("Login")) {
            select_loginStatus.setSelected(!select_loginStatus.isSelected());
        }
        setEmployeeActiveButtons(true, false, "");
    }

    private void handlerButtonActionSaveEmployee(MouseEvent event) {
        setEmployeeActiveButtons(true, false, "node");
        switch (actionEmployee) {
            case "Editar":
                saveEmployee();
                break;
            case "Adicionar":
                addEmployee();
                break;
            case "Status":
                setStatusEmployee();
                break;
            case "Login":
                try {
                    setStatusLogin();
                } catch (UserException ex) {
                    FxDialogs.showException("Erro ao salvar Usuário", ex.getClass() + " - " + ex.getMessage(), ex);
                }
                break;
            default:
                break;
        }
        actionEmployee = "";
    }

    //Login methods
    private void setLoginActiveButtons(Boolean bool_1, Boolean bool_2, String action) {
        group_dataUser.setDisable(bool_1);
        group_dataUserBtnEditarCriar.setDisable(bool_2);

        if (!action.equals("node"))
            actionLogin = action;

    }

    private void handlerButtonActionAddLogin(MouseEvent event) {
        setLoginActiveButtons(false, true, "Adicionar");
    }

    private void handlerButtonActionEditLogin(MouseEvent event) {
        setLoginActiveButtons(false, true, "Editar");
    }

    private void handlerButtonActionCancelLogin(MouseEvent event) {
        setLoginActiveButtons(true, false, "");
        User user = new User(idEmployeeSelected);
        if (!user.getLogin().isEmpty()) {
            btn_editarLogin.setDisable(false);
            btn_criarLogin.setDisable(true);
        } else {
            btn_editarLogin.setDisable(true);
            btn_criarLogin.setDisable(false);
        }
    }

    private void handlerButtonActionSaveLogin(MouseEvent event) {
        setLoginActiveButtons(true, false, "node");
        switch (actionLogin) {
            case "Editar":
                try {
                    saveLogin();
                } catch (UserException ex) {
                    FxDialogs.showException("Erro ao salvar Usuário", ex.getClass() + " - " + ex.getMessage(), ex);
                }
                break;
            case "Adicionar":
                try {
                    addLogin();
                } catch (UserException ex) {
                    FxDialogs.showException("Erro ao criar Usuário", ex.getClass() + " - " + ex.getMessage(), ex);
                }
                break;
            default:
                break;
        }
        actionLogin = "";
    }
    //endregion

    private void saveEmployee() {
        //Código banco aqui
        Employee employee = new Employee(idPersonEmployeeSelected, idEmployeeSelected);
        Address address = new Address(employee.getIdAddress());
        ArrayList<Phone> listPhone = new ArrayList();
        listPhone = employee.getListPhone();
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        if (listPhone.size() > 0) {
            phone1 = listPhone.get(0);
        }
        if (listPhone.size() > 1) {
            phone2 = listPhone.get(1);
        } else {

        }

        employee.setNamePerson(txt_nameEmployee.getText());
        employee.setRole(txt_roleEmployee.getText());
        employee.setStatus(select_typeStatusFunc.isSelected());
        address.setCep(MaskFieldUtil.onlyDigitsValue(txt_cepEmployee));
        address.setNeighborhood(txt_bairroEmployee.getText());
        address.setStreet(txt_streetEmployee.getText());
        address.setNumber(Integer.parseInt(txt_numberEmployee.getText()));
        phone1.setPhone(MaskFieldUtil.onlyDigitsValue(txt_phone1Employee));
        phone2.setPhone(MaskFieldUtil.onlyDigitsValue(txt_phone2Employee));

        employee.setAddress(address);
        employee.Save();


        if (listPhone.size() > 0) {
            phone1.Save();
        } else if (!txt_phone1Employee.getText().equals("")) {
            phone1.setIdPerson(employee.getIdPerson());
            phone1.Create();
        }

        if (listPhone.size() > 1) {
            phone2.Save();
        } else if (!txt_phone2Employee.getText().equals("")) {
            phone2.setIdPerson(employee.getIdPerson());
            phone2.Create();
        }


        this.resetTableViewEmployee();

    }

    private void addEmployee() {
        //Código banco aqui
        Employee employee = new Employee();
        Address address = new Address();
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        employee.setNamePerson(txt_nameEmployee.getText());
        employee.setRole(txt_roleEmployee.getText());
        employee.setStatus(select_typeStatusFunc.isSelected());
        address.setCep(MaskFieldUtil.onlyDigitsValue(txt_cepEmployee));
        address.setNeighborhood(txt_bairroEmployee.getText());
        address.setStreet(txt_streetEmployee.getText());
        address.setNumber(Integer.parseInt(txt_numberEmployee.getText()));
        phone1.setPhone(MaskFieldUtil.onlyDigitsValue(txt_phone1Employee));
        phone2.setPhone(MaskFieldUtil.onlyDigitsValue(txt_phone2Employee));


        employee.setAddress(address);

        employee.setPhone1(phone1);
        employee.setPhone2(phone2);

        employee.Create();


        this.resetTableViewEmployee();


    }

    private void setStatusEmployee() {
        Employee employee = new Employee(idPersonEmployeeSelected, idEmployeeSelected);
        employee.setStatus(select_typeStatusFunc.isSelected());
        employee.Save();

        this.resetTableViewEmployee();

    }


    private void setStatusLogin() throws UserException {
        saveLogin();
    }

    //Login methods
    private void saveLogin() throws UserException {
        //Código banco aqui
        User user = new User(idEmployeeSelected);
        String level = cbox_typeLevelLogin.getSelectionModel().getSelectedItem();

        if (!user.getLogin().isEmpty() || !user.getPassword().isEmpty() || !level.equals("")) {

            user.setLogin(txt_userLogin.getText());
            user.setPassword(txt_passLogin.getText());


            if (level.equals("Administrador")) {
                user.setLevel(5);
            } else if (level.equals("Operador")) {
                user.setLevel(4);
            }

            user.setStatus(select_loginStatus.isSelected());


            user.SaveByIdEmployee();

        } else {
            if (user.getLogin().isEmpty() && user.getPassword().isEmpty()) {
                throw new UserException("Digite um login e uma senha!") {
                };
            } else if (user.getLogin().isEmpty()) {
                throw new UserException("Digite um login!") {
                };
            } else if (user.getPassword().isEmpty()) {
                throw new UserException("Digite uma senha!") {
                };

            } else if (level.equals("")) {
                throw new UserException("Escolha um nivel de acesso!") {
                };
            }
        }
    }

    private void addLogin() throws UserException {
        //Código banco aqui
        User user = new User();
        user.setIdEmployee(idEmployeeSelected);
        user.setLogin(txt_userLogin.getText());
        user.setPassword(txt_passLogin.getText());
        String level = cbox_typeLevelLogin.getSelectionModel().getSelectedItem();
        if (level.equals("Administrador")) {
            user.setLevel(5);
        } else if (level.equals("Operador")) {
            user.setLevel(4);
        }

        user.setStatus(select_loginStatus.isSelected());

        if (!user.getLogin().isEmpty() || !user.getPassword().isEmpty() || !level.equals("")) {
            user.Create();
        } else {
            if (user.getLogin().isEmpty() && user.getPassword().isEmpty()) {
                throw new UserException("Digite um login e uma senha!") {
                };
            } else if (user.getLogin().isEmpty()) {
                throw new UserException("Digite um login!") {
                };
            } else if (user.getPassword().isEmpty()) {
                throw new UserException("Digite uma senha!") {
                };

            } else if (level.equals("")) {
                throw new UserException("Escolha um nivel de acesso!") {
                };
            }
        }

    }


    //endregion

    //region Tab "Produtos" methods

    /**
     * tab "Produtos" methods
     */

    //region default methods
    public static void addIngredient(Ingredient ingredient) {
        boolean flag = true;
        for (Ingredient ingredient2 : dataObervableIngredient) {
            if (ingredient.getIdIngredient() == ingredient2.getIdIngredient()) {
                flag = false;
            }
        }
        if (flag) dataObervableIngredient.add(ingredient);
    }


    private void handlerButtonActionCheckBoxProductType(MouseEvent event) {
        if (refreshCategory) {
            //cbox_categoryProduct.getItems().clear();
            dataObervableProductType.clear();
            listProductType = ProductType.readAllToString();
            dataObervableProductType.add("");
            dataObervableProductType.addAll(listProductType);

            //resetTableViewProduct();
            refreshCategory = false;
        }
    }

    private void handlerButtonActionEditProductType(MouseEvent event) {
        try {
            //EditProductTypeController.loader().show();
            Controller controller = new Controller();
            FXMLLoader fxmlLoader = controller.fxmlLoaderContent(EditProductTypeController.path);

            Stage stage = controller.buildStage(fxmlLoader, EditProductTypeController.title);
            stage.setResizable(false);
            stage.setOnShowing(onShow());
            stage.setOnCloseRequest(onClose());
            stage.showAndWait();



        } catch (IOException ex) {
            FxDialogs.showWarning(ex.getMessage(), "Tente novamente.");
        } finally {

        }
    }

    private void handlerButtonActionEditIngredients(MouseEvent event) {
        try {
            //EditIngredientsController.loader().show();

            Controller controller = new Controller();
            FXMLLoader fxmlLoader = controller.fxmlLoaderContent(EditIngredientsController.path);

            Stage stage = controller.buildStage(fxmlLoader, EditIngredientsController.title);
            stage.setResizable(false);
            stage.setOnShowing(onShow());
            stage.setOnCloseRequest(onClose());
            stage.showAndWait();

        } catch (IOException ex) {
            FxDialogs.showWarning(ex.getMessage(), "Tente novamente.");
        }
    }

    private void handlerButtonActionListIngredients(MouseEvent event) {
        try {
            //ListIngredientsController.loader().show();

            Controller controller = new Controller();
            FXMLLoader fxmlLoader = controller.fxmlLoaderContent(ListIngredientsController.path);
            Stage stage = controller.buildStage(fxmlLoader, ListIngredientsController.title);
            stage.setResizable(false);
            stage.setOnShowing(onShow());
            stage.setOnCloseRequest(onClose());
            stage.showAndWait();
        } catch (IOException ex) {
            FxDialogs.showWarning(ex.getMessage(), "Tente novamente.");
        }
    }

    private void handlerButtonActionDropIngredients(MouseEvent event) {
        dataObervableIngredient.remove(tview_productIngredient.getSelectionModel().getSelectedItem());

    }

    /**
     * @param bool_1 afeta a disponibilidade dos campos de inserção de dados e confirmação
     * @param bool_2 afeta a disponibilidade dos botões Editar, Adicionar e Excluir
     * @param action seta o Action Product
     */
    private void setProductActiveButtons(Boolean bool_1, Boolean bool_2, String action) {
        group_dataProduct.setDisable(bool_1);
        group_addDropIngredients.setDisable(bool_1);
        group_cancelSaveProduct.setDisable(bool_1);
        group_editAddProduct.setDisable(bool_2);
        if (!action.equals("node"))
            actionProduct = action;

    }

    private void handlerButtonActionEditProduct(MouseEvent event) {
        setProductActiveButtons(false, true, "Editar");
    }

    private void handlerButtonActionAddProduct(MouseEvent event) {
        setProductActiveButtons(false, true, "Adicionar");
        clearProductDetails();
        tbtn_statusProduct.setSelected(true);
        tbtn_statusProduct.setText("Ativo");

    }

    private void handlerButtonActionCancelProduct(MouseEvent event) {

        if (actionProduct.equals("Status")) {
            if (tbtn_statusProduct.isSelected()) {
                tbtn_statusProduct.setSelected(false);

            } else {
                tbtn_statusProduct.setSelected(true);
            }
        }
        setProductActiveButtons(true, false, "");
    }

    private void handlerButtonActionSaveProduct(MouseEvent event) {
        setProductActiveButtons(true, false, "node");
        switch (actionProduct) {
            case "Editar":
                saveProduct();
                break;
            case "Adicionar":
                addProduct();
                break;
            case "Status":
                setStatusProduct();
                break;
            default:
                break;
        }
        actionProduct = "";
    }

    private void handlerButtonActionStatusProduct(MouseEvent event) {
        group_cancelSaveProduct.setDisable(false);
        group_editAddProduct.setDisable(true);
        if (!actionProduct.equals("Editar"))
            if (!actionProduct.equals("Adicionar"))
                actionProduct = "Status";
        if (tbtn_statusProduct.isSelected()) tbtn_statusProduct.setText("Ativo");
        else tbtn_statusProduct.setText("Inativo");
    }

    /*private void handlerButtonActionSearchProduct(MouseEvent event) {
        listProduct = Product.ReadAll();
        dataObervableProduct.clear();
        dataObervableProduct.addAll(listProduct);
    }*/
    //endregion

    private void clearProductDetails() {
        txt_nameProduct.setText("");
        txt_finalPriceProduct.setText("");
        txt_weightProduct.setText("");
        //imageview_product
        cbox_categoryProduct.setValue("");
        resetTableViewProductIngredient();
        tbtn_statusProduct.setSelected(false);
        tbtn_statusProduct.setText("Inativo");
        idProductSelected = 0;
        btn_editProduct.setDisable(true);
    }

    private void resetTableViewProduct() {
        listProduct = Product.ReadAll();
        dataObervableProduct.clear();
        dataObervableProduct.addAll(listProduct);
    }

    private void resetTableViewProductIngredient() {
        dataObervableIngredient.clear();
    }

    private void showProductDetails(Product product) {
        setProductActiveButtons(true, false, "");
        tbtn_statusProduct.setSelected(false);
        if (product != null) {
            btn_editProduct.setDisable(false);
            ProductType productType = product.getProductType();
            ArrayList<Ingredient> listIngredient = product.getListIngredients();

            txt_nameProduct.setText(product.getNameProduct());
            txt_finalPriceProduct.setText(valueOf(product.getFinalPriceProduct()));
            txt_weightProduct.setText(valueOf(product.getWeightProduct()));
            //imageview_product
            cbox_categoryProduct.setValue(productType.getNameProductType());

            idProductSelected = product.getIdProduct();

            tbtn_statusProduct.setSelected(product.getStatusProduct());
            if (product.getStatusProduct()) tbtn_statusProduct.setText("Ativo");
            else tbtn_statusProduct.setText("Inativo");

            listIngredient = product.getListIngredients();
            dataObervableIngredient.clear();
            dataObervableIngredient.addAll(listIngredient);

        } else {
            this.clearProductDetails();

        }
    }

    private void saveProduct() {
        Product product = new Product(idProductSelected);
        ProductType productType;
        ArrayList<Ingredient> listIngredient = new ArrayList<>();


        product.setNameProduct(txt_nameProduct.getText());
        product.setFinalPriceProduct(Float.parseFloat(txt_finalPriceProduct.getText()));
        product.setWeightProduct(Float.parseFloat(txt_weightProduct.getText()));
        productType = new ProductType(cbox_categoryProduct.getSelectionModel().getSelectedItem());
        product.setIdProductType(productType.getIdProductType());
        listIngredient.addAll(dataObervableIngredient);
        product.setStatusProduct(tbtn_statusProduct.isSelected());
        product.Save();
        ProductIngredient.saveListProductIngredient(listIngredient, idProductSelected);

        resetTableViewProduct();
    }

    private void addProduct() {
        Product product = new Product();
        ProductType productType;
        ArrayList<Ingredient> listIngredient = new ArrayList<>();

        product.setNameProduct(txt_nameProduct.getText());
        product.setFinalPriceProduct(Float.parseFloat(txt_finalPriceProduct.getText()));
        product.setWeightProduct(Float.parseFloat(txt_weightProduct.getText()));
        productType = new ProductType(cbox_categoryProduct.getSelectionModel().getSelectedItem());
        product.setIdProductType(productType.getIdProductType());
        listIngredient.addAll(dataObervableIngredient);
        product.setStatusProduct(tbtn_statusProduct.isSelected());
        product.Create();
        ProductIngredient.saveListProductIngredient(listIngredient, product.getIdProduct());

        resetTableViewProduct();
    }

    private void setStatusProduct() {
        //Código banco aqui
        Product product = new Product(idProductSelected);
        product.setStatusProduct(tbtn_statusProduct.isSelected());
        product.Save();

        resetTableViewProduct();

    }

    //endregion

    //region Tab "Log" methods

    /**
     * tab "Log" methods
     */
    //endregion

    //endregion

    //region Util Methods

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

    private TabPane findTabPaneForNode(Node node) {
        TabPane tabPane = null;

        for (Node n = node.getParent(); n != null && tabPane == null; n = n.getParent()) {
            if (n instanceof TabPane) {
                tabPane = (TabPane) n;
            }
        }

        return tabPane;
    }

    //region CEP Experimental
    private void onKeyEnterPressedCEP(String CEP, JFXTextField street, JFXTextField neighborhood) {

        /**
         * Sequence:
         *
         * Rua
         * Bairro
         */

        Endereco endereco = ViaCEP.buscarCep(CEP);

        if (endereco != null) {
            street.setText(endereco.getRua());
            neighborhood.setText(endereco.getBairro());
        }


    }

    private void handleronKeyEnterPressedCEP(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {


            if (paneTab.getSelectionModel().getSelectedItem().getText().equals("Administração")) {
                switch (tablePaneAdm.getSelectionModel().getSelectedItem().getText()) {
                    case "Fornecedores":
                        onKeyEnterPressedCEP(MaskFieldUtil.onlyDigitsValue(txt_cepSupplier), txt_streetSupplier, txt_bairroSupplier);
                        break;

                    case "Funcionários/Usuários":
                        onKeyEnterPressedCEP(MaskFieldUtil.onlyDigitsValue(txt_cepEmployee), txt_streetEmployee, txt_bairroEmployee);
                        break;
                }
            } else {
                onKeyEnterPressedCEP(MaskFieldUtil.onlyDigitsValue(txt_cepCustomer), txt_streetCustomer, txt_bairroCustomer);
            }
        }
    }

    //endregion

    /**
     * Set Cell Factory Wrapper -  Method designed to turn Multi Object Table into a generic exibithion of Data
     */
    private void setCells(TableColumn<Person, Person> columnDefault, String opc) {

        columnDefault.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue()));

        columnDefault.setCellFactory(column -> new TableCell<Person, Person>() {
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
            public void updateItem(Person person, boolean empty) {
                if (person == null) {
                    setGraphic(null);
                } else {
                    String text = "";

                    switch (opc) {
                        case "namePerson":
                            text = person.getNamePerson();
                            break;

                        case "phone1":
                            if (person.getListPhone().size() > 0) text = person.getListPhone().get(0).getPhone();
                            break;

                        case "phone2":
                            if (person.getListPhone().size() > 1) text = person.getListPhone().get(1).getPhone();
                            break;

                        case "cep":
                            text = person.getAddress().getCep();
                            break;

                        case "status":
                            text = (person.getStatus()) ? "Ativo" : "Inativo";
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

    private void setCellsProduct(TableColumn<Product, Product> columnDefault, String opc) {

        columnDefault.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue()));

        columnDefault.setCellFactory(column -> new TableCell<Product, Product>() {
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
            public void updateItem(Product product, boolean empty) {
                if (product == null) {
                    setGraphic(null);
                } else {
                    String text = "";

                    switch (opc) {
                        case "nameProduct":
                            text = product.getNameProduct();
                            break;

                        case "finalPriceProduct":
                            text = "R$ " + product.getFinalPriceProduct();
                            break;

                        case "weightProduct":
                            text = Float.toString(product.getWeightProduct());
                            break;

                        case "productType":
                            text = product.getProductType().getNameProductType();
                            break;

                        case "statusProduct":
                            text = (product.getStatusProduct()) ? "Ativo" : "Inativo";
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

    private void setCellsIngredient(TableColumn<Ingredient, Ingredient> columnDefault, String opc) {

        columnDefault.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue()));

        columnDefault.setCellFactory(column -> new TableCell<Ingredient, Ingredient>() {
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
            public void updateItem(Ingredient ingredient, boolean empty) {
                if (ingredient == null) {
                    setGraphic(null);
                } else {
                    String text = "";

                    switch (opc) {
                        case "nameIngredient":
                            text = ingredient.getNameIngredient();
                            break;

                        case "priceIngredient":
                            text = "R$ " + ingredient.getPrice();
                            break;

                        case "statusIngredient":
                            text = (ingredient.getStatusIngredient()) ? "Ativo" : "Inativo";
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

    //region Controller Stage Window Show and Close request
    public EventHandler<WindowEvent> onShow() {
        return new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                mainAnchorPane.setDisable(true);
            }
        };
    }

    public EventHandler<WindowEvent> onClose() {
        return new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                mainAnchorPane.setDisable(false);
            }
        };
    }
    //endregion

    //endregion

    //--------------------------------------------------
    public static Stage loader() throws IOException {

        return Controller.loader(DashboardController.class, StageStyle.DECORATED, path, title);

    }


}
