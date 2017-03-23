package sporamonitor.hslapi;

public class Vehicle {
    public static final String TYPE_TRAM = "100";
    public static final String TYPE_UNKNOWN = "unknown";

    private final String code;
    private final LatLon coordinates;
    private final Double bearing;
    private final String vehicleId;

    public Vehicle(String code, LatLon coordinates, Double bearing, String vehicleId) {
        this.code = code;
        this.coordinates = coordinates;
        this.bearing = bearing;
        this.vehicleId = vehicleId;
    }

    public String code() {
        return code;
    }

    public LatLon coordinates() {
        return coordinates;
    }

    public Double bearing() {
        return bearing;
    }

    public String vehicleId() {
        return vehicleId;
    }

    public String type() {
        if (code.startsWith(TYPE_TRAM)) {
            return TYPE_TRAM;
        } else {
            return TYPE_UNKNOWN;
        }
    }

    public String displayName() {
        if (type().equals(TYPE_TRAM)) {
            return code.substring(3);
        } else {
            return code;
        }
    }
}
