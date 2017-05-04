package sporamonitor.domain;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import sporamonitor.hslapi.HSLLiveClient;
import sporamonitor.hslapi.Vehicle;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class VehicleRepositoryTest {
    @Captor ArgumentCaptor<List<Vehicle>> captor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSubscribe() throws Exception {
        Vehicle vehicle = mock(Vehicle.class);

        List<Vehicle> vehicles = Collections.singletonList(vehicle);

        CompletableFuture<List<Vehicle>> future = new CompletableFuture<List<Vehicle>>();
        future.complete(vehicles);
        HSLLiveClient client = mock(HSLLiveClient.class);
        when(client.vehicles()).thenReturn(future);

        VehicleRepository repository = new VehicleRepository(client);

        Consumer<Collection<Vehicle>> observer = mock(Consumer.class);
        repository.subscribe(observer);

        repository.startPolling();

        // Unfortunately JUnit does not support testing concurrency out-of-the-box so naive sleep should do it for now
        Thread.sleep(20);

        verify(observer).accept(captor.capture());
        assertThat(captor.getValue(), hasItem(vehicle));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUnsubscribe() throws Exception {
        List<Vehicle> vehicles = Collections.emptyList();

        CompletableFuture<List<Vehicle>> future = new CompletableFuture<List<Vehicle>>();
        future.complete(vehicles);
        HSLLiveClient client = mock(HSLLiveClient.class);
        when(client.vehicles()).thenReturn(future);

        VehicleRepository repository = new VehicleRepository(client);

        Consumer<Collection<Vehicle>> observer = mock(Consumer.class);
        repository.subscribe(observer);
        repository.unsubscribe(observer);

        repository.startPolling();

        Thread.sleep(20);

        verifyZeroInteractions(observer);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testVehicles() throws Exception {
        Vehicle vehicle = mock(Vehicle.class);
        List<Vehicle> vehicles = Collections.singletonList(vehicle);

        CompletableFuture<List<Vehicle>> future = new CompletableFuture<List<Vehicle>>();
        future.complete(vehicles);
        HSLLiveClient client = mock(HSLLiveClient.class);
        when(client.vehicles()).thenReturn(future);

        VehicleRepository repository = new VehicleRepository(client);
        repository.startPolling();

        Thread.sleep(20);

        assertThat(repository.vehicles(), hasItem(vehicle));
    }
}
