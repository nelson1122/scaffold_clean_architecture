package co.com.bancolombia.model.customerstatistics.gateways;

import co.com.bancolombia.model.customerstatistics.CustomerStatistics;
import reactor.core.publisher.Mono;

public interface CustomerStatisticsSenderRepository {
    Mono<CustomerStatistics> send(CustomerStatistics customerStatistics);
}
