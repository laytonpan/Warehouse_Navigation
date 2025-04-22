package com.example.navi_warehouse.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;


import com.example.navi_warehouse.Map.WarehouseMapModel;
import com.example.navi_warehouse.Map.WarehouseMapModel.Node;

import java.util.Map;
import java.util.Set;

public class CustomMapView extends View {

    private static final float SHELF_WIDTH = 20f;
    private static final float SHELF_HEIGHT = 40f;
    private static final float ENTRANCE_EXIT_SIZE = 30f;
    private static final float LABEL_OFFSET = 50f;
    private static final float TEXT_SIZE = 50f;

    private WarehouseMapModel warehouseMapModel;
    private Paint paint;
    private boolean showNavigationPath = false;
    private Queue<Node> navigationPath;
    private List<Node> highlightedPath; // ✅ highlighted path support

    public CustomMapView(Context context) {
        super(context);
        init();
    }

    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
    }

    public void setWarehouseMapModel(WarehouseMapModel model) {
        this.warehouseMapModel = model;
        invalidate();
    }

    public void setNavigationPath(Queue<Node> path) {
        this.navigationPath = path;
        this.showNavigationPath = true;
        invalidate();
    }

    public void clearNavigationPath() {
        this.showNavigationPath = false;
        this.navigationPath = null;
        invalidate();
    }

    public void setHighlightedPath(List<Node> path) {
        this.highlightedPath = path;
        invalidate();
    }

    private void drawRectWithLabel(Canvas canvas, float x, float y, float size, int color, String label, float scale) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(
                x - size * scale, y - size * scale,
                x + size * scale, y + size * scale,
                paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(TEXT_SIZE);
        canvas.drawText(label, x - (paint.measureText(label) / 2), y - LABEL_OFFSET * scale, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (warehouseMapModel != null) {
            float maxX = 0, maxY = 0;
            for (Node node : warehouseMapModel.getNodes().values()) {
                maxX = Math.max(maxX, (float) node.x);
                maxY = Math.max(maxY, (float) node.y);
            }

            float canvasWidth = canvas.getWidth();
            float canvasHeight = canvas.getHeight();
            float scaleX = canvasWidth / (maxX + 300);
            float scaleY = canvasHeight / (maxY + 300);
            float scale = Math.min(scaleX, scaleY);
            float offsetX = (canvasWidth - maxX * scale) / 2;
            float offsetY = (canvasHeight - maxY * scale) / 2;

            // Draw nodes
            for (Node node : warehouseMapModel.getNodes().values()) {
                float rectX = (float) (offsetX + node.x * scale);
                float rectY = (float) (offsetY + node.y * scale);

                if (node.id.equals("Entrance")) {
                    drawRectWithLabel(canvas, rectX, rectY, ENTRANCE_EXIT_SIZE, Color.GREEN, "Entrance", scale);
                } else if (node.id.equals("Exit")) {
                    drawRectWithLabel(canvas, rectX, rectY, ENTRANCE_EXIT_SIZE, Color.RED, "Exit", scale);
                } else if (node.id.startsWith("Shelf")) {
                    paint.setColor(Color.BLUE);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(
                            rectX - SHELF_WIDTH * scale, rectY - SHELF_HEIGHT * scale,
                            rectX + SHELF_WIDTH * scale, rectY + SHELF_HEIGHT * scale,
                            paint);
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(TEXT_SIZE);
                    canvas.drawText(node.id, rectX - (paint.measureText(node.id) / 2), rectY - LABEL_OFFSET * scale, paint);
                }
            }

            // Draw warehouse paths (only once for each edge)
            paint.setColor(Color.GRAY);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2 * scale);
            Set<String> drawnEdges = new HashSet<>();
            for (Node node : warehouseMapModel.getNodes().values()) {
                for (Map.Entry<Node, Integer> neighborEntry : node.neighbors.entrySet()) {
                    Node neighbor = neighborEntry.getKey();
                    String edgeKey = node.id + "-" + neighbor.id;
                    String reverseKey = neighbor.id + "-" + node.id;
                    if (!drawnEdges.contains(reverseKey)) {
                        canvas.drawLine(
                                (float) (offsetX + node.x * scale), (float) (offsetY + node.y * scale),
                                (float) (offsetX + neighbor.x * scale), (float) (offsetY + neighbor.y * scale),
                                paint);
                        drawnEdges.add(edgeKey);
                    }
                }
            }

            // Draw navigation path (Queue)
            if (showNavigationPath && navigationPath != null) {
                paint.setColor(Color.RED);
                paint.setStrokeWidth(4 * scale);
                Node prev = null;
                for (Node node : navigationPath) {
                    if (prev != null) {
                        canvas.drawLine(
                                (float) (offsetX + prev.x * scale), (float) (offsetY + prev.y * scale),
                                (float) (offsetX + node.x * scale), (float) (offsetY + node.y * scale),
                                paint);
                    }
                    prev = node;
                }
            }

            // Draw highlighted path (List)
            if (highlightedPath != null && highlightedPath.size() > 1) {
                paint.setColor(Color.RED);
                paint.setStrokeWidth(6 * scale);
                for (int i = 0; i < highlightedPath.size() - 1; i++) {
                    Node a = highlightedPath.get(i);
                    Node b = highlightedPath.get(i + 1);
                    canvas.drawLine(
                            (float) (offsetX + a.x * scale), (float) (offsetY + a.y * scale),
                            (float) (offsetX + b.x * scale), (float) (offsetY + b.y * scale),
                            paint);
                }
            }
        }
    }
}
