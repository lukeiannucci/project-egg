package xyz.jmatt.daos;

import xyz.jmatt.models.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private Connection connection;

    public CategoryDao(PersonalDatabaseTransaction transaction) {
        this.connection = transaction.getConnection();
    }

    /**
     * Creates the table and all the columns for the first time
     */
    public void initializeTable() throws SQLException {
        PreparedStatement prep = connection.prepareStatement(
                "CREATE TABLE Categories (CategoryId VARCHAR(255) PRIMARY KEY, ParentId VARCHAR(255), Name VARCHAR(255), Depth INT, Lineage VARCHAR(255));");
        prep.execute();
        prep.close();
    }

    /**
     * Gets all category objects from the database
     * @return an unsorted list of all category objects
     */
    public List<Category> getAllCategories() throws SQLException {
        PreparedStatement prep = connection.prepareStatement(
                "SELECT * FROM Categories");
        ResultSet resultSet = prep.executeQuery();

        List<Category> result = new ArrayList<>();
        while(resultSet.next()) {
            Category category = new Category();
            category.setId(resultSet.getString("CategoryId"));
            category.setParentId(resultSet.getString("ParentId"));
            category.setName(resultSet.getString("Name"));
            result.add(category);
        }
        resultSet.close();
        prep.close();
        return result;
    }

    public void pushCategory(Category model) throws SQLException {
        PreparedStatement prep = connection.prepareStatement(
                "INSERT INTO Categories (CategoryId,ParentId,Name,Depth,Lineage) VALUES (?,?,?,?,?)");
        prep.setString(1, model.getId());
        prep.setString(2, model.getParentId());
        prep.setString(3, model.getName());
        prep.setInt(4, 1);
        prep.setString(5, "");

        prep.executeUpdate();
        prep.close();
    }
}
