package client.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import shared.PredictionResult;
import shared.StudentData;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.List;
import java.util.Map;
public class ApiService {

    private final String      baseUrl;
    private final ObjectMapper mapper  = new ObjectMapper();
    private final HttpClient   client  = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public ApiService(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public boolean isServerAlive() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/health"))
                    .GET().timeout(Duration.ofSeconds(3))
                    .build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            return resp.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
    public PredictionResult predict(StudentData data) throws Exception {
        String body = mapper.writeValueAsString(data);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/predict"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(30))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            throw new RuntimeException("Server error " + resp.statusCode() + ": " + resp.body());
        }
        return mapper.readValue(resp.body(), PredictionResult.class);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllStudents() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/students"))
                .GET().timeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(resp.body(), List.class);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPredictionHistory() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/history"))
                .GET().timeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            return List.of();
        }
        return mapper.readValue(resp.body(), List.class);
    }
    @SuppressWarnings("unchecked")
    public Map<String, String> getInsights() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/insights"))
                .GET().timeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            return Map.of();
        }
        return mapper.readValue(resp.body(), Map.class);
    }
}
