package com.example.final_project;

public class GroceryItem {
    private String id;
    private String itemName;
    private boolean purchased;
    private String category;

    public GroceryItem() {
    }

    public GroceryItem(String id, String itemName, boolean purchased, String category) {
        this.id = id;
        this.itemName = itemName;
        this.purchased = purchased;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
