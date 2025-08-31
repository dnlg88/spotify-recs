package com.spotifyrecs.spotify_recs;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import com.dnlcode.api_toolkit.client.HttpApiClient;
import com.dnlcode.api_toolkit.model.HttpRequestDetails;
import com.dnlcode.api_toolkit.model.HttpResponseDetails;
import com.fasterxml.jackson.core.type.TypeReference;

public class SpotifyArtistService {
    private final SpotifyAuthService authService;

    public SpotifyArtistService(SpotifyAuthService authService) {
        this.authService = authService;
    }

    // Método público para buscar artistas por nombre
    public void searchArtistByName(String name) {
        String token = authService.getAccessToken();
        if (token == null) {
            System.out.println("No se pudo obtener el token de acceso.");
            return;
        }

        String url = "https://api.spotify.com/v1/search?q=" + name + "&type=artist";

        HttpRequestDetails request = new HttpRequestDetails.Builder(url, "GET")
            .headers(Map.of(
                "Authorization", List.of("Bearer " + token),
                "Content-Type", List.of("application/json")
            ))
            .timeoutMillis(5000)
            .build();

        HttpApiClient client = new HttpApiClient();
        long requestStartTime = System.currentTimeMillis();
        try {
            HttpResponse<String> response = client.send(request);
            HttpResponseDetails responseDetails = new HttpResponseDetails.Builder(response.uri().toString(), response.request().method())
                .headers(response.headers().map())
                .body(response.body())
                .responseTimestamp(System.currentTimeMillis())
                .statusCode(response.statusCode())
                .protocolVersion(response.version())
                .cookies(response.headers().allValues("Set-Cookie"))
                .elapsedTime(System.currentTimeMillis() - requestStartTime)
                .build();

            processArtistSearchResponse(responseDetails.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para procesar la respuesta (implementación pendiente)
    private void processArtistSearchResponse(String responseBody) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> jsonMap = mapper.readValue(
                responseBody,
                new TypeReference<Map<String, Object>>() {}
            );

            // Accede a la propiedad "artists"
            Object artistsObj = jsonMap.get("artists");
            if (artistsObj instanceof Map) {
                Map<String, Object> artistsMap = (Map<String, Object>) artistsObj;
                List<Map<String, Object>> items = (List<Map<String, Object>>) artistsMap.get("items");

                System.out.println("Artistas encontrados:");
                for (Map<String, Object> artist : items) {
                    String id = (String) artist.get("id");
                    String name = (String) artist.get("name");
                    int popularity = ((Number) artist.get("popularity")).intValue();
                    List<String> genres = (List<String>) artist.get("genres");
                    Map<String, Object> followers = (Map<String, Object>) artist.get("followers");
                    int totalFollowers = followers != null ? ((Number) followers.get("total")).intValue() : 0;

                    System.out.printf("ID: %s | Nombre: %s | Popularidad: %d | Géneros: %s | Seguidores: %d%n",
                        id, name, popularity, genres, totalFollowers);
                }
            } else {
                System.out.println("No se encontró la propiedad 'artists' en la respuesta.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al procesar la respuesta de búsqueda de artistas");
        }
    }
}
