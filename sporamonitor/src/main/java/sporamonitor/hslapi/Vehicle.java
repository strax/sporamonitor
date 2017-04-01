package sporamonitor.hslapi;

import com.google.gson.annotations.SerializedName;

public class Vehicle {
    public static final String TYPE_TRAM = "100";
    public static final String TYPE_UNKNOWN = "unknown";

    @SerializedName("LineRef")
    private final String code;

    @SerializedName("VehicleLocation")
    private final LatLon coordinates;

    @SerializedName("Bearing")
    private final Double bearing;

    @SerializedName("VehicleRef")
    private final String vehicleId;

    @SerializedName("Delay")
    private final Double delay;

    public Vehicle(String code, LatLon coordinates, Double bearing, String vehicleId, Double delay) {
        this.code = code;
        this.coordinates = coordinates;
        this.bearing = bearing;
        this.vehicleId = vehicleId;
        this.delay = delay;
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

    public Double delay() { return delay; }

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
