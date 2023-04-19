package com.julioluis.awsapigateway;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        EmployeeService service=new EmployeeService();

        switch (apiGatewayRequest.getHttpMethod()) {
            case "POST":
                //save employee
               return service.saveEmployee(apiGatewayRequest,context);
            case "GET":
                if(apiGatewayRequest.getPathParameters()!=null) {
                    // fetch employee by id
                    return service.getEmployeeById(apiGatewayRequest,context);
                }
                // fetch all employees
                return service.getEmployees(apiGatewayRequest,context);
            case "DELETE":
                if(apiGatewayRequest.getPathParameters()!=null) {
                    // delete employee by id
                    return service.deleteEmployeeById(apiGatewayRequest,context);
                }
            default:
                // throw some error
                throw new Error("Unsupported methods "+ apiGatewayRequest.getHttpMethod());
        }

    }
}
