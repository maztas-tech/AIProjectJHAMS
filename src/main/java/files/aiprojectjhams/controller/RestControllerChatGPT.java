package files.aiprojectjhams.controller;


import files.aiprojectjhams.dto.chatgpt.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RestControllerChatGPT {

    @Value("${API}")
    private String openAiKey;

    @Value("${APIWEATHER}")
    private String weatherApiKey;

    private final WebClient webClient;
    private final WebClient webClient2;


    public RestControllerChatGPT(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
        this.webClient2 = webClientBuilder.baseUrl("https://api.weatherstack.com/current").build();
    }


    @GetMapping("/chat")
    public Map<String, Object> chat(@RequestParam String city) {
        ChatRequest chatRequest = new ChatRequest();

        chatRequest.setModel("gpt-4");
        List<Message> listMessages = new ArrayList<>();
        listMessages.add(new Message("system", "You are a weather forecaster that looks for the forecast of cities across the world. i would also like for u to give suggestions on outdoor activity and clothing based on weather"));
        listMessages.add(new Message("user", getWeather(city)));
        chatRequest.setMessages(listMessages);
        chatRequest.setN(3);
        chatRequest.setTemperature(1);
        chatRequest.setMaxTokens(200);
        chatRequest.setStream(false);
        chatRequest.setPresencePenalty(1);

        ChatResponse chatResponse = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(openAiKey))
                .bodyValue(chatRequest)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        List<Choice> lst = chatResponse.getChoices();
        Usage usage = chatResponse.getUsage();

        Map<String, Object> map = new HashMap<>();
        map.put("Usage", usage);
        map.put("Choices", lst);

        return map;
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
