package com.example.navi_warehouse.Item;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "code_name")
    private String codeName;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "size")
    private String size;

    @ColumnInfo(name = "weight")
    private double weight;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "location_x")
    public int locationX;

    @ColumnInfo(name = "location_y")
    public int locationY;

    // Constructors, Getters and Setters
    public Item(int id, String name, String codeName, double price, String size, double weight, String category, int locationX, int locationY) {
        this.id = id;
        this.name = name;
        this.codeName = codeName;
        this.price = price;
        this.size = size;
        this.weight = weight;
        this.category = category;
        this.locationX = locationX;
        this.locationY = locationY;
    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCodeName() { return codeName; }
    public void setCodeName(String codeName) { this.codeName = codeName; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getLocationX() { return locationX; }
    public void setLocationX() {this.locationX = locationX; }
    public int getLocationY() { return locationY; }
    public void setLocationY() {this.locationY = locationY; }

}
