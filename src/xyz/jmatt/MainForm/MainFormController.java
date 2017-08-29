package xyz.jmatt.MainForm;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainFormController extends MenuItem implements Initializable {
    @FXML
    private BorderPane MainForm;
    @FXML
    private MenuBar MainMenuBar;
    @FXML
    private TableView TransactionTable;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //myList.add("hello");
        
        //final Menu menu1 = new Menu("File");
        //menu1.show();
        //MenuBar menuBar = new MenuBar();
        //menuBar.getMenus().add(menu1);
        //MainForm.getChildren().add(menuBar);
    }
}
