package com.lifebook.Lifebook.dynamodb;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.List;

@AllArgsConstructor
public class DynamoDBStore<T> {
    private final DynamoDbTable<T> dynamoDbTable;
    private final DynamoDbEnhancedClient enhancedClient;

    /**
     * Get the DynamoDbTable object within the wrapper class.
     *
     * @return DynamoDbTable.
     */
    @NonNull
    public DynamoDbTable<T> dynamoDbTable() {
        return dynamoDbTable;
    }

    /**
     * Puts an item in DynamoDB table.
     *
     * @param item item to put.
     */
    @NonNull
    public void put(final @NonNull T item) {
        try {
            dynamoDbTable.putItem(item);
        } catch (DynamoDbException err) {
            String errorMessage = String.format("Unable to put the record %s in the table %s",
                item, this.dynamoDbTable.tableName());
            System.out.println(errorMessage);

            throw new RuntimeException(errorMessage, err);
        }
    }

    /**
     * Updates an item in DynamoDB table.
     *
     * @param item item to update.
     */
    @NonNull
    public void update(final @NonNull T item) {
        try {
            dynamoDbTable.updateItem(item);
        } catch (DynamoDbException err) {
            String errorMessage = String.format("Unable to update the record %s in the table %s",
                item, this.dynamoDbTable.tableName());
            System.out.println(errorMessage);

            throw new RuntimeException(errorMessage, err);
        }
    }

    /**
     * Gets an item in DynamoDB table.
     *
     * @param partitionKey partition key of item to get.
     * @return item.
     */
    @NonNull
    public T get(final @NonNull String partitionKey, final String sortKey) {
        try {
            Key key = buildKey(partitionKey, sortKey);
            return dynamoDbTable.getItem(key);
        } catch (DynamoDbException err) {
            String errorMessage = String.format("Unable to get the record from table %s for key %s with error %s",
                this.dynamoDbTable.tableName(), partitionKey, err);
            System.out.println(errorMessage);

            throw new RuntimeException(errorMessage, err);
        }
    }

    /**
     * Deletes an item in DynamoDB table.
     *
     * @param item partition key of item to delete.
     * @return deleted item.
     */
    @NonNull
    public T delete(final @NonNull T item) {
        try {
            return dynamoDbTable.deleteItem(item);
        } catch (DynamoDbException err) {
            String errorMessage = String.format("Unable to delete the record %s in the table %s",
                item, this.dynamoDbTable.tableName());
            System.out.println(errorMessage);

            throw new RuntimeException(errorMessage, err);
        }
    }

    /**
     * Query DynamoDB Table Index and return results.
     *
     * @param indexName name of index to query.
     * @param request   a {@link QueryEnhancedRequest} instance
     * @return list of items
     */
    @NonNull
    public List<T> queryIndex(final @NonNull String indexName, final @NonNull QueryEnhancedRequest request) {
        try {
            System.out.println("Querying the index " + indexName);
            return dynamoDbTable
                .index(indexName)
                .query(request)
                .iterator()
                .next()
                .items();
        } catch (DynamoDbException e) {
            String errorMessage = String.format("Unable to query index %s from table %s with request %s",
                indexName, this.dynamoDbTable.tableName(), request);
            System.out.println(errorMessage);

            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * Construct Key which helps us to query DynamoDB Table.
     *
     * @param partitionValue of the table.
     * @param sortValue of the table.
     * @return key
     */
    @NonNull
    public Key buildKey(@NonNull final String partitionValue, final String sortValue) {
        if (sortValue == null) {
            return Key
                .builder()
                .partitionValue(partitionValue)
                .build();
        }

        return Key.builder()
            .partitionValue(partitionValue)
            .sortValue(sortValue)
            .build();
    }

    /**
     * Construct QueryConditional which helps us to query DynamoDB Table.
     *
     * @param key of the table
     * @return QueryConditional.
     */
    @NonNull
    public QueryConditional buildQueryConditional(final @NonNull Key key) {
        return QueryConditional.keyEqualTo(key);
    }

    /**
     * Construct QueryEnhancedRequest to query DynamoDB Table.
     *
     * @param partitionValue of the table.
     * @param sortValue of the table.
     * @return QueryEnhancedRequest.
     */
    @NonNull
    public QueryEnhancedRequest buildQueryEnhancedRequest(final @NonNull String partitionValue,
                                                          final String sortValue) {
        Key key = buildKey(partitionValue, sortValue);
        QueryConditional queryConditional = buildQueryConditional(key);

        return QueryEnhancedRequest
            .builder()
            .queryConditional(queryConditional)
            .build();
    }

    @NonNull
    public List<T> saveBatch(@NonNull final List<T> list, @NonNull final Class<T> itemClass) {
        try {
            WriteBatch.Builder<T> batchWriter = WriteBatch
                .builder(itemClass)
                .mappedTableResource(dynamoDbTable);

            list.forEach(batchWriter::addPutItem);
            BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(batchWriter.build()).build();

            BatchWriteResult ret =
                enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
            return ret.unprocessedPutItemsForTable(dynamoDbTable);
        }catch(DynamoDbException exception){
            String errorMessage = String.format("Cannot perform batch write operation on following batch: %s", list);
            throw new RuntimeException(errorMessage, exception);
        }
    }
}

