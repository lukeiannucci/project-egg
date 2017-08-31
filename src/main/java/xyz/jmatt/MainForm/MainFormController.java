package xyz.jmatt.MainForm;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import xyz.jmatt.models.Category;
import xyz.jmatt.models.TransactionModel;
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
    @FXML
    private TreeTableView CategoryTreeTableView;
    @FXML
    private TreeTableColumn TreeCategory;

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
        List<String> Categories = new ArrayList<>();
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

//        TreeItem<Category> root = new TreeItem<>();
//        //root.add(new TreeItem<>(new Category("Show Categories")));
//        List<TreeItem<Category>> CategoryList = new ArrayList<>();
//        for(int i = 0; i < data.size(); i++){
//            xyz.jmatt.models.Category myCat = new Category(data.get(i).getCategory());
//            myCat.Categories.add("Food");
//            myCat.Categories.add("Sports");
//            myCat.Categories.add("Entertainment");
//            myCat.Categories.add("Auto");
//            myCat.Categories.add("Health");
//            myCat.Categories.add("Utilities");
//            myCat.Categories.add("Misc");
//            CategoryList.add(new TreeItem<Category>(myCat));
//            //root.add(new TreeItem<Category>(myCat));
//            //root.get(i).getChildren().add(new TreeItem<Category>(myCat.getSubcategories().get(i)));
//            //test.add(Test(data.get(i).getCategory()));
//        }
//        root.getChildren().setAll(CategoryList);
//        TreeCategory.setCellValueFactory(new TreeItemPropertyValueFactory<Category, String>("Categories"));
//        CategoryTreeTableView.setRoot(root);
        //List<TreeItem<String>> root = new ArrayList<>();
    }
}

