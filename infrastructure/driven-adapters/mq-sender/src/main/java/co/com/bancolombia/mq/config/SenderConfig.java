package co.com.bancolombia.mq.config;

import com.rabbitmq.client.ConnectionFactory;
import org.reactivecommons.async.rabbit.config.ConnectionFactoryProvider;
import org.reactivecommons.async.rabbit.config.RabbitProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SenderConfig {
    @Value("${aws.rabbit.host}")
    private String host;

    @Value("${aws.rabbit.port}")
    private int port;

    @Value("${aws.rabbit.username}")
    private String username;

    @Value("${aws.rabbit.password}")
    private String password;

    @Bean
    @Primary
    public RabbitProperties rabbitProperties() {
        var rabbitProperties = new RabbitProperties();
        rabbitProperties.setHost(host);
        rabbitProperties.setPort(port);
        rabbitProperties.setUsername(username);
        rabbitProperties.setPassword(password);
        return rabbitProperties;
    }

    @Bean
    public ConnectionFactoryProvider connectionTest(RabbitProperties rabbitProperties) {
        var connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        return () -> connectionFactory;
    }
}
