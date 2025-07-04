package co.com.bancolombia.api.statistics;

import co.com.bancolombia.api.helper.AbstractValidationHandler;
import co.com.bancolombia.api.model.request.CustomerStatisticsRequest;
import co.com.bancolombia.api.model.request.CustomerStatisticsRequestData;
import co.com.bancolombia.api.model.response.CustomerStatisticsResponse;
import co.com.bancolombia.api.model.response.CustomerStatisticsResponseData;
import co.com.bancolombia.model.customerstatistics.CustomerStatistics;
import co.com.bancolombia.model.error.ErrorCS;
import co.com.bancolombia.model.exception.CustomerStatisticsException;
import co.com.bancolombia.usecase.customerstatistics.CustomerStatisticsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class CustomerStatisticsHandler extends AbstractValidationHandler<CustomerStatisticsRequest, Validator> {

    private final Logger logger = Logger.getLogger(CustomerStatisticsHandler.class.getName());
    private final CustomerStatisticsUseCase customerStatisticsUseCase;

    protected CustomerStatisticsHandler(@Autowired Validator validator,
                                        CustomerStatisticsUseCase customerStatisticsUseCase) {
        super(CustomerStatisticsRequest.class, validator);
        this.customerStatisticsUseCase = customerStatisticsUseCase;
    }

    @Override
    protected Mono<ServerResponse> processBody(CustomerStatisticsRequest request, ServerRequest originalRequest) {
        logger.info("Processing customer statistics");
        return buildModel(request.getData())
                .flatMap(customerStatisticsUseCase::validateMD5Hash)
                .flatMap(statistics -> buildResponse())
                .doOnSuccess(o -> logger.info("Customer statistics model processing successfully"))
                .doOnError(e -> logger.severe("Error processing customer statistics: " + e.getMessage()))
                .onErrorResume(CustomerStatisticsException.class, e -> buildError(e.getCode(), e.getMessage()))
                .onErrorResume(Exception.class, e -> buildError("TCS-000", e.getMessage()));
    }

    @Override
    protected Mono<ServerResponse> exceptionProcessBody(CustomerStatisticsRequest body, Exception err) {
        logger.severe(err.getMessage());
        var errorData = ErrorCS.builder().title("Error").code("TCS-002").message(err.getMessage()).build();
        return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                Mono.just(CustomerStatisticsResponse.builder().error(errorData).build()),
                CustomerStatisticsResponse.class
        );
    }

    private Mono<CustomerStatistics> buildModel(CustomerStatisticsRequestData requestData) {
        return Mono.just(CustomerStatistics.builder()
                .totalCustomerContacts(requestData.getTotalCustomerContacts())
                .changeReason(requestData.getChangeReason())
                .claimReason(requestData.getClaimReason())
                .hash(requestData.getHash())
                .doubtReason(requestData.getDoubtReason())
                .congratulationsReason(requestData.getCongratulationsReason())
                .purchaseReason(requestData.getPurchaseReason())
                .warrantyReason(requestData.getWarrantyReason())
                .build());
    }

    private Mono<ServerResponse> buildResponse() {
        var responseData = CustomerStatisticsResponseData.builder()
                .code("200").message("Customer statistics processed successfully").build();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
                Mono.just(CustomerStatisticsResponse.builder().data(responseData).build()),
                CustomerStatisticsResponse.class
        );
    }

    private Mono<ServerResponse> buildError(String code, String msg) {
        var errorData = ErrorCS.builder().title("Error").code(code).message(msg).build();
        return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                Mono.just(CustomerStatisticsResponse.builder().error(errorData).build()),
                CustomerStatisticsResponse.class
        );
    }
}
