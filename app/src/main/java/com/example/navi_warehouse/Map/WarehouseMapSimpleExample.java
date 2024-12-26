package com.example.navi_warehouse.Map;

public class WarehouseMapSimpleExample {
    public static WarehouseMapModel createSimpleMap(int scaler) {
        WarehouseMapModel mapModel = new WarehouseMapModel();

        // Add nodes (Shelves and key points) with larger spacing
        mapModel.addNode("Entrance", 0 * scaler, 0 * scaler);   // Entrance
        mapModel.addNode("Shelf1", 3 * scaler, 1 * scaler);    // Shelf 1
        mapModel.addNode("Shelf2", 6 * scaler, 1 * scaler);    // Shelf 2
        mapModel.addNode("Shelf3", 9 * scaler, 1 * scaler);    // Shelf 3
        mapModel.addNode("Shelf4", 3 * scaler, 5 * scaler);    // Shelf 4
        mapModel.addNode("Shelf5", 6 * scaler, 5 * scaler);    // Shelf 5
        mapModel.addNode("Shelf6", 9 * scaler, 5 * scaler);    // Shelf 6
        mapModel.addNode("Shelf7", 3 * scaler, 9 * scaler);    // Shelf 7
        mapModel.addNode("Shelf8", 6 * scaler, 9 * scaler);    // Shelf 8
        mapModel.addNode("Shelf9", 9 * scaler, 9 * scaler);    // Shelf 9
        mapModel.addNode("Exit", 12 * scaler, 10 * scaler);    // Exit

        // Add paths connecting the shelves and other nodes
        mapModel.addEdge("Entrance", "Shelf1", 3 * scaler);    // Entrance to Shelf 1
        mapModel.addEdge("Shelf1", "Shelf2", 3 * scaler);      // Shelf 1 to Shelf 2
        mapModel.addEdge("Shelf2", "Shelf3", 3 * scaler);      // Shelf 2 to Shelf 3
        mapModel.addEdge("Shelf1", "Shelf4", 4 * scaler);      // Shelf 1 to Shelf 4
        mapModel.addEdge("Shelf2", "Shelf5", 4 * scaler);      // Shelf 2 to Shelf 5
        mapModel.addEdge("Shelf3", "Shelf6", 4 * scaler);      // Shelf 3 to Shelf 6
        mapModel.addEdge("Shelf4", "Shelf5", 3 * scaler);      // Shelf 4 to Shelf 5
        mapModel.addEdge("Shelf5", "Shelf6", 3 * scaler);      // Shelf 5 to Shelf 6
        mapModel.addEdge("Shelf4", "Shelf7", 4 * scaler);      // Shelf 4 to Shelf 7
        mapModel.addEdge("Shelf5", "Shelf8", 4 * scaler);      // Shelf 5 to Shelf 8
        mapModel.addEdge("Shelf6", "Shelf9", 4 * scaler);      // Shelf 6 to Shelf 9
        mapModel.addEdge("Shelf7", "Shelf8", 3 * scaler);      // Shelf 7 to Shelf 8
        mapModel.addEdge("Shelf8", "Shelf9", 3 * scaler);      // Shelf 8 to Shelf 9
        mapModel.addEdge("Shelf9", "Exit", 3 * scaler);        // Shelf 9 to Exit

        return mapModel;
    }

    public static float getDefaultScaler(float canvasWidth, float canvasHeight) {
        // Define base dimensions of the map (before scaling)
        float baseWidth = 10; // Maximum x-coordinate in the original map
        float baseHeight = 6; // Maximum y-coordinate in the original map

        // Calculate the scaling factor based on the canvas dimensions
        float scaleX = canvasWidth / (baseWidth + 2); // Add padding for better visibility
        float scaleY = canvasHeight / (baseHeight + 2);

        return Math.min(scaleX, scaleY); // Use the smaller scaling factor to fit the map on screen
    }
}
