package sporamonitor.hslapi;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a HSL transport vehicle currently in transit. A vehicle is associated to a line (code) such as tram
 * line number 9 and an unique vehicle ID.
 */
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

    /**
     * Returns the code (line ID) of this vehicle
     */
    public String code() {
        return code;
    }

    /**
     * Returns the current coordinates of the vehicle
     */
    public LatLon coordinates() {
        return coordinates;
    }

    /**
     * Returns the current bearing of the vehicle, if available
     */
    public Double bearing() {
        return bearing;
    }

    /**
     * Returns the unique vehicle ID of this vehicle
     */
    public String vehicleId() {
        return vehicleId;
    }

    /**
     * Returns the delay of the vehicle, if available
     */
    public Double delay() {
        return delay;
    }

    /**
     * Returns the type of the vehicle. Currently only tram vehicles are supported.
     * @return TYPE_TRAM if the vehicle is a tram, TYPE_UNKNOWN otherwise
     */
    public String type() {
        if (code.startsWith(TYPE_TRAM)) {
            return TYPE_TRAM;
        } else {
            return TYPE_UNKNOWN;
        }
    }

    /**
     * Returns the display name of the vehicle
     */
    public String displayName() {
        if (type().equals(TYPE_TRAM)) {
            return code.substring(3);
        } else {
            return code;
        }
    }
}
