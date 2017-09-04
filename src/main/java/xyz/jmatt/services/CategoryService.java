package xyz.jmatt.services;

import xyz.jmatt.daos.CategoryDao;
import xyz.jmatt.daos.PersonalDatabaseTransaction;
import xyz.jmatt.models.Category;

import java.sql.SQLException;
import java.util.*;

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
            sortAllSubcategories(result);

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
     * Updates a ategory in the database
     * @param model the model of category with new details
     * @return whether the update was successful
     */
    public boolean renameCategory(Category model) {
        PersonalDatabaseTransaction transaction = null;

        try {
            transaction = new PersonalDatabaseTransaction();
            CategoryDao categoryDao = new CategoryDao(transaction);

            categoryDao.renameCategory(model.getId(), model.getName());

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

    public static boolean moveCategory(Category model) {
        PersonalDatabaseTransaction transaction = null;

        try {
            transaction = new PersonalDatabaseTransaction();
            CategoryDao categoryDao = new CategoryDao(transaction);

            categoryDao.moveCategory(model.getId(), model.getParentId());

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

    /**
     * Adds the given category into the database
     * @param model the category to add
     * @return whether the category was added successfully or not
     */
    public static boolean addCategory(Category model) {
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

    /**
     * Sorts all the lists of subcategories in the tree into alphabetical order
     * @param category the category whose children to sort
     */
    private void sortAllSubcategories(Category category) {
        Collections.sort(category.getSubcategories(), new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        List<Category> subcategories = category.getSubcategories();
        for(Category child : subcategories) {
            if(child.getSubcategories().size() != 0) {
                sortAllSubcategories(child);
            }
        }
    }

    /**
     * Gets a structured tree of categories
     * @param categories the list of all categories
     * @return the root node of the category tree
     */
    private Category getOrganizedCategories(List<Category> categories) {
        //binary search will be used to find given categories, so first we sort by id
        categories.sort(new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        Category root = null; //eventually will want to return the root

        for(Category category : categories) { //go through each category
            if(category.getParentId() == null) { //root category has no parent, must be the root
                root = category; //save
            } else { //if the current category has a parent
                int index = getIndexOfParent(category, categories); //find that parent
                categories.get(index).addSubcategory(category); //add the current node as a child of its' parent
            }
        }

        return root;
    }

    /**
     * Gets the index of the parent category for the given category in the list of categories
     * @param category the category whose parent to find the index of
     * @param categories the list of all categories
     * @return the index of the parent category in the given list
     */
    private int getIndexOfParent(Category category, List<Category> categories) {
        //binary search in O(log(n)) time
        return Collections.binarySearch(categories, category, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getId().compareTo(o2.getParentId());
            }
        });
    }
}
