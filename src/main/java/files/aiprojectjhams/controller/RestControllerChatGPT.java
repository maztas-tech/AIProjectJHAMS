package files.aiprojectjhams.controller;


import files.aiprojectjhams.dto.chatgpt.*;
import files.aiprojectjhams.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
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



    private final WebClient webClient;
    //private final WebClient webClient2;

    private final String systemMode = """
            You are a weather reporter that looks for the forecast of cities across the world. 
            I would also like for you to give suggestions for outdoor activities and clothing based on weather
            """;
    @Autowired
    private ChatGPTService chatGPTService;

    public RestControllerChatGPT(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();

    }


    @GetMapping("/chat")
    public Map<String, Object> chat(@RequestParam String city) {
        ChatRequest chatRequest = new ChatRequest();

        chatRequest.setModel("gpt-4");
        List<Message> listMessages = new ArrayList<>();
        listMessages.add(new Message("system", systemMode));
        listMessages.add(new Message("user", chatGPTService.getWeather(city)));
        chatRequest.setMessages(listMessages);
        chatRequest.setN(3);
        chatRequest.setTemperature(1);
        chatRequest.setMaxTokens(500);
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








}
