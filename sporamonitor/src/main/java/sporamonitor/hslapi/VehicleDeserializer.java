package sporamonitor.hslapi;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

class VehicleDeserializer implements JsonDeserializer<Vehicle> {
    public Vehicle deserialize(JsonElement element, Type typeOfSrc, JsonDeserializationContext context) {
        JsonObject object = element.getAsJsonObject().getAsJsonObject("MonitoredVehicleJourney");
        String lineRef = object.getAsJsonObject("LineRef").getAsJsonPrimitive("value").getAsString();
        LatLon coordinates = context.deserialize(object.getAsJsonObject("VehicleLocation"), LatLon.class);
        Double bearing = context.deserialize(object.getAsJsonPrimitive("Bearing"), Double.class);
        Double delay = context.deserialize(object.getAsJsonPrimitive("Delay"), Double.class);
        String vehicleRef = object.getAsJsonObject("VehicleRef").getAsJsonPrimitive("value").getAsString();
        return new Vehicle(lineRef, coordinates, bearing, vehicleRef, delay);
    }
}
