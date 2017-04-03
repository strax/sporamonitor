package sporamonitor.ui;

import org.mapsforge.core.graphics.*;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.layer.overlay.FixedPixelCircle;

public class VehicleMarker extends FixedPixelCircle {
    private final GraphicFactory graphicFactory;
    private String label;

    public VehicleMarker(LatLong latLong, String label, GraphicFactory graphicFactory) {
        super(latLong, 20f, paintFill(graphicFactory), paintStroke(graphicFactory), true);
        this.label = label;
        this.graphicFactory = graphicFactory;
    }

    @Override
    public synchronized void draw(BoundingBox bb, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        super.draw(bb, zoomLevel, canvas, topLeftPoint);
        long mapSize = MercatorProjection.getMapSize(zoomLevel, displayModel.getTileSize());
        int pixelX = (int) (MercatorProjection.longitudeToPixelX(getPosition().getLongitude(), mapSize) - topLeftPoint.x);
        int pixelY = (int) (MercatorProjection.latitudeToPixelY(getPosition().getLatitude(), mapSize) - topLeftPoint.y);
        canvas.drawText(label, pixelX, pixelY, paintText(graphicFactory));
    }

    private static Paint paintFill(GraphicFactory graphicFactory) {
        Paint p = graphicFactory.createPaint();
        p.setColor(Color.RED);
        return p;
    }

    private static Paint paintStroke(GraphicFactory graphicFactory) {
        Paint p = graphicFactory.createPaint();
        p.setStrokeWidth(0);
        return p;
    }

    private static Paint paintText(GraphicFactory graphicFactory) {
        Paint p = graphicFactory.createPaint();
        p.setTextSize(14.0f);
        p.setColor(Color.WHITE);
        p.setTypeface(FontFamily.fromString("sans_serif"), FontStyle.BOLD);
        return p;
    }
}
