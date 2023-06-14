package jmaster.io.notificationservice.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@Configuration 
public class Config {
	@Bean
	JsonMessageConverter converter() {
		return new JsonMessageConverter();
	}
}
