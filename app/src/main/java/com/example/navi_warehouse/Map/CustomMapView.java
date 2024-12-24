package com.example.navi_warehouse.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.navi_warehouse.Map.WarehouseMapModel;
import com.example.navi_warehouse.Map.WarehouseMapModel.Node;

import java.util.Map;

public class CustomMapView extends View {

    private WarehouseMapModel warehouseMapModel;
    private Paint paint;

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
        invalidate(); // Request to redraw the view with the new model
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (warehouseMapModel != null) {
            // Calculate bounds of warehouse model for dynamic scaling
            float maxX = 0, maxY = 0;
            for (Node node : warehouseMapModel.getNodes().values()) {
                if (node.x > maxX) maxX = node.x;
                if (node.y > maxY) maxY = node.y;
            }

            // Get the canvas width and height for scaling
            float canvasWidth = canvas.getWidth();
            float canvasHeight = canvas.getHeight();
            float scaleX = canvasWidth / (maxX + 300); // Increase padding to prevent overlap
            float scaleY = canvasHeight / (maxY + 300);
            float scale = Math.min(scaleX, scaleY); // Use the smaller scale to fit both dimensions

            // Centering the content
            float offsetX = (canvasWidth - maxX * scale) / 2;
            float offsetY = (canvasHeight - maxY * scale) / 2;

            // Draw shelves (货架)
            paint.setColor(Color.GRAY); // 使用灰色来表示货架
            paint.setStyle(Paint.Style.FILL);

            for (Node node : warehouseMapModel.getNodes().values()) {
                if (node.id.startsWith("Shelf")) { // 假设货架节点以 "Shelf" 开头
                    // 缩小货架的大小以增加节点之间的距离
                    canvas.drawRect(
                            offsetX + node.x * scale - 20f * scale, offsetY + node.y * scale - 40f * scale,
                            offsetX + node.x * scale + 20f * scale, offsetY + node.y * scale + 40f * scale,
                            paint
                    );
                }
            }

            // Draw aisles (通道) - 绘制连接货架的路径
            paint.setColor(Color.RED); // 使用红色来表示通道
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2 * scale); // Adjust stroke width for better visibility

            for (Node node : warehouseMapModel.getNodes().values()) {
                for (Map.Entry<Node, Integer> neighborEntry : node.neighbors.entrySet()) {
                    Node neighbor = neighborEntry.getKey();
                    // 绘制货架之间的连接线（通道）
                    canvas.drawLine(
                            offsetX + node.x * scale, offsetY + node.y * scale,
                            offsetX + neighbor.x * scale, offsetY + neighbor.y * scale,
                            paint
                    );
                }
            }

            // Draw nodes (标记节点) - 用蓝色圆形标记节点位置
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL);

            for (Node node : warehouseMapModel.getNodes().values()) {
                canvas.drawCircle(offsetX + node.x * scale, offsetY + node.y * scale, 5f * scale, paint); // 缩小圆形，使其不与货架冲突
            }
        }
    }

}