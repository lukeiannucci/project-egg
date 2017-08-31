package xyz.jmatt.MainForm;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import xyz.jmatt.models.Category;
import xyz.jmatt.models.TransactionModel;
import xyz.jmatt.services.CategoryService;
import xyz.jmatt.services.TransactionService;

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
    private DatePicker DateIn;

    private ObservableList<TransactionModel> data;
    @FXML
    private void AddTransactionModel()
    {
        //TODO fix the date
        TransactionModel trans = new TransactionModel(TransIn.getText(),CategoryIn.getText(),new BigDecimal(AmountIn.getText()), 1);
        new TransactionService().addTransaction(trans);
        data.add(trans);
        TransIn.setText("");
        CategoryIn.setText("");
        AmountIn.setText("");
        DateIn.getEditor().setText("");


    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = FXCollections.observableArrayList(
                new TransactionService().getAllTransactions()
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

        TransactionTable.skinProperty().addListener((observable, oldValue, newValue) -> {
            final TableHeaderRow header = (TableHeaderRow)TransactionTable.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable1, oldValue1, newValue1) -> {header.setReordering(false);});
        });
        TransactionTable.setItems(data);

        //temp
        Category category = new CategoryService().getAllCategories();
        System.out.println();
    }
}
