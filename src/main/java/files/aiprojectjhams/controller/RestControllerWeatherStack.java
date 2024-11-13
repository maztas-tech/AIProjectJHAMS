package files.aiprojectjhams.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RestController
public class RestControllerWeatherStack {

    @Value("${APIWEATHER}")
    private String weatherApiKey;


    private final WebClient webClient;

    public RestControllerWeatherStack(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.weatherstack.com/current").build();
    }
    /*
    @GetMapping("/weather")
    public Map<String, Object> getWeather(@RequestParam String query) {



    }

     */
}
