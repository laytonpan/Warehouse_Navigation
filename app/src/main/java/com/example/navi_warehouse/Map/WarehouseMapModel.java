package com.example.navi_warehouse.Map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;


public class WarehouseMapModel {

    // Map to store nodes by their unique ID
    private final Map<String, Node> nodes = new HashMap<>();

    /**
     * Adds a new node to the map with a given ID and coordinates.
     *
     * @param id Unique identifier for the node.
     * @param x  X-coordinate of the node.
     * @param y  Y-coordinate of the node.
     */
    public void addNode(String id, double x, double y) {
        nodes.put(id, new Node(id, x, y));
    }

    /**
     * Adds an edge between two nodes with a given distance.
     *
     * @param id1      ID of the first node.
     * @param id2      ID of the second node.
     * @param distance Distance between the two nodes.
     */
    public void addEdge(String id1, String id2, int distance) {
        if (id1.equals(id2)) return; // Prevent self-loops

        Node node1 = nodes.get(id1);
        Node node2 = nodes.get(id2);

        if (node1 != null && node2 != null) {
            node1.addNeighbor(node2, distance);
            node2.addNeighbor(node1, distance); // Bidirectional edge
        }
    }

    /**
     * Retrieves a node by its ID.
     *
     * @param id Unique identifier of the node.
     * @return The corresponding Node object, or null if not found.
     */
    public Node getNode(String id) {
        return nodes.get(id);
    }

    /**
     * Returns all nodes in the model.
     *
     * @return A map of node IDs to Node objects.
     */
    public Map<String, Node> getNodes() {
        return nodes;
    }

    /**
     * Calculates the shortest path between two nodes identified by their IDs using Dijkstra's algorithm.
     *
     * @param startId ID of the starting node.
     * @param endId   ID of the target node.
     * @return A list of nodes representing the shortest path, or an empty list if no path exists.
     */
    public List<Node> calculateShortestPath(String startId, String endId) {
        Node start = nodes.get(startId);
        Node end = nodes.get(endId);

        if (start == null || end == null) return Collections.emptyList();

        PriorityQueue<NodeDistance> queue = new PriorityQueue<>();
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        Set<Node> visited = new HashSet<>();

        // Initialize distances
        for (Node node : nodes.values()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        distances.put(start, 0.0);
        queue.add(new NodeDistance(start, 0.0));

        while (!queue.isEmpty()) {
            NodeDistance current = queue.poll(); // Get the node with the shortest distance

            if (visited.contains(current.node)) continue; // Skip if already visited
            visited.add(current.node);

            if (current.node.equals(end)) break; // Exit if target node is reached

            // Explore neighbors of the current node
            for (Map.Entry<Node, Integer> neighborEntry : current.node.neighbors.entrySet()) {
                Node neighbor = neighborEntry.getKey();
                double edgeWeight = neighborEntry.getValue(); // Safely get the edge weight
                double newDist = distances.get(current.node) + edgeWeight;

                // Update the distance if a shorter path is found
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current.node);

                    queue.add(new NodeDistance(neighbor, newDist)); // Add neighbor to the queue
                }
            }
        }

        return reconstructPath(previous, end);
    }

    /**
     * Reconstructs the shortest path from the previous node map.
     *
     * @param previous Map of nodes to their previous node in the shortest path.
     * @param end      The target node.
     * @return A list of nodes representing the shortest path.
     */
    private List<Node> reconstructPath(Map<Node, Node> previous, Node end) {
        List<Node> path = new ArrayList<>();
        Node current = end;

        // In case duplicate node
        Set<Node> visitedNodes = new HashSet<>();

        while (current != null) {
            if (visitedNodes.contains(current)) {
                throw new IllegalStateException("Detected circular reference in path reconstruction");
            }
            visitedNodes.add(current);

            path.add(0, current); // Add the node to the beginning of the path
            current = previous.get(current); // Move to the previous node
        }

        return path;
    }

    /**
     * Represents a node in the warehouse map.
     */
    public static class Node {
        public final String id; // Unique identifier for the node
        public final double x, y; // Coordinates of the node
        public final Map<Node, Integer> neighbors = new HashMap<>(); // Neighboring nodes with distances

        public Node(String id, double x, double y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        /**
         * Adds a neighbor to this node with a given distance.
         *
         * @param neighbor The neighbor node.
         * @param distance The distance to the neighbor.
         */
        public void addNeighbor(Node neighbor, int distance) {
            if (neighbor != this && !neighbors.containsKey(neighbor)) {
                neighbors.put(neighbor, distance);
            }
        }

        /**
         * Gets the distance to a specified neighbor.
         *
         * @param neighbor The neighbor node.
         * @return The distance to the neighbor, or positive infinity if not connected.
         */
        public double getDistance(Node neighbor) {
            return neighbors.getOrDefault(neighbor, (int) Double.POSITIVE_INFINITY);
        }
    }

    /**
     * Helper class to represent a node and its distance in the priority queue.
     */
    public static class NodeDistance implements Comparable<NodeDistance> {
        public final Node node; // Reference to the node
        public final double distance; // Distance from the source node

        public NodeDistance(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return Double.compare(this.distance, other.distance);
        }
    }
}





