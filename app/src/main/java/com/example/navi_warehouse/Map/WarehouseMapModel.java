package com.example.navi_warehouse.Map;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class WarehouseMapModel {

    // Define a Node class to represent positions in the warehouse map
    public static class Node {
        public String id; // Unique identifier for the node
        public int x, y; // Coordinates of the node in the 2D grid
        public Map<Node, Integer> neighbors; // Neighbor nodes and their respective distances


        // Node constructor to initialize the node ID, coordinates, and neighbors
        public Node(String id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.neighbors = new HashMap<>();
        }

        // Add a neighbor node and the distance to that neighbor
        public void addNeighbor(Node neighbor, int distance) {
            neighbors.put(neighbor, distance);
        }
    }

    // Store all nodes in a map, using the node ID as the key
    private Map<String, Node> nodes;

    // WarehouseMapModel constructor to initialize the nodes map
    public WarehouseMapModel() {
        nodes = new HashMap<>();
    }

    // Add a node to the warehouse map
    public void addNode(String id, int x, int y) {
        nodes.put(id, new Node(id, x, y));
    }

    // Add an edge (path) between two nodes in the warehouse map, defining the distance between them
    public void addEdge(String fromNodeId, String toNodeId, int distance) {
        Node fromNode = nodes.get(fromNodeId);
        Node toNode = nodes.get(toNodeId);
        if (fromNode != null && toNode != null) {
            fromNode.addNeighbor(toNode, distance); // Add neighbor to the starting node
            toNode.addNeighbor(fromNode, distance); // Add starting node as a neighbor to the target node
        }
    }



    // Get a node by its ID
    public Node getNode(String nodeId) {
        return nodes.get(nodeId);
    }

    // Get all nodes
    public Map<String, Node> getNodes() {
        return nodes;
    }

    // Implement A* pathfinding algorithm to find the optimal path from the start node to the goal node
    public Queue<Node> findOptimalPath(String startNodeId, String goalNodeId) {
        Node start = nodes.get(startNodeId);
        Node goal = nodes.get(goalNodeId);
        if (start == null || goal == null) {
            return null; // Return null if either the start or goal node does not exist
        }

        // Use a priority queue to store nodes to be processed, sorted by estimated distance
        Queue<Node> openSet = new PriorityQueue<>((a, b) -> Integer.compare(estimateDistance(a, goal), estimateDistance(b, goal)));
        Map<Node, Integer> gScore = new HashMap<>(); // Store the current shortest path estimate from the start node to each node
        Map<Node, Node> cameFrom = new HashMap<>(); // Track the predecessor of each node

        openSet.add(start); // Add the start node to the queue
        gScore.put(start, 0); // Set the gScore of the start node to 0

        while (!openSet.isEmpty()) {
            Node current = openSet.poll(); // Retrieve the node with the smallest estimated distance

            if (current == goal) {
                return reconstructPath(cameFrom, current); // If the goal is reached, reconstruct and return the path
            }

            // Iterate through all neighbors of the current node
            for (Map.Entry<Node, Integer> neighborEntry : current.neighbors.entrySet()) {
                Node neighbor = neighborEntry.getKey();
                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + neighborEntry.getValue();

                // If a shorter path to the neighbor is found, update the gScore and add to the queue
                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, current); // Record the predecessor of the neighbor
                    gScore.put(neighbor, tentativeGScore); // Update the gScore
                    openSet.add(neighbor); // Add the neighbor to the queue for further processing
                }
            }
        }

        return null; // Return null if no path is found
    }

    // Use Manhattan distance as the heuristic to estimate the distance from a node to the goal node
    private int estimateDistance(Node node, Node goal) {
        return Math.abs(node.x - goal.x) + Math.abs(node.y - goal.y);
    }

    // Reconstruct the path from the start to the goal node
    private Queue<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
        Queue<Node> totalPath = new PriorityQueue<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current); // Backtrack to the predecessor node
            totalPath.add(current);
        }
        return totalPath;
    }

}
