package sporamonitor.hslapi;

import org.junit.Test;

import static org.junit.Assert.*;

public class VehicleTest {
    @Test
    public void testCode() {
        Vehicle vehicle = new Vehicle("abcd", null, null);
        assertEquals(vehicle.code(), "abcd");
    }

    @Test
    public void testTramTypeFromCode() {
        Vehicle vehicle1 = new Vehicle("1006T", null, null);
        assertEquals(vehicle1.type(), Vehicle.TYPE_TRAM);

        Vehicle vehicle2 = new Vehicle("100X", null, null);
        assertEquals(vehicle2.type(), Vehicle.TYPE_TRAM);
    }

    @Test
    public void testUnknownTypeFromCode() {
        Vehicle vehicle = new Vehicle("9996T", null, null);
        assertEquals(vehicle.type(), Vehicle.TYPE_UNKNOWN);
    }

    @Test
    public void testTramDisplayName() {
        Vehicle vehicle = new Vehicle("1006T", null, null);
        assertEquals("6T", vehicle.displayName());
    }

    @Test
    public void testUnknownDisplayName() {
        Vehicle vehicle = new Vehicle("9996T", null, null);
        assertEquals("9996T", vehicle.displayName());
    }

    @Test
    public void testCoordinates() {
        LatLon coordinates = new LatLon(123.456,123.456);
        Vehicle vehicle = new Vehicle(null, coordinates, null);
        assertSame(coordinates, vehicle.coordinates());
    }

    @Test
    public void testBearing() {
        double bearing = Math.random();
        Vehicle vehicle = new Vehicle(null, null, bearing);
        assertEquals(bearing, vehicle.bearing(), 0.01);
    }
}
