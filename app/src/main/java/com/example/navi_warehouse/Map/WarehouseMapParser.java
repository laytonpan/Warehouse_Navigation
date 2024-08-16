package com.example.navi_warehouse.Map;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;
import android.util.Xml;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.caverock.androidsvg.SVGParseException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class WarehouseMapParser {

    public static class MapElement {
        public String id;
        public float x, y, width, height;

        public MapElement(String id, float x, float y, float width, float height) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    public List<MapElement> parseSvg(Context context, int svgResourceId) throws XmlPullParserException, IOException {
        List<MapElement> elements = new ArrayList<>();
        InputStream inputStream = context.getResources().openRawResource(svgResourceId);
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        parser.nextTag();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            // Element for rect
            if (name.equals("rect")) {
                String id = parser.getAttributeValue(null, "id");
                float x = Float.parseFloat(parser.getAttributeValue(null, "x"));
                float y = Float.parseFloat(parser.getAttributeValue(null, "y"));
                float width = Float.parseFloat(parser.getAttributeValue(null, "width"));
                float height = Float.parseFloat(parser.getAttributeValue(null, "height"));

                elements.add(new MapElement(id, x, y, width, height));
            }


            // Element for path
        }

        return elements;
    }
}