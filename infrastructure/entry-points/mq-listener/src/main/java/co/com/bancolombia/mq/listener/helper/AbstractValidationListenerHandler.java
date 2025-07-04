package co.com.bancolombia.mq.listener.helper;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

public abstract class AbstractValidationListenerHandler<T, E, U extends Validator> {

    private final Class<T> validationClass;

    private final U validator;

    protected AbstractValidationListenerHandler(Class<T> clazz, U validator) {
        this.validationClass = clazz;
        this.validator = validator;
    }

    public final Mono<E> handleQuery(final T request) {
        return this.verifyQuery(request)
                .onErrorResume(Exception.class, e -> this.exceptionProcessBody(request, e));
    }

    protected abstract Mono<E> processQuery(T r);

    protected abstract Mono<E> exceptionProcessBody(T request, Exception err);

    private Mono<E> verifyQuery(T request) {
        var errors = new BeanPropertyBindingResult(request, this.validationClass.getName());
        this.validator.validate(request, errors);

        if (errors.getAllErrors().isEmpty()) {
            return processQuery(request);
        } else {
            return Mono.error(() -> new Exception(errors.getAllErrors().toString()));
        }
    }

}