package sporamonitor.ui;

import sporamonitor.domain.VehicleRepository;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class RootController {
    private final MapController mapController;

    public RootController() {
        Frame frame = new Frame("Sporamonitor");
        VehicleRepository vehicleRepository = new VehicleRepository();

        this.mapController = new MapController(frame, vehicleRepository);

        frame.pack();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Close application when the window is closed
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        vehicleRepository.startPolling();
    }
}
