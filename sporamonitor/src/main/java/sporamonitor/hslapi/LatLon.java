package sporamonitor.hslapi;

/**
 * Represents latitude-longitude coordinates
 */
public class LatLon {
    private final double lat, lon;

    /**
     * Creates a new latitude-longitude pair
     * @param lat
     * @param lon
     */
    public LatLon(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Returns the latitude component of the coordinates
     */
    public double lat() {
        return lat;
    }

    /**
     * Returns the longitude component of the coordinates
     */
    public double lon() {
        return lon;
    }
}
