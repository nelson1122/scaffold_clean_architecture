package co.com.bancolombia.mq.statistics;

import co.com.bancolombia.model.customerstatistics.CustomerStatistics;
import co.com.bancolombia.model.customerstatistics.gateways.CustomerStatisticsSenderRepository;
import co.com.bancolombia.model.exception.CustomerStatisticsException;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@Repository
@RequiredArgsConstructor
public class CustomerStatisticsSenderRepositoryAdapter implements CustomerStatisticsSenderRepository {
    private final Logger logger = Logger.getLogger(CustomerStatisticsSenderRepositoryAdapter.class.getName());
    private final DirectAsyncGateway directAsyncGateway;

    @Override
    public Mono<CustomerStatistics> send(CustomerStatistics customerStatistics) {
        logger.info("Customer statistics sender has started");
        AsyncQuery<CustomerStatistics> asyncQuery = new AsyncQuery<>("event.stats.validated", customerStatistics);

        return directAsyncGateway.requestReply(asyncQuery, "ms-customer-statistics", CustomerStatistics.class)
                .doOnSuccess(o ->
                        logger.info("Customer statistics sender has finished successfully"))
                .doOnError(err ->
                        logger.severe("Customer statistics sender: " + err.getMessage()))
                .onErrorResume(TimeoutException.class, err -> Mono.error(() ->
                        new CustomerStatisticsException("TCS-003", "TimeoutException: " + err.getMessage())))
                .switchIfEmpty(Mono.error(() ->
                        new CustomerStatisticsException("TCS-003", "Error: mq empty response")));
    }
}
