package xyz.jmatt.MainForm;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import xyz.jmatt.models.Category;
import xyz.jmatt.models.TransactionModel;
import xyz.jmatt.services.CategoryService;
import xyz.jmatt.services.TransactionService;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class MainFormController extends MenuItem implements Initializable {
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
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
    @FXML
    private Label AddedCategory;
    @FXML
    private TextField CategoryInput;

    private TreeItem<Category> root;

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

    @FXML
    private void OnLabelDragDetected(MouseEvent event)
    {
//        Dragboard db = AddedCategory.startDragAndDrop(TransferMode.MOVE);
//        ClipboardContent content = new ClipboardContent();
//        //TreeItem<Category> test = (AddedCategoryetSelectedItem();
//        content.putString(AddedCategory.getText());
//        db.setContent(content);
//        event.consume();
//        System.out.println(db.getString());
    }

    @FXML
    private void OnDragEntered(DragEvent event)
    {
//        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
//        event.consume();
//        System.out.println(event.getDragboard().getString());
    }

    @FXML
    private void OnDragDropped(DragEvent event)
    {
        //System.out.println("Drag Dropped");
//        Dragboard db = event.getDragboard();
//        if(event.getDragboard().hasString()){
//            System.out.println("Drag Dropped");
//            //CategoryTreeTableView.
//            //TreeItem<Category> test = CategoryTreeTableView.getRow(event.)
//            root.getChildren().add(new TreeItem<Category>(new Category(db.getString())));
//        }
//        event.setDropCompleted(true);
//        event.consume();
    }

    @FXML
    private void GetRow(MouseEvent e){

    }

    @FXML
    private void AddCategory()
    {
        root.getChildren().add(new TreeItem<Category>(new Category(CategoryInput.getText())));
        //AddedCategory.setText(CategoryInput.getText());
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

        //temp
        Category category = new CategoryService().getAllCategories();
        System.out.println();
        TreeItem<Category> returnTree = new TreeItem<Category>(category);
        List<Category> node = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        List<TreeItem<Category>> findNode = new ArrayList<>();
        root = ReadCategory(category, returnTree, node, findNode);
        CategoryTreeTableView.setRowFactory(new Callback<TreeTableView, TreeTableRow<Category>>() {
            @Override
            public TreeTableRow<Category> call(TreeTableView param) {
                final TreeTableRow<Category> row = new TreeTableRow<>();
                row.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        TreeItem<Category> selectedItem = (TreeItem<Category>)CategoryTreeTableView.getSelectionModel().getSelectedItem();
                        if(selectedItem != null){
                            Dragboard db = CategoryTreeTableView.startDragAndDrop(TransferMode.MOVE);
                            db.setDragView(row.snapshot(null, null));
                            ClipboardContent content = new ClipboardContent();
                            content.put(SERIALIZED_MIME_TYPE, row.getIndex());
                            db.setContent(content);
                            event.consume();
                            //System.out.println(db.getString());
                        }
                    }
                });
                row.setOnDragOver(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        Dragboard db = event.getDragboard();
                        if(event.getDragboard().hasContent(SERIALIZED_MIME_TYPE)){
                            event.acceptTransferModes(TransferMode.MOVE);
                        }
                        event.consume();
                    }
                });
                row.setOnDragDropped(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        Dragboard db = event.getDragboard();
                        boolean success = false;
                        if(event.getDragboard().hasContent(SERIALIZED_MIME_TYPE)){
                            int index = (Integer)db.getContent(SERIALIZED_MIME_TYPE);
                            //int dropIndex = row.getIndex();


                            TreeItem<Category> droppedon = row.getTreeItem();
                            if(droppedon != null){
                                TreeItem<Category> removeItem = CategoryTreeTableView.getTreeItem(index);
                                removeItem.getParent().getChildren().remove(removeItem);
                                System.out.println(droppedon.getValue().getName());
                                droppedon.getChildren().add(removeItem);
                            }

                            //((TreeItem<xyz.jmatt.models.Category>) CategoryTreeTableView.getSelectionModel().getSelectedItem()).getParent().getChildren().remove(CategoryTreeTableView.getSelectionModel().getSelectedItem());
                            success = true;
                        }
                        event.setDropCompleted(success);
                        event.consume();
                    }

                });
                return row;
            }
        });
        //TreeItem<Category> ChildA = new TreeItem<>();
        //TreeItem<Category> ChildB = new TreeItem<>();
        //TreeItem<Category> ChildAA = new TreeItem<>();
        //List<TreeItem<Category>> test = new ArrayList<>();
        //root.setValue(category);
        //ChildA.setValue(category.getSubcategories().get(0));
        //ChildB.setValue(category.getSubcategories().get(1));
        //ChildAA.setValue(category.getSubcategories().get(0).getSubcategories().get(0));
         //root.add(new TreeItem<>(new Category("Show Categories")));
        //List<TreeItem<Category>> CategoryList = new ArrayList<>();
//            Category myCat = new Category(data.get(i).getCategory());
//            CategoryList.add(new TreeItem<Category>(myCat));
//            root.add(new TreeItem<Category>(myCat));
//            root.get(i).getChildren().add(new TreeItem<Category>(myCat.getSubcategories().get(i)));
//            test.add(Test(data.get(i).getCategory()));
//        }
        //ChildA.getChildren().setAll(ChildAA);
        //test.add(ChildA);
        //test.add(ChildB);
        //root.getChildren().setAll(test);
        TreeCategory.setCellValueFactory(new TreeItemPropertyValueFactory<Category, String>("name"));
        CategoryTreeTableView.setRoot(root);
        //List<TreeItem<String>> root = new ArrayList<>();
    }

    public TreeItem<Category> ReadCategory(Category cat, TreeItem<Category> returnTree, List<Category> comeBackToNode, List<TreeItem<Category>> findNode){
        ///TreeItem<Category> returnTree = new ArrayList<>();
        if(comeBackToNode.size() > 0){
            comeBackToNode.remove(0);
            findNode.remove(0);
        }
        if(cat.getSubcategories().size() > 0){
            List<TreeItem<Category>> test = new ArrayList<>();
            int i = 0;
            for(Category cat1 : cat.getSubcategories()){
                test.add(new TreeItem<>(cat1));
                if(cat1.getSubcategories().size() > 0){
                    comeBackToNode.add(cat1);
                    findNode.add(test.get(i));
                }
                i++;

            }
            returnTree.getChildren().setAll(test);
            if(comeBackToNode.size() > 0){
                TreeItem<Category> passedItem = findNode.get(0);
                ReadCategory(comeBackToNode.get(0), passedItem, comeBackToNode, findNode);
            }
            else{
                return returnTree;
            }
            return returnTree;
        }
        return returnTree;
    }
}

