package com.example.navi_warehouse.NaviTest;

import com.example.navi_warehouse.Map.WarehouseMapModel;
import com.example.navi_warehouse.Navigation.DijkstraNavigator;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class NavigationTest {

    private WarehouseMapModel mapModel;

    @Before
    public void setUp() {
        // Initialize the warehouse map
        mapModel = new WarehouseMapModel();

        // Add nodes
        mapModel.addNode("Entrance", 0, 0);
        mapModel.addNode("Shelf1", 1, 1);
        mapModel.addNode("Shelf2", 2, 2);
        mapModel.addNode("Shelf3", 3, 3);
        mapModel.addNode("Exit", 4, 4);

        // Add edges
        mapModel.addEdge("Entrance", "Shelf1", 1);
        mapModel.addEdge("Shelf1", "Shelf2", 1);
        mapModel.addEdge("Shelf2", "Shelf3", 1);
        mapModel.addEdge("Shelf3", "Exit", 1);

        // Print graph structure
        System.out.println("Nodes in the graph:");
        for (String nodeId : mapModel.getNodes().keySet()) {
            System.out.println("Node ID: " + nodeId);
        }

        System.out.println("Edges in the graph:");
        for (Map.Entry<String, WarehouseMapModel.Node> entry : mapModel.getNodes().entrySet()) {
            System.out.println("Node: " + entry.getKey());
            for (Map.Entry<WarehouseMapModel.Node, Integer> neighbor : entry.getValue().neighbors.entrySet()) {
                System.out.println("  Neighbor: " + neighbor.getKey().id + ", Distance: " + neighbor.getValue());
            }
        }
    }



    @Test
    public void testShortestPathSingleDestination() {
        // Test navigation from Entrance to Shelf3
        List<WarehouseMapModel.Node> path = DijkstraNavigator.calculateShortestPath(
                mapModel.getNode("Entrance"),
                mapModel.getNode("Shelf3")
        );

        // Validate the path
        assertEquals(Arrays.asList(
                mapModel.getNode("Entrance"),
                mapModel.getNode("Shelf1"),
                mapModel.getNode("Shelf2"),
                mapModel.getNode("Shelf3")
        ), path);
    }

    @Test
    public void testShortestPathMultiDestination() {
        // Test navigation from Entrance to multiple shelves
        List<WarehouseMapModel.Node> path = DijkstraNavigator.calculateShortestPathMultiDestination(
                mapModel.getNode("Entrance"),
                Arrays.asList(
                        mapModel.getNode("Shelf1"),
                        mapModel.getNode("Shelf3")
                )
        );

        // Validate the path (expected order: Entrance -> Shelf1 -> Shelf3 -> Exit)
        assertEquals(Arrays.asList(
                mapModel.getNode("Entrance"),
                mapModel.getNode("Shelf1"),
                mapModel.getNode("Shelf2"),
                mapModel.getNode("Shelf3")
        ), path);
    }

    @Test
    public void testNoPath() {
        // Test navigation to a non-existent node
        List<WarehouseMapModel.Node> path = DijkstraNavigator.calculateShortestPath(
                mapModel.getNode("Entrance"),
                mapModel.getNode("Unknown")
        );

        // Validate the path is empty
        assertEquals(0, path.size());
    }
}
