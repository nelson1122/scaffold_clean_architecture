package co.com.bancolombia.dynamodb.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.concurrent.ExecutionException;

@Configuration
public class DynamoDBInitializer {
    @Value("${aws.dynamodb.table-name}")
    private String tableName;

    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    public DynamoDBInitializer(DynamoDbAsyncClient dynamoDbAsyncClient) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
    }

    @PostConstruct
    public void createTableIfNotExists() throws ExecutionException, InterruptedException {
        ListTablesResponse tables = dynamoDbAsyncClient.listTables().get();
        if (!tables.tableNames().contains(tableName)) {
            CreateTableRequest request = CreateTableRequest.builder()
                    .tableName(tableName)
                    .keySchema(KeySchemaElement.builder()
                            .attributeName("id")
                            .keyType(KeyType.HASH)
                            .build())
                    .attributeDefinitions(AttributeDefinition.builder()
                            .attributeName("id")
                            .attributeType(ScalarAttributeType.S)
                            .build())
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build();
            dynamoDbAsyncClient.createTable(request).get();
        }
    }

}
