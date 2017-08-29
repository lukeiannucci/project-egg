package xyz.jmatt.models;

import java.util.List;

public class Category {
    private String name;
    private List<Category> subcategories;

    public Category(String name) {
        this.name = name;
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
