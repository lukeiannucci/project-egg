package xyz.jmatt.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Category {
    private String name;
    private String id;
    private String parentId;
    private List<Category> subcategories = new ArrayList<>();

    public Category() {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
    }
    public Category(String passedName, String passedParent)
    {
        this.name = passedName;
        this.parentId = passedParent;
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
