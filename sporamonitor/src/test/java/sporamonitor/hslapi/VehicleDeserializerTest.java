package sporamonitor.hslapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import static org.junit.Assert.*;

public class VehicleDeserializerTest {
    private final static String FIXTURE = "{\"ValidUntilTime\":1493921936640,\"RecordedAtTime\":1493921906640,\"MonitoredVehicleJourney\":{\"LineRef\":{\"value\":\"4611\"},\"DirectionRef\":{\"value\":\"1\"},\"FramedVehicleJourneyRef\":{\"DataFrameRef\":{\"value\":\"2017-05-04\"},\"DatedVehicleJourneyRef\":\"2123\"},\"OperatorRef\":{\"value\":\"HSL\"},\"Monitored\":true,\"VehicleLocation\":{\"Longitude\":24.94304,\"Latitude\":60.17127},\"MonitoredCall\":{},\"VehicleRef\":{\"value\":\"1003\"}}}";

    @Test
    public void testDeserialize() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Vehicle.class, new VehicleDeserializer());
        Gson gson = builder.create();
        Vehicle vehicle = gson.fromJson(FIXTURE, Vehicle.class);
        assertEquals(vehicle.code(), "4611");
        assertEquals(vehicle.coordinates().lon(), 24.94304, 0.001);
        assertEquals(vehicle.coordinates().lat(), 60.17127, 0.001);
        assertEquals(vehicle.vehicleId(), "1003");
    }
}
