package co.com.bancolombia.mq.listener.statistics;

import co.com.bancolombia.model.customerstatistics.CustomerStatistics;
import co.com.bancolombia.mq.listener.helper.AbstractValidationListenerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class StatisticsListenerHandler
        extends AbstractValidationListenerHandler<CustomerStatistics, CustomerStatistics, Validator> {
    private final Logger logger = Logger.getLogger(StatisticsListenerHandler.class.getName());

    public StatisticsListenerHandler(@Autowired Validator validator) {
        super(CustomerStatistics.class, validator);
    }

    @Override
    protected Mono<CustomerStatistics> processQuery(CustomerStatistics r) {
        logger.log(Level.WARNING, "\n\n ===== Processing customer statistics in MQ listener: MD5-Hash={0} ===== \n", r.getHash());
        return Mono.just(r);
    }

    @Override
    protected Mono<CustomerStatistics> exceptionProcessBody(CustomerStatistics customerStatistics, Exception err) {
        return Mono.just(customerStatistics);
    }
}
