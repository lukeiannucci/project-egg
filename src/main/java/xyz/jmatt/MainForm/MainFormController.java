package xyz.jmatt.MainForm;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import xyz.jmatt.models.SortableDate;
import xyz.jmatt.models.TransactionModel;
import xyz.jmatt.services.CategoryService;
import xyz.jmatt.services.CreateAccountService;
import xyz.jmatt.services.TransactionService;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
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
        LocalDate date = DateIn.getValue();
        long dateMillis = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        TransactionModel trans = new TransactionModel(TransIn.getText(),CategoryIn.getText(),new BigDecimal(AmountIn.getText()), dateMillis);
        if(!new TransactionService().addTransaction(trans)) {
            //TODO show error or something
        } else {
            data.add(trans); //only add it to the chart if it saved properly so the user doesn't get confused
        }
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
        Category addedCategory = new Category(CategoryInput.getText(), root.getValue().getId());
        //addedCategory.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        root.getChildren().add(new TreeItem<Category>(addedCategory));
        CategoryService.addCategory(addedCategory);
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
//        Date.setCellValueFactory(
//                new Callback<TableColumn.CellDataFeatures<TransactionModel, String>, ObservableValue>() {
//                    @Override
//                    public ObservableValue call(TableColumn.CellDataFeatures<TransactionModel, String> param) {
//                        return new ReadOnlyStringWrapper(param.getValue().getFormattedDate());
//                    }
//                }
//        );
        Date.setCellValueFactory(new PropertyValueFactory<TransactionModel, SortableDate>("FormattedDate"));
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
        TreeItem<Category> returnTree = new TreeItem<Category>(category);
        List<TreeItem<Category>> findNode = new ArrayList<>();
        root = ReadCategory(category, returnTree, findNode);
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
                        }
                    }
                });
                row.setOnDragOver(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
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
                        if(db.hasContent(SERIALIZED_MIME_TYPE)){
                            int index = (Integer)db.getContent(SERIALIZED_MIME_TYPE);
                            TreeItem<Category> droppedon = row.getTreeItem();
                            if(droppedon != null){
                                TreeItem<Category> moveItem = CategoryTreeTableView.getTreeItem(index);
                                moveItem.getParent().getChildren().remove(moveItem);
                                droppedon.getChildren().add(moveItem);
                                droppedon.getChildren().sort(Comparator.comparing(t->t.getValue().getName()));
                                moveItem.getValue().setParentId(droppedon.getValue().getId());
                                CategoryService.moveCategory(moveItem.getValue());

                            }
                            success = true;
                        }
                        event.setDropCompleted(success);
                        event.consume();
                    }

                });
                return row;
            }
        });
        TreeCategory.setCellValueFactory(new TreeItemPropertyValueFactory<Category, String>("name"));
        CategoryTreeTableView.setRoot(root);
    }

    public TreeItem<Category> ReadCategory(Category category, TreeItem<Category> returnTree, List<TreeItem<Category>> findNode){
        if(findNode.size() > 0){
            findNode.remove(0);
        }
        if(category.getSubcategories().size() > 0){
            List<TreeItem<Category>> childrenOfCategory = new ArrayList<>();
            int i = 0;
            for(Category cat1 : category.getSubcategories()){
                childrenOfCategory.add(new TreeItem<>(cat1));
                if(cat1.getSubcategories().size() > 0){
                    findNode.add(childrenOfCategory.get(i));
                }
                i++;
            }
            returnTree.getChildren().setAll(childrenOfCategory);
            if(findNode.size() > 0){
                ReadCategory(findNode.get(0).getValue(), findNode.get(0), findNode);
            }
        }
        return returnTree;
    }
}

