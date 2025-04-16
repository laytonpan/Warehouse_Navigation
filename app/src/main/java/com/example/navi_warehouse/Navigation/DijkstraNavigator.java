// DijkstraNavigator.java
package com.example.navi_warehouse.Navigation;

import com.example.navi_warehouse.Map.WarehouseMapModel;
import com.example.navi_warehouse.Map.WarehouseMapModel.Node;
import com.example.navi_warehouse.Map.WarehouseMapModel.NodeDistance;

import java.util.*;

public class DijkstraNavigator {

    /**
     * Calculates the shortest path that visits multiple destination nodes.
     *
     * @param start   The starting node.
     * @param targets The list of target nodes to visit.
     * @return The shortest path visiting all targets.
     */
    public static List<Node> calculateShortestPathMultiDestination(Node start, List<Node> targets) {
        List<Node> mutableTargets = new ArrayList<>(targets);
        List<Node> resultPath = new ArrayList<>();
        Node current = start;

        while (!mutableTargets.isEmpty()) {
            Node closestTarget = null;
            double shortestDistance = Double.POSITIVE_INFINITY;

            for (Node target : mutableTargets) {
                List<Node> pathToTarget = calculateShortestPath(current, target);
                double distance = 0;

                // Calculate total distance of the path
                for (int i = 0; i < pathToTarget.size() - 1; i++) {
                    Node currentNode = pathToTarget.get(i);
                    Node nextNode = pathToTarget.get(i + 1);
                    distance += currentNode.neighbors.get(nextNode);
                }

                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    closestTarget = target;
                }
            }

            // Add the path to the result (remove overlap with last node of resultPath)
            List<Node> pathToTarget = calculateShortestPath(current, closestTarget);
            if (!resultPath.isEmpty()) {
                pathToTarget.remove(0); // Remove the first node to avoid duplication
            }
            resultPath.addAll(pathToTarget);

            // Update current node and remove visited target
            current = closestTarget;
            mutableTargets.remove(closestTarget);
        }

        return resultPath;
    }

    /**
     * Calculates the shortest path between two nodes using Dijkstra's algorithm.
     *
     * @param start  The starting node.
     * @param target The target node.
     * @return A list of nodes representing the shortest path.
     */
    public static List<WarehouseMapModel.Node> calculateShortestPath(WarehouseMapModel.Node start, WarehouseMapModel.Node target) {
        if (start == null || target == null) return Collections.emptyList();

        PriorityQueue<WarehouseMapModel.NodeDistance> queue = new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.distance));
        Map<WarehouseMapModel.Node, Double> distances = new HashMap<>();
        Map<WarehouseMapModel.Node, WarehouseMapModel.Node> previous = new HashMap<>();
        Set<WarehouseMapModel.Node> visited = new HashSet<>();

        // Initialize distances
        distances.put(start, 0.0);
        queue.add(new WarehouseMapModel.NodeDistance(start, 0.0));

        while (!queue.isEmpty()) {
            WarehouseMapModel.NodeDistance current = queue.poll();
            if (visited.contains(current.node)) continue;
            visited.add(current.node);

            if (current.node.equals(target)) break;

            for (Map.Entry<WarehouseMapModel.Node, Integer> neighborEntry : current.node.neighbors.entrySet()) {
                WarehouseMapModel.Node neighbor = neighborEntry.getKey();
                double newDist = distances.get(current.node) + neighborEntry.getValue();

                if (newDist < distances.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current.node);
                    queue.add(new WarehouseMapModel.NodeDistance(neighbor, newDist));
                }
            }
        }

        // ✅ Fix: Check if target is unreachable
        if (!previous.containsKey(target) && !start.equals(target)) {
            return Collections.emptyList();
        }

        // ✅ Reconstruct the path from target to start
        List<WarehouseMapModel.Node> path = new ArrayList<>();
        WarehouseMapModel.Node current = target;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        return path;
    }

    /**
     * Calculates the total distance of a given path.
     *
     * @param path The list of nodes representing the path.
     * @return The total distance of the path.
     */
    private static double calculatePathDistance(List<Node> path) {
        double totalDistance = 0.0;

        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next = path.get(i + 1);
            totalDistance += current.neighbors.get(next);
        }

        return totalDistance;
    }
}
