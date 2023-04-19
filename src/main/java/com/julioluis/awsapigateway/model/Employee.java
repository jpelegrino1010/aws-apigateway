package com.julioluis.awsapigateway.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

@DynamoDBTable(tableName = "employee")
@Data
public class Employee {

    @DynamoDBHashKey(attributeName = "empId")
    private String empId;
    @DynamoDBHashKey(attributeName = "name")
    private String name;
    @DynamoDBHashKey(attributeName = "email")
    private String email;
}
