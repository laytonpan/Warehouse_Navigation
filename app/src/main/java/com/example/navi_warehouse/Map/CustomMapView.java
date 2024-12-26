package com.example.navi_warehouse.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.util.Queue;


import com.example.navi_warehouse.Map.WarehouseMapModel;
import com.example.navi_warehouse.Map.WarehouseMapModel.Node;

import java.util.Map;

public class CustomMapView extends View {

    private static final float SHELF_WIDTH = 20f;  // Half-width of a shelf rectangle
    private static final float SHELF_HEIGHT = 40f; // Half-height of a shelf rectangle
    private static final float ENTRANCE_EXIT_SIZE = 30f; // Half-size of entrance/exit rectangles
    private static final float LABEL_OFFSET = 50f; // Distance between labels and rectangles
    private static final float TEXT_SIZE = 50f;    // Size of the text labels

    private WarehouseMapModel warehouseMapModel;
    private Paint paint;
    private boolean showNavigationPath = false; // Toggle for navigation path
    private Queue<WarehouseMapModel.Node> navigationPath;

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
        invalidate(); // Redraw the view with the updated model
    }

    public void setNavigationPath(Queue<WarehouseMapModel.Node> path) {
        this.navigationPath = path;
        this.showNavigationPath = true;
        invalidate(); // Redraw the view with the updated path
    }

    public void clearNavigationPath() {
        this.showNavigationPath = false;
        this.navigationPath = null;
        invalidate(); // Clear and redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (warehouseMapModel != null) {
            // Calculate scaling and offset
            float maxX = 0, maxY = 0;
            for (Node node : warehouseMapModel.getNodes().values()) {
                if (node.x > maxX) maxX = node.x;
                if (node.y > maxY) maxY = node.y;
            }

            float canvasWidth = canvas.getWidth();
            float canvasHeight = canvas.getHeight();
            float scaleX = canvasWidth / (maxX + 300);
            float scaleY = canvasHeight / (maxY + 300);
            float scale = Math.min(scaleX, scaleY);
            float offsetX = (canvasWidth - maxX * scale) / 2;
            float offsetY = (canvasHeight - maxY * scale) / 2;

            // Draw nodes (shelves, entrance, exit)
            for (Node node : warehouseMapModel.getNodes().values()) {
                float rectX = offsetX + node.x * scale;
                float rectY = offsetY + node.y * scale;

                if (node.id.equals("Entrance")) {
                    // Entrance: Green rectangle
                    paint.setColor(Color.GREEN);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(
                            rectX - ENTRANCE_EXIT_SIZE * scale, rectY - ENTRANCE_EXIT_SIZE * scale,
                            rectX + ENTRANCE_EXIT_SIZE * scale, rectY + ENTRANCE_EXIT_SIZE * scale,
                            paint
                    );
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(TEXT_SIZE);
                    canvas.drawText("Entrance", rectX - (paint.measureText("Entrance") / 2),
                            rectY - LABEL_OFFSET * scale, paint);

                } else if (node.id.equals("Exit")) {
                    // Exit: Red rectangle
                    paint.setColor(Color.RED);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(
                            rectX - ENTRANCE_EXIT_SIZE * scale, rectY - ENTRANCE_EXIT_SIZE * scale,
                            rectX + ENTRANCE_EXIT_SIZE * scale, rectY + ENTRANCE_EXIT_SIZE * scale,
                            paint
                    );
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(TEXT_SIZE);
                    canvas.drawText("Exit", rectX - (paint.measureText("Exit") / 2),
                            rectY - LABEL_OFFSET * scale, paint);

                } else if (node.id.startsWith("Shelf")) {
                    // Shelves: Blue rectangles
                    paint.setColor(Color.BLUE);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(
                            rectX - SHELF_WIDTH * scale, rectY - SHELF_HEIGHT * scale,
                            rectX + SHELF_WIDTH * scale, rectY + SHELF_HEIGHT * scale,
                            paint
                    );
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(TEXT_SIZE);
                    canvas.drawText(node.id, rectX - (paint.measureText(node.id) / 2),
                            rectY - LABEL_OFFSET * scale, paint);
                }
            }

            // Draw warehouse paths
            paint.setColor(Color.GRAY);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2 * scale);

            for (Node node : warehouseMapModel.getNodes().values()) {
                for (Map.Entry<Node, Integer> neighborEntry : node.neighbors.entrySet()) {
                    Node neighbor = neighborEntry.getKey();
                    canvas.drawLine(
                            offsetX + node.x * scale, offsetY + node.y * scale,
                            offsetX + neighbor.x * scale, offsetY + neighbor.y * scale,
                            paint
                    );
                }
            }

            // Draw navigation path if enabled
            if (showNavigationPath && navigationPath != null) {
                paint.setColor(Color.RED);
                paint.setStrokeWidth(4 * scale);

                Node prev = null;
                for (Node node : navigationPath) {
                    if (prev != null) {
                        canvas.drawLine(
                                offsetX + prev.x * scale, offsetY + prev.y * scale,
                                offsetX + node.x * scale, offsetY + node.y * scale,
                                paint
                        );
                    }
                    prev = node;
                }
            }
        }
    }
}
