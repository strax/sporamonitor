package sporamonitor.ui;

import com.sun.awt.AWTUtilities;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.awt.util.AwtUtil;
import org.mapsforge.map.awt.view.MapView;
import org.mapsforge.map.controller.FrameBufferController;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.debug.TileCoordinatesLayer;
import org.mapsforge.map.layer.debug.TileGridLayer;
import org.mapsforge.map.layer.download.TileDownloadLayer;
import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik;
import org.mapsforge.map.layer.download.tilesource.TileSource;
import org.mapsforge.map.layer.overlay.*;
import org.mapsforge.map.layer.overlay.Polygon;
import org.mapsforge.map.layer.renderer.MapWorkerPool;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.reader.ReadBuffer;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.UUID;

public class MapController {
    private final MapView view;
    private final static GraphicFactory GRAPHIC_FACTORY = AwtGraphicFactory.INSTANCE;
    private final static LatLong HELSINKI_COORDINATES = new LatLong(60.1699, 24.9384);
    private final static byte DEFAULT_ZOOM_LEVEL = 16;
    private final static byte MIN_ZOOM_LEVEL = 12;

    public MapController(Frame frame) {
        MapWorkerPool.NUMBER_OF_THREADS = 2;
        ReadBuffer.MAXIMUM_BUFFER_SIZE = 6500000;
        FrameBufferController.SQUARE_FRAME_BUFFER = false;
        view = new MapView();
        view.getMapScaleBar().setVisible(true);
        view.getFpsCounter().setVisible(true);
        BoundingBox bb = addLayers();

        frame.add(view);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                view.getModel().mapViewPosition.setMapPosition(new MapPosition(HELSINKI_COORDINATES, DEFAULT_ZOOM_LEVEL));
            }
        });
    }

    private BoundingBox addLayers() {
        Layers layers = view.getLayerManager().getLayers();
        System.out.println(System.getProperty("java.io.tmpdir"));

        TileCache tileCache = AwtUtil.createTileCache(
                view.getModel().displayModel.getTileSize(),
                view.getModel().frameBufferModel.getOverdrawFactor(),
                1024,
                new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString())
        );

        final BoundingBox boundingBox;

        view.getModel().displayModel.setFixedTileSize(256);
        TileSource tileSource = OpenStreetMapMapnik.INSTANCE;
        TileDownloadLayer tileDownloadLayer = createTileDownloadLayer(tileCache, view.getModel().mapViewPosition, tileSource);
        layers.add(tileDownloadLayer);
        tileDownloadLayer.start();
        view.setZoomLevelMax(tileSource.getZoomLevelMax());
        view.setZoomLevelMin(MIN_ZOOM_LEVEL);
        boundingBox = new BoundingBox(LatLongUtils.LATITUDE_MIN, LatLongUtils.LONGITUDE_MIN, LatLongUtils.LATITUDE_MAX, LatLongUtils.LONGITUDE_MAX);
        layers.add(new TileGridLayer(GRAPHIC_FACTORY, view.getModel().displayModel));
        layers.add(new TileCoordinatesLayer(GRAPHIC_FACTORY, view.getModel().displayModel));

        return boundingBox;
    }

    private TileDownloadLayer createTileDownloadLayer(TileCache tileCache, MapViewPosition mapViewPosition, TileSource tileSource) {
        return new TileDownloadLayer(tileCache, mapViewPosition, tileSource, GRAPHIC_FACTORY) {
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                return true;
            }
        };
    }
}
