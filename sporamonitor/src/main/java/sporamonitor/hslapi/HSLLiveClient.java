package sporamonitor.hslapi;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * An API client to HSL's Live API (http://dev.hsl.fi/).
 */
public class HSLLiveClient {
    private static final String ENDPOINT_URI = "http://dev.hsl.fi/siriaccess/vm/json?operatorRef=HSL";

    private Gson gson;
    private AsyncHttpClient httpClient;

    /**
     * Creates a new HSL Live client.
     */
    public HSLLiveClient() {
        this(Dsl::asyncHttpClient);
    }

    /**
     * Creates a new HSL Live client.
     * @param httpClientSupplier the HTTP client object to use.
     */
    public HSLLiveClient(Supplier<AsyncHttpClient> httpClientSupplier) {
        this.httpClient = httpClientSupplier.get();
    }

    /**
     * Returns the list of vehicles currently in transit by HSL.
     * @return a future containing a list of vehicles
     * @throws IOException if the fetch operation failed
     */
    public CompletableFuture<List<Vehicle>> vehicles() throws IOException {
        return httpClient
                .prepareGet(ENDPOINT_URI)
                .execute()
                .toCompletableFuture()
                .thenApply(resp -> resp.getResponseBody(Charset.forName("UTF-8")))
                .thenApply(this::parseSiriJson);
    }

    private List<Vehicle> parseSiriJson(String json) {
        JsonParser parser = new JsonParser();
        JsonElement container = parser.parse(json);
        JsonArray vehicleActivity = container
                .getAsJsonObject()
                .getAsJsonObject("Siri")
                .getAsJsonObject("ServiceDelivery")
                .getAsJsonArray("VehicleMonitoringDelivery")
                .get(0).getAsJsonObject()
                .getAsJsonArray("VehicleActivity");
        Type listType = new TypeToken<List<Vehicle>>() { }.getType();
        return buildGson().fromJson(vehicleActivity, listType);
    }

    private Gson buildGson() {
        if (gson != null) {
            return gson;
        }
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Vehicle.class, new VehicleDeserializer());
        gson = builder.create();
        return gson;
    }
}
