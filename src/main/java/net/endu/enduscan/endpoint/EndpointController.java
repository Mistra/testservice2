package net.endu.enduscan.endpoint;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/surface")
public class EndpointController {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofSeconds(2000)).setReadTimeout(Duration.ofSeconds(2000)).build();
    }

    @GetMapping()
    public String operatorIsAllowed(RestTemplate restTemplate) {
        String str = null;
        try {
            str = restTemplate.getForObject("http://localhost:8762/backend", String.class);
        } catch (RestClientException e) {
            str = "Cannot fetch service (changed!)";
        }
        str = str  + " - test!!!";
        return str;
    }

}
