package sporamonitor;

import sporamonitor.hslapi.HSLLiveClient;
import sporamonitor.hslapi.Vehicle;
import sporamonitor.ui.RootController;
import sporamonitor.ui.RootView;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        HSLLiveClient client = new HSLLiveClient();
        List<Vehicle> vehicles = client.vehicles().get();
    }
}
