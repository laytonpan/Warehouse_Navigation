package com.example.navi_warehouse.Navigation;

import com.example.navi_warehouse.Map.WarehouseMapModel;
import com.example.navi_warehouse.Order.Order;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NavigationHelper {

    /**
     * Calculates the shortest path for fulfilling an order, starting from a specific node.
     *
     * @param order       The order containing items with shelf IDs.
     * @param mapModel    The warehouse map model.
     * @param startNodeId The ID of the starting node (e.g., "Entrance").
     * @return A list of nodes representing the shortest path to fulfill the order.
     */
    public static List<WarehouseMapModel.Node> calculateShortestPathForOrder(Order order, WarehouseMapModel mapModel, String startNodeId) {
        // Extract unique shelf IDs from the order
        List<Integer> shelfIds = order.getShelfIds();

        // Retrieve the starting node (e.g., Entrance)
        WarehouseMapModel.Node startNode = mapModel.getNode(startNodeId);
        if (startNode == null) {
            throw new IllegalArgumentException("Start node not found in the map: " + startNodeId);
        }

        // Map shelf IDs to corresponding nodes, filtering out any invalid mappings
        List<WarehouseMapModel.Node> shelfNodes = shelfIds.stream()
                .map(shelfId -> mapModel.getNode("Shelf" + shelfId)) // Map shelf ID to node
                .filter(Objects::nonNull) // Filter out null nodes
                .collect(Collectors.toList());

        // Ensure there are valid shelf nodes to navigate to
        if (shelfNodes.isEmpty()) {
            throw new IllegalArgumentException("No valid shelf nodes found for the provided order.");
        }

        // Calculate the shortest path that visits all shelves
        return DijkstraNavigator.calculateShortestPathMultiDestination(startNode, shelfNodes);
    }
}