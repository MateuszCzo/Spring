package mc.recruitment_task.epidemic_simulation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${cors.allowed.origin}")
    private String origin;
    @Value("${cors.allowed.methods}")
    private String methods;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins(origin.split(","))
                .allowedMethods(methods.split(","))
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
