package sporamonitor.hslapi;

import java.net.URI;

public class HSLLiveClient {
    private static URI ENDPOINT_URI = URI.create("http://dev.hsl.fi/siriaccess/vm/json?operatorRef=HSL");

    public HSLLiveClient() {
    }
}
