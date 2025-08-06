package com.example.navi_warehouse.Item;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Objects;

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
    private int rowPosition;

    @ColumnInfo(name = "column_position")
    private int columnPosition;

    // Constructor
    public Item(int id, String name, String codeName, double price, String size, double weight,
                String category, int shelfId, int rowPosition, int columnPosition) {
        this.id = id;
        this.name = name;
        this.codeName = codeName;
        this.price = price;
        this.size = size;
        this.weight = weight;
        this.category = category;
        this.shelfId = shelfId;
        this.rowPosition = rowPosition;
        this.columnPosition = columnPosition;
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

    public int getRowPosition() { return rowPosition; }
    public void setRowPosition(int rowPosition) { this.rowPosition = rowPosition; }

    public int getColumnPosition() { return columnPosition; }
    public void setColumnPosition(int columnPosition) { this.columnPosition = columnPosition; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
