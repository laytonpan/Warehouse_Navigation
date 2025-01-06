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

    @ColumnInfo(name = "shelf_id")
    private int shelfId; // Foreign key to Shelf table

    @ColumnInfo(name = "row_position")
    private int row_position; // Row position in the shelf

    @ColumnInfo(name = "column_position")
    private int column_position; // Column position in the shelf

    // Updated Constructor
    public Item(int id, String name, String codeName, double price, String size, double weight,
                String category, int shelfId, int row_position, int column_position) {
        this.id = id;
        this.name = name;
        this.codeName = codeName;
        this.price = price;
        this.size = size;
        this.weight = weight;
        this.category = category;
        this.shelfId = shelfId;
        this.row_position = row_position;
        this.column_position = column_position;
    }

    // Getters and Setters
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

    public int getShelfId() { return shelfId; }
    public void setShelfId(int shelfId) { this.shelfId = shelfId; }

    public int getRow_position() { return row_position; }
    public void setRow_position(int row_position) { this.row_position = row_position; }

    public int getColumn_position() { return column_position; }
    public void setColumn_position(int column_position) { this.column_position = column_position; }

}

