package com.okta.poc.iot.tisensor.apidynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CreateTables {

    private AmazonDynamoDB amazonDynamoDB;

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTables.class);

    public CreateTables(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }

    public void createUserModelTable(){

        List<KeySchemaElement> elements = new ArrayList<>();
        KeySchemaElement hashKey = new KeySchemaElement()
                .withKeyType(KeyType.RANGE)
                .withAttributeName("id")
                .withKeyType("id");

        elements.add(hashKey);

        List<LocalSecondaryIndex> localSecondaryIndices = new ArrayList<>();

        ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<>();

        indexKeySchema.add(new KeySchemaElement()
                .withAttributeName("id")
                .withKeyType(KeyType.RANGE));  //Partition key
        indexKeySchema.add(new KeySchemaElement()
                .withAttributeName("deviceID")
                .withKeyType(KeyType.RANGE));  //Sort key


        LocalSecondaryIndex factoryIndex = new LocalSecondaryIndex()
                .withIndexName("userDeviceIndex")
                .withKeySchema(indexKeySchema)
                .withProjection(new Projection().withProjectionType(ProjectionType.ALL));
        localSecondaryIndices.add(factoryIndex);

        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();

        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("id")
                .withAttributeType(ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("userID")
                .withAttributeType(ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("deviceID")
                .withAttributeType(ScalarAttributeType.S));

        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName("UserModel")
                .withKeySchema(elements)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(5L)
                        .withWriteCapacityUnits(5L))
                .withLocalSecondaryIndexes(localSecondaryIndices)
                .withAttributeDefinitions(attributeDefinitions);

        amazonDynamoDB.createTable(createTableRequest);
    }

}
