package com.julioluis.awsapigateway;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.julioluis.awsapigateway.Utility;
import com.julioluis.awsapigateway.model.Employee;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EmployeeService {

    private DynamoDBMapper dynamoDBMapper;
    private String jsonBody;

    public void initDynamoDB(){
        AmazonDynamoDB client= AmazonDynamoDBClientBuilder.standard().build();
        dynamoDBMapper=new DynamoDBMapper(client);
    }

    public APIGatewayProxyResponseEvent saveEmployee(APIGatewayProxyRequestEvent request, Context context) {
        initDynamoDB();

        Employee employee= Utility.convertStringToObj(request.getBody(),context);
        dynamoDBMapper.save(employee);
        jsonBody=Utility.convertObjToString(employee,context);
        context.getLogger().log("Data save successfully to dynamoDB::: "+ jsonBody);

        return createAPIResponse(jsonBody,201,Utility.createHeaders());
    }

    public APIGatewayProxyResponseEvent getEmployeeById(APIGatewayProxyRequestEvent request,Context context) {
        initDynamoDB();
        String empId=request.getPathParameters().get("empId");
        Employee employee=dynamoDBMapper.load(Employee.class,empId);

        if (Objects.nonNull(employee)) {
            jsonBody=Utility.convertObjToString(employee,context);
            context.getLogger().log("Fetch employee by Id:: "+ jsonBody);
            return createAPIResponse(jsonBody,200,Utility.createHeaders());
        }else {
            jsonBody="Employee not found exception "+ empId;
            return createAPIResponse(jsonBody,400,Utility.createHeaders());
        }
    }

    public APIGatewayProxyResponseEvent getEmployees(APIGatewayProxyRequestEvent request,Context context) {
        initDynamoDB();
        List<Employee> employees=dynamoDBMapper.scan(Employee.class,new DynamoDBScanExpression());
        jsonBody=Utility.convertListOfObjToString(employees,context);
        context.getLogger().log("Fetch all employee "+ jsonBody);
        return createAPIResponse(jsonBody,200,Utility.createHeaders());
    }

    public APIGatewayProxyResponseEvent deleteEmployeeById(APIGatewayProxyRequestEvent request,Context context) {
        initDynamoDB();

        String empId=request.getPathParameters().get("empId");
        Employee employee=dynamoDBMapper.load(Employee.class,empId);

        if (Objects.nonNull(employee)) {
            dynamoDBMapper.delete(employee);
            jsonBody="Data delete successful "+ empId;
            context.getLogger().log(jsonBody);
            return createAPIResponse(jsonBody,200,Utility.createHeaders());
        }else {
            jsonBody="Error deleting employee "+ empId;
            return createAPIResponse(jsonBody,400,Utility.createHeaders());
        }

    }

    public APIGatewayProxyResponseEvent createAPIResponse(String body, int statusCode, Map<String,String> headers) {
        APIGatewayProxyResponseEvent response=new APIGatewayProxyResponseEvent();
        response.setBody(body);
        response.setHeaders(headers);
        response.setStatusCode(statusCode);

        return response;
    }
}
