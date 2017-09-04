package xyz.jmatt.models;

import java.util.*;

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

    public void sortSubcategories() {
        Collections.sort(subcategories, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
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
