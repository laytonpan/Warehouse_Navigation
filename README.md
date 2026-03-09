# Warehouse Navigation

> An Android warehouse navigation app for item management, order handling, and route planning.

## Overview

Navi Warehouse is an Android-based warehouse navigation project that combines:

- item management
- order storage
- warehouse map logic
- shortest-path route generation for picking tasks

The app uses Room Database to manage warehouse data and graph-based pathfinding to generate navigation routes across shelves.

## Key Features

- Warehouse item display
- Order creation and storage
- Shelf-based route generation
- Graph-based navigation logic
- Shortest-path planning with Dijkstra algorithm

## Tech Stack

| Category | Details |
|----------|---------|
| Platform | Android |
| Languages | Kotlin, Java |
| Database | Room Database |
| UI | Fragments, RecyclerView |
| Navigation Logic | Graph model + Dijkstra algorithm |

## Use Case

This project is designed around a simple warehouse picking scenario:

1. Store item and shelf data
2. Create or manage an order
3. Match target shelves in the warehouse
4. Generate a navigation route for picking

## Project Goal

The goal of this project is to demonstrate:

- practical Android development skills
- database integration with Room
- algorithmic thinking in navigation and pathfinding
- application of software development to logistics scenarios

## Future Improvements

- Better route optimization for multi-stop picking
- Improved warehouse map rendering
- Picking progress tracking
- Damaged or missed item reporting
- Route animation and visual guidance

## Author

**Zhuoxing Pan**
