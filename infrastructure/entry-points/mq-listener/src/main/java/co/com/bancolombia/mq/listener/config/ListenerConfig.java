package co.com.bancolombia.mq.listener.config;

import co.com.bancolombia.model.customerstatistics.CustomerStatistics;
import co.com.bancolombia.mq.listener.statistics.StatisticsListenerHandler;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ListenerConfig {
    private final StatisticsListenerHandler statisticsListenerHandler;

    @Bean
    public HandlerRegistry handlerRegistry() {
        return HandlerRegistry.register()
                .serveQuery("event.stats.validated", statisticsListenerHandler::handleQuery, CustomerStatistics.class);
    }
}
