package co.com.bancolombia.dynamodb.config;

import co.com.bancolombia.dynamodb.statistics.model.CustomerStatisticsData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class DynamoDBConfig {

    @Value("${aws.dynamodb.accessKey}")
    private String accessKeyID;
    @Value("${aws.dynamodb.secretAccessKey}")
    private String secretAccessKey;
    @Value("${aws.dynamodb.endpoint}")
    private String endpoint;
    @Value("${aws.region}")
    private String region;
    @Value("${aws.dynamodb.table-name}")
    private String tableName;

    @Bean
    public DynamoDbAsyncClient amazonDynamoDB() {
        var basicCredentials = AwsBasicCredentials.create(this.accessKeyID, this.secretAccessKey);
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(basicCredentials))
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient(DynamoDbAsyncClient client) {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(client)
                .build();
    }

    @Bean
    DynamoDbAsyncTable<CustomerStatisticsData> dbFvtErrorTextTable(DynamoDbEnhancedAsyncClient dbEnhancedClient) {
        return dbEnhancedClient.table(tableName, TableSchema.fromBean(CustomerStatisticsData.class));
    }
}
