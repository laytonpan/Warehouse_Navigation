# Warehouse Navigation

> An Android warehouse navigation app for item management, order handling, and route planning.

## Overview

Navi Warehouse is an Android-based warehouse navigation project designed to support warehouse picking tasks.  
It combines item management, order storage, warehouse map logic, and shortest-path route generation in one practical mobile application.

The app uses **Room Database** to manage warehouse data and applies **graph-based pathfinding** to generate navigation routes across shelves.

## Key Features

- Display warehouse items in the app
- Create and store orders locally
- Generate shelf-based picking routes
- Support graph-based warehouse navigation
- Apply shortest-path planning with Dijkstra algorithm

## Tech Stack

| Category | Details |
|----------|---------|
| Platform | Android |
| Languages | Kotlin, Java |
| Database | Room Database |
| UI | Fragments, RecyclerView |
| Navigation Logic | Graph model + Dijkstra algorithm |

## Core Components

- **MainActivity**: sets up the main app navigation and initializes the warehouse map model.
- **DashboardFragment / DashboardViewModel**: load and display warehouse items from Room Database.
- **ItemAdapter**: binds item data to the RecyclerView list.
- **WarehouseDatabase / ItemDao**: manage local data storage and item queries.
- **MapFragment**: displays the warehouse map interface.
- **WarehouseMapModel**: represents the warehouse as a graph of nodes and edges.
- **DijkstraNavigator**: calculates shortest paths for picking routes.
- **NavigationHelper**: converts shelf targets into navigable route data.
- **WarehouseMapParser**: parses SVG-based warehouse map elements into structured map data.

## Use Case

This project is designed around a simple warehouse picking scenario:

1. Store item and shelf data
2. Create or manage an order
3. Match target shelves in the warehouse
4. Generate a navigation route for picking

## Project Goal

The goal of this project is to demonstrate:

- Practical Android development skills
- Database integration with Room
- Algorithmic thinking in navigation and pathfinding
- Application of software development to logistics scenarios

## Future Improvements

- Better route optimization for multi-stop picking
- Improved warehouse map rendering
- Picking progress tracking
- Damaged or missed item reporting
- Route animation and visual guidance

## Author

**Zhuoxing Pan**
