package sporamonitor.hslapi;

import org.junit.Test;
import static org.junit.Assert.*;

public class LatLonTest {
    @Test
    public void testLat() {
        LatLon coordinates = new LatLon(1.23, 4.56);
        assertEquals(coordinates.lat(), 1.23, 0.01);
    }

    @Test
    public void testLon() {
        LatLon coordinates = new LatLon(1.23, 4.56);
        assertEquals(coordinates.lon(), 4.56, 0.01);
    }
}