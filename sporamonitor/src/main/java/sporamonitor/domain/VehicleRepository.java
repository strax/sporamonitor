package sporamonitor.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sporamonitor.hslapi.HSLLiveClient;
import sporamonitor.hslapi.Vehicle;
import sporamonitor.util.Subject;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * A polling repository class to hold the most up-to-date vehicle positions in a single place.
 */
public class VehicleRepository implements Subject<Collection<Vehicle>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleRepository.class);

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final List<Vehicle> vehicles = Collections.synchronizedList(new ArrayList<>());
    private final HSLLiveClient apiClient;
    private final List<Consumer<Collection<Vehicle>>> observers = new LinkedList<>();
    private final ExecutorService observerTaskExecutorService = ForkJoinPool.commonPool();

    public VehicleRepository(HSLLiveClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Starts to poll for new vehicles periodically.
     */
    public void startPolling() {
        scheduler.scheduleWithFixedDelay(this::fetchVehicles, 0, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * @return a read-only collection of {@link Vehicle} objects
     */
    public Collection<Vehicle> vehicles() {
        return Collections.unmodifiableCollection(vehicles);
    }

    @Override
    public void subscribe(Consumer<Collection<Vehicle>> observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(Consumer<Collection<Vehicle>> observer) throws IllegalArgumentException {
        if (!observers.remove(observer)) {
            throw new IllegalArgumentException("Given observer is not attached to this subject");
        }
    }

    private void notifyObservers(Collection<Vehicle> state) {
        observers.forEach(observer -> {
            observerTaskExecutorService.submit(() -> observer.accept(state));
        });
    }

    private void fetchVehicles() {
        LOGGER.info("Fetching new vehicles");
        synchronized (vehicles) {
            try {
                Collection<Vehicle> newVehicles = apiClient.vehicles().get();
                vehicles.clear();
                vehicles.addAll(newVehicles);
                notifyObservers(vehicles());
                LOGGER.info("Observers notified");
            } catch (Throwable ex) {
                LOGGER.error("Cannot fetch vehicles", ex);
            }
        }
    }
}
