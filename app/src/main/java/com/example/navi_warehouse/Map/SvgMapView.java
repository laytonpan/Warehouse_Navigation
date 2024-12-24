//package com.example.navi_warehouse.Map;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.util.AttributeSet;
//import android.view.View;
//import com.caverock.androidsvg.SVG;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.util.AttributeSet;
//import android.view.View;
//import com.caverock.androidsvg.SVG;
//import com.caverock.androidsvg.SVGParseException;
//
//public class SvgMapView extends View {
//
//    private SVG svg;
//    private Matrix matrix = new Matrix();
//
//    public SvgMapView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public void loadSvg(int resourceId, Context context) {
//        try {
//            svg = SVG.getFromResource(context, resourceId);
//            invalidate();
//        } catch (SVGParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (svg != null) {
//            canvas.save();
//            canvas.concat(matrix);
//            svg.renderToCanvas(canvas);
//            canvas.restore();
//        }
//    }
//
//    public void setScale(float scaleFactor) {
//        matrix.setScale(scaleFactor, scaleFactor);
//        invalidate();
//    }
//
//    public void setRotation(float degrees) {
//        matrix.setRotate(degrees);
//        invalidate();
//    }
//}