package files.aiprojectjhams.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ChatGPTService {

    @Value("${APIWEATHER}")
    private String weatherApiKey;

    private final WebClient webClient2;

    public ChatGPTService(WebClient.Builder webClientBuilder) {
        this.webClient2 = webClientBuilder.baseUrl("https://api.weatherstack.com/current").build();
    }

    public String getWeather(@RequestParam String city) {
        return webClient2.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("access_key", weatherApiKey)
                        .queryParam("query", city)
                        .build())
                .retrieve()
                .bodyToMono(String.class).block(); // Use String for a raw JSON response
    }




}
