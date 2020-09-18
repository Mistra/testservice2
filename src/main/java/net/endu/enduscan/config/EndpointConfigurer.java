package net.endu.enduscan.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "endpoint-config")
class EndpointConfiguration {
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
}

@Configuration
public class EndpointConfigurer implements WebMvcConfigurer {
    @Autowired
    EndpointConfiguration endpointConfiguration;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // registry.addMapping("/**")
        //         .allowedOrigins(endpointConfiguration.getAllowedOrigins().stream().toArray(String[]::new))
        //         .allowedMethods(endpointConfiguration.getAllowedMethods().stream().toArray(String[]::new));
    }
}
