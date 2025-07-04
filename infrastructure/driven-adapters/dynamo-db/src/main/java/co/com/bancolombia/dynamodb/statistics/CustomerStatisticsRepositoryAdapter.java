package co.com.bancolombia.dynamodb.statistics;

import co.com.bancolombia.dynamodb.helper.TemplateAdapterOperations;
import co.com.bancolombia.dynamodb.statistics.model.CustomerStatisticsData;
import co.com.bancolombia.model.customerstatistics.CustomerStatistics;
import co.com.bancolombia.model.customerstatistics.gateways.CustomerStatisticsSaverRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;

import java.util.UUID;
import java.util.logging.Logger;

@Repository
public class CustomerStatisticsRepositoryAdapter
        extends TemplateAdapterOperations<CustomerStatistics, CustomerStatisticsData>
        implements CustomerStatisticsSaverRepository {

    private final Logger logger = Logger.getLogger(CustomerStatisticsRepositoryAdapter.class.getName());

    public CustomerStatisticsRepositoryAdapter(
            DynamoDbAsyncTable<CustomerStatisticsData> dynamoDbAsyncTable, ObjectMapper mapper) {
        super(dynamoDbAsyncTable, mapper, data -> mapper.map(data, CustomerStatistics.class));
    }

    @Override
    public Mono<CustomerStatistics> save(CustomerStatistics customerStatistics) {
        var data = toEntity(customerStatistics);
        data.setId(UUID.randomUUID().toString());
        logger.info("Saving customer statistics on dynamo table with Hash: " + data.getHash());
        return saveEntity(data)
                .then(Mono.just(customerStatistics))
                .doOnSuccess(s -> logger.info("Success customer statistics saved on dynamo table. ID: " + data.getId()))
                .doOnError(err -> logger.severe("Error customer statistics saved on dynamo table"));

    }
}
