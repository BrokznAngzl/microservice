package open.microservice.accountmanagement.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class BaseHttpClient {
    private final HttpClient httpClient;
    private final Duration readTimeout;

    private static final String CONTENT_TYPE_JSON = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    protected BaseHttpClient(Duration connectTimeout, Duration readTimeout) {
        this.httpClient = HttpClient.newBuilder().connectTimeout(connectTimeout).build();
        this.readTimeout = readTimeout;
    }

    public HttpResponse<String> post(String endpoint, String requestBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .timeout(readTimeout)
                .header(CONTENT_TYPE_JSON, APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}