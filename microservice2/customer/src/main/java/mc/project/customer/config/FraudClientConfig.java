package mc.project.customer.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import mc.project.customer.decoder.FraudErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

public class FraudClientConfig {
    @Bean
    Logger.Level fraudLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public RequestInterceptor reqInterFraudClient() {
        return requestTemplate -> {
            requestTemplate.header(HttpHeaders.AUTHORIZATION + " example auth");
        };
    }
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FraudErrorDecoder();
    }
}
