package sporamonitor.ui;

import com.sun.awt.AWTUtilities;
import java.util.List;
import org.mapsforge.core.graphics.*;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.awt.util.AwtUtil;
import org.mapsforge.map.awt.view.MapView;
import org.mapsforge.map.controller.FrameBufferController;
import org.mapsforge.map.layer.GroupLayer;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.debug.TileCoordinatesLayer;
import org.mapsforge.map.layer.debug.TileGridLayer;
import org.mapsforge.map.layer.download.TileDownloadLayer;
import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik;
import org.mapsforge.map.layer.download.tilesource.TileSource;
import org.mapsforge.map.layer.renderer.MapWorkerPool;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.reader.ReadBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sporamonitor.hslapi.HSLLiveClient;
import sporamonitor.hslapi.Vehicle;
import sporamonitor.map.tilesource.MapboxTileSource;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MapController {
    private final static Logger LOGGER = LoggerFactory.getLogger(MapController.class);

    private final static GraphicFactory GRAPHIC_FACTORY = AwtGraphicFactory.INSTANCE;
    private final static LatLong HELSINKI_COORDINATES = new LatLong(60.1699, 24.9384);
    private final static byte DEFAULT_ZOOM_LEVEL = 16;
    private final static byte MIN_ZOOM_LEVEL = 12;

    private final MapView view;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    private final HSLLiveClient client = new HSLLiveClient();

    public MapController(Frame frame) {
        MapWorkerPool.NUMBER_OF_THREADS = 1;
        ReadBuffer.MAXIMUM_BUFFER_SIZE = 6500000;
        FrameBufferController.SQUARE_FRAME_BUFFER = true;
        view = new MapView();
        view.getModel().frameBufferModel.setOverdrawFactor(1.0f);
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

        TileCache tileCache = AwtUtil.createTileCache(
                view.getModel().displayModel.getTileSize(),
                view.getModel().frameBufferModel.getOverdrawFactor(),
                1024,
                new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString())
        );

        final BoundingBox boundingBox;

        view.getModel().displayModel.setFixedTileSize(256);
        TileSource tileSource = new MapboxTileSource("pk.eyJ1Ijoic2FtaWt1a2tvbmVuMiIsImEiOiJjajExdWs1YnMwMDRhMzJwYm56dnZhdWVoIn0.AryketxtTQkdLlr8TwRyjw", "samikukkonen2", "cj11v1wl6005m2ro9pnyj8c6e");
        TileDownloadLayer tileDownloadLayer = createTileDownloadLayer(tileCache, view.getModel().mapViewPosition, tileSource);
        layers.add(tileDownloadLayer);

        GroupLayer markerLayer = new GroupLayer();
        layers.add(markerLayer);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            synchronized (layers) {
                updateMarkerLayer(markerLayer);
            }
        }, 0, 1, TimeUnit.SECONDS);

        tileDownloadLayer.start();
        view.setZoomLevelMax(tileSource.getZoomLevelMax());
        view.setZoomLevelMin(MIN_ZOOM_LEVEL);
        boundingBox = new BoundingBox(LatLongUtils.LATITUDE_MIN, LatLongUtils.LONGITUDE_MIN, LatLongUtils.LATITUDE_MAX, LatLongUtils.LONGITUDE_MAX);

        return boundingBox;
    }

    private TileDownloadLayer createTileDownloadLayer(TileCache tileCache, MapViewPosition mapViewPosition, TileSource tileSource) {
        return new TileDownloadLayer(tileCache, mapViewPosition, tileSource, GRAPHIC_FACTORY) {
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                return true;
            }
        };
    }

    private void updateMarkerLayer(GroupLayer groupLayer) {
        groupLayer.layers.clear();
        try {
            client.vehicles().get().stream().filter(v -> v.type().equals(Vehicle.TYPE_TRAM)).forEach(vehicle -> {
                LatLong coordinates = new LatLong(vehicle.coordinates().lat(), vehicle.coordinates().lon());
                VehicleMarker marker = new VehicleMarker(coordinates, vehicle.displayName(), GRAPHIC_FACTORY);
                groupLayer.layers.add(marker);
            });
        } catch (Exception e) {
            LOGGER.error("Could not fetch vehicle positions", e);
        }
        // Propagate displaymodel to new sublayers
        groupLayer.setDisplayModel(view.getModel().displayModel);
        groupLayer.requestRedraw();
    }
}
