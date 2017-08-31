package xyz.jmatt.models;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private List<Category> subcategories;
    public List<String> Categories;

    public Category(String name) {
        this.name = name;
        subcategories = new ArrayList<>();
        Categories = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public void addSubcategory(Category category) {
        subcategories.add(category);
    }
}
