package com.spotifyrecs.spotify_recs;

import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import com.dnlcode.api_toolkit.client.HttpApiClient;
import com.dnlcode.api_toolkit.model.HttpRequestDetails;
import com.dnlcode.api_toolkit.model.HttpResponseDetails;
import com.fasterxml.jackson.core.type.TypeReference;

public class SpotifyAuthService {
    private final String clientId;
    private final String clientSecret;
    private String accessToken;
    private long tokenExpiration;

    public SpotifyAuthService() {
        String envClientId = System.getenv("SPOTIFY_CLIENT_ID");
        String envClientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");

        if (envClientId == null || envClientId.isEmpty()) {
            throw new IllegalArgumentException("SPOTIFY_CLIENT_ID no está definido en el entorno.");
        }
        if (envClientSecret == null || envClientSecret.isEmpty()) {
            throw new IllegalArgumentException("SPOTIFY_CLIENT_SECRET no está definido en el entorno.");
        }

        this.clientId = envClientId;
        this.clientSecret = envClientSecret;
    }

     // [ ] Solicita el access token a Spotify
    private void requestAccessToken() {
        try {

			HttpRequestDetails request = new HttpRequestDetails.Builder("https://accounts.spotify.com/api/token", "POST")
                .headers(Map.of(
                "Authorization", List.of(createBasicToken()),
                "Content-Type", List.of("application/x-www-form-urlencoded")
                ))
                .body("grant_type=client_credentials")
                .timeoutMillis(5000)
                .build();
			HttpApiClient client = new HttpApiClient();
			long requestStartTime = System.currentTimeMillis(); // Inicia el temporizador
			HttpResponse<String> response = client.send(request);
			// Process the response
			HttpResponseDetails responseDetails = new HttpResponseDetails.Builder(response.uri().toString(), response.request().method())
				.headers(response.headers().map())
				.body(response.body())
				.responseTimestamp(System.currentTimeMillis())
				.statusCode(response.statusCode())
				.protocolVersion(response.version())
				.cookies(response.headers().allValues("Set-Cookie"))
				.elapsedTime(System.currentTimeMillis() - requestStartTime) // Calcula el tiempo transcurrido
				.build();

			processTokenResponse(responseDetails.getBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    // [ ] Procesa la respuesta del callback de Spotify
    private void processTokenResponse(String responseBody) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> jsonMap = mapper.readValue(
                responseBody,
                new TypeReference<Map<String, Object>>() {}
            );

            this.accessToken = (String) jsonMap.get("access_token");
            int expiresIn = ((Number) jsonMap.get("expires_in")).intValue();
            this.tokenExpiration = System.currentTimeMillis() + expiresIn * 1000L;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al procesar la respuesta del token de Spotify");
        }
    }

    // [ ] Verifica si el token está vigente
    public boolean isTokenValid() {
        return this.accessToken != null
                && !this.accessToken.isEmpty()
                && System.currentTimeMillis() < this.tokenExpiration;
    }
    
    // [ ] Método público para obtener el access token
    public String getAccessToken() {
        return this.accessToken;
    }

    private String createBasicToken() {
    String credentials = this.clientId + ":" + this.clientSecret;
    // Codifica en base64
    String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());
    // Devuelve el token en formato "Basic <base64>"
    return "Basic " + encoded;
}

}