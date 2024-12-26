package com.example.navi_warehouse.Map;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;


import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class WarehouseMapModel {

    public static class Node {
        public String id;
        public int x, y;
        public Map<Node, Integer> neighbors;

        public Node(String id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.neighbors = new HashMap<>();
        }

        public void addNeighbor(Node neighbor, int distance) {
            neighbors.put(neighbor, distance);
        }
    }

    private Map<String, Node> nodes;

    public WarehouseMapModel() {
        nodes = new HashMap<>();
    }

    public void addNode(String id, int x, int y) {
        nodes.put(id, new Node(id, x, y));
    }

    public void addEdge(String fromNodeId, String toNodeId, int distance) {
        Node fromNode = nodes.get(fromNodeId);
        Node toNode = nodes.get(toNodeId);
        if (fromNode != null && toNode != null) {
            fromNode.addNeighbor(toNode, distance);
            toNode.addNeighbor(fromNode, distance);
        }
    }

    public Queue<Node> findOptimalPath(String startNodeId, String goalNodeId) {
        Node start = nodes.get(startNodeId);
        Node goal = nodes.get(goalNodeId);
        if (start == null || goal == null) return null;

        Queue<Node> openSet = new PriorityQueue<>((a, b) -> estimateDistance(a, goal) - estimateDistance(b, goal));
        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Integer> gScore = new HashMap<>();

        openSet.add(start);
        gScore.put(start, 0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current == goal) return reconstructPath(cameFrom, current);

            for (Map.Entry<Node, Integer> neighborEntry : current.neighbors.entrySet()) {
                Node neighbor = neighborEntry.getKey();
                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + neighborEntry.getValue();

                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    openSet.add(neighbor);
                }
            }
        }

        return null;
    }

    private int estimateDistance(Node node, Node goal) {
        return Math.abs(node.x - goal.x) + Math.abs(node.y - goal.y);
    }

    private Queue<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
        Queue<Node> path = new PriorityQueue<>();
        path.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }
        return path;
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }
}
