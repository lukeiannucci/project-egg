package xyz.jmatt.MainForm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import xyz.jmatt.models.TransactionModel;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class MainFormController extends MenuItem implements Initializable {
    @FXML
    private BorderPane MainForm;
    @FXML
    private MenuBar MainMenuBar;
    @FXML
    private TableView TransactionTable;
    @FXML
    private TableColumn Name;
    @FXML
    private TableColumn Category;
    @FXML
    private TableColumn Amount;
    @FXML
    private TableColumn Date;
    @FXML
    private TableColumn Total;
    @FXML
    private Button AddTransaction;
    @FXML
    private TextField TransIn;
    @FXML
    private TextField CategoryIn;
    @FXML
    private TextField AmountIn;
    @FXML
    private TextField DateIn;

    private ObservableList<TransactionModel> data;
    @FXML
    private void AddTransactionModel()
    {
        data.add(new TransactionModel(TransIn.getText(),CategoryIn.getText(),new BigDecimal(AmountIn.getText()), Long.valueOf(DateIn.getText())));
        TransIn.setText("");
        CategoryIn.setText("");
        AmountIn.setText("");
        DateIn.setText("");

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<TransactionModel> myList = new ArrayList<TransactionModel>();
        myList.add(new TransactionModel("McDicks", "Food", new BigDecimal(1), 1));
        myList.add(new TransactionModel("Gym", "Sports", new BigDecimal(2), 2));
        myList.add(new TransactionModel("McDicks", "Food", new BigDecimal(3), 3));

        data = FXCollections.observableArrayList(
                myList
        );
        Name.setCellValueFactory(
                new PropertyValueFactory<TransactionModel, String>("Name")
        );
        Category.setCellValueFactory(
                new PropertyValueFactory<TransactionModel, String>("Category")
        );
        Amount.setCellValueFactory(
                new PropertyValueFactory<TransactionModel, BigDecimal>("Amount")
        );
        Date.setCellValueFactory(
                new PropertyValueFactory<TransactionModel, Long>("Date")
        );
        Total.setCellValueFactory(
                new PropertyValueFactory<TransactionModel, String>("Total")
        );
        TransactionTable.setItems(data);
        //myList.add("hello");

        //final Menu menu1 = new Menu("File");
        //menu1.show();
        //MenuBar menuBar = new MenuBar();
        //menuBar.getMenus().add(menu1);
        //MainForm.getChildren().add(menuBar);
    }
}
