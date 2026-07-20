package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseAPI {
    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("https://octopus-app-a5vye.ondigitalocean.app/bank-api")
                .setContentType(ContentType.JSON)
                .build();
    }
}
