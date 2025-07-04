package co.com.bancolombia.api.helper;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public abstract class AbstractValidationHandler<T, U extends Validator> {

    private final Class<T> validationClass;

    private final U validator;

    protected AbstractValidationHandler(Class<T> clazz, U validator) {
        this.validationClass = clazz;
        this.validator = validator;
    }

    public final Mono<ServerResponse> handleRequest(final ServerRequest request) {
        return request.bodyToMono(this.validationClass)
                .flatMap(b -> this.verifyRequest(b, request)
                        .onErrorResume(Exception.class, err -> exceptionProcessBody(b, err))
                )
                .switchIfEmpty(Mono.error(new ServerWebInputException("Request body cannot be empty.")))
                .onErrorResume(ServerWebInputException.class, err -> exceptionProcessBody(null, err));

    }

    protected abstract Mono<ServerResponse> processBody(T validBody, ServerRequest originalRequest);

    protected abstract Mono<ServerResponse> exceptionProcessBody(T body, Exception err);

    private Mono<ServerResponse> verifyRequest(T body, ServerRequest request) {
        var errors = new BeanPropertyBindingResult(body, this.validationClass.getName());

        this.validator.validate(body, errors);

        if (errors.getAllErrors().isEmpty()) {
            return processBody(body, request).subscribeOn(Schedulers.newSingle("db"));
        } else {
            return Mono.error(() -> new Exception(errors.getAllErrors().toString()));
        }
    }
}
