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
 * An API client to HSL's Live API (http://dev.hsl.fi/)
 */
public class HSLLiveClient {
    private static final String ENDPOINT_URI = "http://dev.hsl.fi/siriaccess/vm/json?operatorRef=HSL";

    private Gson gson;
    private Supplier<AsyncHttpClient> httpClientSupplier;

    public HSLLiveClient() {
        this(Dsl::asyncHttpClient);
    }

    public HSLLiveClient(Supplier<AsyncHttpClient> httpClientSupplier) {
        this.httpClientSupplier = httpClientSupplier;
    }

    /**
     * Returns the list of vehicles currently in transit by HSL
     * @returns a future containing a list of vehicles
     * @throws IOException
     */
    public CompletableFuture<List<Vehicle>> vehicles() throws IOException {
        AsyncHttpClient asyncHttpClient = httpClientSupplier.get();
        return asyncHttpClient
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
        Type listType = new TypeToken<List<Vehicle>>(){}.getType();
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
