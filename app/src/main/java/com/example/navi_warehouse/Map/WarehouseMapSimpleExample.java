package com.example.navi_warehouse.Map;

public class WarehouseMapSimpleExample {
    public static WarehouseMapModel createSimpleMap(int scaler) {
        WarehouseMapModel mapModel = new WarehouseMapModel();

        // Add nodes (e.g., shelves and aisles) with scaled positions
        mapModel.addNode("Entrance", 0 * scaler, 0 * scaler); // Warehouse entrance
        mapModel.addNode("Shelf1", 2 * scaler, 1 * scaler); // Shelf 1
        mapModel.addNode("Shelf2", 5 * scaler, 1 * scaler); // Shelf 2
        mapModel.addNode("Aisle1", 3 * scaler, 3 * scaler); // Aisle 1
        mapModel.addNode("Shelf3", 6 * scaler, 5 * scaler); // Shelf 3

        // Add edges (paths) representing connections between nodes and their distances
        mapModel.addEdge("Entrance", "Shelf1", 2 * scaler); // Distance from entrance to Shelf 1 is scaled
        mapModel.addEdge("Shelf1", "Shelf2", 3 * scaler); // Distance from Shelf 1 to Shelf 2 is scaled
        mapModel.addEdge("Shelf2", "Aisle1", 3 * scaler); // Distance from Shelf 2 to Aisle 1 is scaled
        mapModel.addEdge("Aisle1", "Shelf3", 4 * scaler); // Distance from Aisle 1 to Shelf 3 is scaled
        mapModel.addEdge("Shelf1", "Aisle1", 2 * scaler); // Distance from Shelf 1 to Aisle 1 is scaled


        return mapModel; // Return the constructed warehouse map model
    }

    public static float getDefaultScaler(float canvasWidth, float canvasHeight) {
        // Define base dimensions of the map (before scaling)
        float baseWidth = 7; // Maximum x-coordinate in the original map
        float baseHeight = 5; // Maximum y-coordinate in the original map

        // Calculate the scaling factor based on the canvas dimensions
        float scaleX = canvasWidth / (baseWidth + 2); // Add padding for better visibility
        float scaleY = canvasHeight / (baseHeight + 2);

        return Math.min(scaleX, scaleY); // Use the smaller scaling factor to fit the map on screen
    }
}

