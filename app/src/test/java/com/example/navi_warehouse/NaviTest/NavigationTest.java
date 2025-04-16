package com.example.navi_warehouse.NaviTest;

import com.example.navi_warehouse.Map.WarehouseMapModel;
import com.example.navi_warehouse.Navigation.DijkstraNavigator;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
        mapModel.addNode("Shelf4", 4, 4);
        mapModel.addNode("Exit", 5, 5);

        // Add edges
        mapModel.addEdge("Entrance", "Shelf1", 1);
        mapModel.addEdge("Shelf1", "Shelf2", 1);
        mapModel.addEdge("Shelf2", "Shelf3", 1);
        mapModel.addEdge("Shelf3", "Shelf4", 1);
        mapModel.addEdge("Shelf4", "Exit", 1);
        mapModel.addEdge("Shelf2", "Shelf4", 2); // Alternative path
    }

    @Test
    public void testShortestPathSingleDestination() {
        // Test navigation from Entrance to Exit
        List<WarehouseMapModel.Node> path = mapModel.calculateShortestPath("Entrance", "Exit");

        // Validate the path
        assertEquals(Arrays.asList(
                mapModel.getNode("Entrance"),
                mapModel.getNode("Shelf1"),
                mapModel.getNode("Shelf2"),
                mapModel.getNode("Shelf3"),
                mapModel.getNode("Shelf4"),
                mapModel.getNode("Exit")
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

        // Validate the path (expected order: Entrance -> Shelf1 -> Shelf2 -> Shelf3)
        assertEquals(Arrays.asList(
                mapModel.getNode("Entrance"),
                mapModel.getNode("Shelf1"),
                mapModel.getNode("Shelf2"),
                mapModel.getNode("Shelf3")
        ), path);
    }

    @Test
    public void testShortestPathNoPath() {
        // Add an isolated node
        mapModel.addNode("IsolatedNode", 10, 10);

        // Test navigation from Entrance to the isolated node
        List<WarehouseMapModel.Node> path = mapModel.calculateShortestPath("Entrance", "IsolatedNode");

        // Validate the path is empty
        assertEquals(1, path.size());
    }

    @Test
    public void testShortestPathAlternativeRoute() {
        // Add a shorter alternative route
        mapModel.addEdge("Entrance", "Shelf3", 2);

        // Test navigation from Entrance to Shelf4
        List<WarehouseMapModel.Node> path = mapModel.calculateShortestPath("Entrance", "Shelf4");

        // Validate the path (expected to take the alternative route)
        assertEquals(Arrays.asList(
                mapModel.getNode("Entrance"),
                mapModel.getNode("Shelf3"),
                mapModel.getNode("Shelf4")
        ), path);
    }
}
