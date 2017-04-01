package sporamonitor.hslapi;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.asynchttpclient.AsyncHttpClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.asynchttpclient.Dsl.*;

public class HSLLiveClient {
    private static final String ENDPOINT_URI = "http://dev.hsl.fi/siriaccess/vm/json?operatorRef=HSL";

    public CompletableFuture<List<Vehicle>> vehicles() throws IOException {
        AsyncHttpClient asyncHttpClient = asyncHttpClient();
        return asyncHttpClient
                .prepareGet(ENDPOINT_URI)
                .execute()
                .toCompletableFuture()
                .thenApply(resp -> resp.getResponseBody(Charset.forName("UTF-8")))
                .thenApply(this::parseSiriJson);
    }

    private List<Vehicle> parseSiriJson(String json) {
        Gson gson = buildGson();
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
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Vehicle.class, new VehicleDeserializer());
        return builder.create();
    }
}
