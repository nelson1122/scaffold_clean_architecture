package co.com.bancolombia.api;

import co.com.bancolombia.api.statistics.CustomerStatisticsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    private static final String ENDPOINT = "/api/v1";

    @Bean
    public RouterFunction<ServerResponse> routerFunction(CustomerStatisticsHandler customerStatisticsHandler) {
        return route().path(ENDPOINT, builder -> builder
                        .GET("health", request -> ServerResponse.ok().bodyValue("OK"))
                        .POST("stats", customerStatisticsHandler::handleRequest))
                .build();
    }
}
