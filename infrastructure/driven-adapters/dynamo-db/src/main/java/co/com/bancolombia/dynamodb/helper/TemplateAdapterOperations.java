package co.com.bancolombia.dynamodb.helper;

import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

public abstract class TemplateAdapterOperations<E, V> {
    private final Class<V> dataClass;
    private final Function<V, E> toEntityFn;
    protected ObjectMapper mapper;
    protected DynamoDbAsyncTable<V> dynamoDbAsyncTable;

    protected TemplateAdapterOperations(DynamoDbAsyncTable<V> dynamoDbAsyncTable, ObjectMapper mapper, Function<V, E> toEntityFn) {
        this.toEntityFn = toEntityFn;
        this.mapper = mapper;
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.dataClass = (Class<V>) genericSuperclass.getActualTypeArguments()[1];
        this.dynamoDbAsyncTable = dynamoDbAsyncTable;
    }

    protected V toEntity(E model) {
        return mapper.map(model, dataClass);
    }

    protected E toModel(V data) {
        return data != null ? toEntityFn.apply(data) : null;
    }

    protected Mono<Void> saveEntity(V entity) {
        return Mono.fromFuture(dynamoDbAsyncTable.updateItem(entity)).then(Mono.empty());
    }
}