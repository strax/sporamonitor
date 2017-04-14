package sporamonitor.map.tilesource;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.download.tilesource.AbstractTileSource;

import java.net.MalformedURLException;
import java.net.URL;

public class MapboxTileSource extends AbstractTileSource {

    private static final String[] HOSTNAMES = new String[] { "api.mapbox.com" };

    private final String styleName;
    private final String apiKey;
    private final String username;

    public MapboxTileSource(String apiKey, String username, String styleName) {
        super(HOSTNAMES, 443);
        this.apiKey = apiKey;
        this.username = username;
        this.styleName = styleName;
    }

    @Override
    public int getParallelRequestsLimit() {
        return 20;
    }

    @Override
    public URL getTileUrl(Tile tile) throws MalformedURLException {
        return new URL("https", getHostName(), this.port, getTilePath(tile));
    }

    @Override
    public byte getZoomLevelMax() {
        return 18;
    }

    @Override
    public byte getZoomLevelMin() {
        return 0;
    }

    @Override
    public boolean hasAlpha() {
        return false;
    }

    private String getTilePath(Tile tile) {
        return "/styles/v1/" + this.username + "/" + this.styleName + "/tiles/" + tile.tileSize + "/" + tile.zoomLevel + "/" + tile.tileX + "/" + tile.tileY + "?access_token=" + this.apiKey;
    }
}
