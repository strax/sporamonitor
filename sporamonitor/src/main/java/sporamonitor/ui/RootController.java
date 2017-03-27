package sporamonitor.ui;

import java.awt.*;

public class RootController {
    private final MapController mapController;

    public RootController() {
        Frame frame = new Frame("Sporamonitor");

        this.mapController = new MapController(frame);

        frame.pack();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
