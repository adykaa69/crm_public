package hu.bhr.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

public class HttpRequestFactory {

    public static HttpRequest createDelete(String path) throws URISyntaxException {
        return HttpRequest
                .newBuilder()
                .uri(new URI(path))
//                .headers("Authorization", "Bearer " + token)
                .DELETE()
                .build();
    }

    public static HttpRequest createGet(String path) throws URISyntaxException {
        return HttpRequest
                .newBuilder()
                .uri(new URI(path))
//                .headers("Authorization", "Bearer " + token)
                .GET()
                .build();
    }

    public static HttpRequest createPost(String path, String body) throws URISyntaxException {
        return HttpRequest
                .newBuilder()
                .uri(new URI(path))
//                .headers("Content-Type", "application/json", "Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body.getBytes(StandardCharsets.UTF_8)))
                .build();
    }

}
