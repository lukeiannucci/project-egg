package xyz.jmatt.services;

import xyz.jmatt.daos.CategoryDao;
import xyz.jmatt.daos.PersonalDatabaseTransaction;
import xyz.jmatt.models.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CategoryService {
    public CategoryService() {}

    /**
     * Gets all categories from the database
     * @return a tree of all category objects
     */
    public Category getAllCategories() {
        PersonalDatabaseTransaction transaction = null;
        Category result = null;

        try {
            transaction = new PersonalDatabaseTransaction();
            CategoryDao categoryDao = new CategoryDao(transaction);

            List<Category> unsortedCategories = categoryDao.getAllCategories();

            transaction.commit();
            transaction.close();
            transaction = null;

            result = getOrganizedCategories(unsortedCategories);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(transaction != null) {
                transaction.rollback();
                transaction.close();
                transaction = null;
            }
        }
        return result;
    }

    /**
     * Returns a structured tree of categories
     * @param categories the unsorted list of all categories
     * @return a structured tree of categories
     */
    private Category getOrganizedCategories(List<Category> categories) {
        for(Category category : categories) { //go through each individual category
            List<Category> children = getChildrenForParentId(category.getId(), categories); //get all categories with current category as a parent
            //add each child as a child of the current node
            for(Category child : children) {
                category.addSubcategory(child);
            }
        }

        //find the root category
        for(Category category : categories) {
            if(category.getParentId() == null) {
                return category;
            }
        }
        return null;
    }

    /**
     * Gets all the categories that have the given id as a parent
     * @param parentId the parent id to search for children for
     * @param categories the full list of categories
     * @return the list of children for the given id
     */
    private List<Category> getChildrenForParentId(String parentId, List<Category> categories) {
        //new empty list to put children in
        List<Category> result = new ArrayList<>();
        //go through all categories
        for(Category category : categories) {
            if(category.getParentId() != null && category.getParentId().equals(parentId)) { //current category has parentId marked as the given id
                result.add(category); //add the current category to the list of children
            }
        }
        return result;
    }

    /**
     * Adds the given category into the database
     * @param model the category to add
     * @return whether the category was added successfully or not
     */
    public boolean addCategory(Category model) {
        PersonalDatabaseTransaction transaction = null;

        try {
            transaction = new PersonalDatabaseTransaction();
            CategoryDao categoryDao = new CategoryDao(transaction);

            categoryDao.pushCategory(model);

            transaction.commit();
            transaction.close();
            transaction = null;

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(transaction != null) {
                transaction.rollback();
                transaction.close();
            }
        }
        return false;
    }
}
