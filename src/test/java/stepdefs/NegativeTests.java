package stepdefs;

import base.BaseAPI;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import pojo.*;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class NegativeTests {

    private Response response;
    private String payload;
    private long aNum;
    private Scenario scenario;


    @Before
    public void setup(Scenario scenario) {
        this.scenario = scenario;
    }
    @Given("{string} to add customer API")
    public void invalidcustomernumberToAddCustomerAPI(String cnum) {

        if(cnum.equals("Null")) {
            payload = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\",\n" +
                "  \"middleName\": \"D\",\n" +
                "  \"customerNumber\":,\n" +
                "  \"status\": \"ACTIVE\",\n" +
                "  \"customerAddress\": {\n" +
                "    \"address1\": \"Street 1\",\n" +
                "    \"address2\": \"Apartment 1\",\n" +
                "    \"city\": \"Nicosia\",\n" +
                "    \"state\": \"Nicosia\",\n" +
                "    \"zip\": \"1010\",\n" +
                "    \"country\": \"Cyprus\"\n" +
                "  },\n" +
                "  \"contactDetails\": {\n" +
                "    \"emailId\": \"172522328@mail.com\",\n" +
                "    \"homePhone\": \"22334455\",\n" +
                "    \"workPhone\": \"99887766\"\n" +
                "  }\n" +
                "}";
        } else
            payload = "{\n" +
                    "  \"firstName\": \"John\",\n" +
                    "  \"lastName\": \"Doe\",\n" +
                    "  \"middleName\": \"D\",\n" +
                    "  \"customerNumber\":"+ cnum +",\n" +
                    "  \"status\": \"ACTIVE\",\n" +
                    "  \"customerAddress\": {\n" +
                    "    \"address1\": \"Street 1\",\n" +
                    "    \"address2\": \"Apartment 1\",\n" +
                    "    \"city\": \"Nicosia\",\n" +
                    "    \"state\": \"Nicosia\",\n" +
                    "    \"zip\": \"1010\",\n" +
                    "    \"country\": \"Cyprus\"\n" +
                    "  },\n" +
                    "  \"contactDetails\": {\n" +
                    "    \"emailId\": \"172522328@mail.com\",\n" +
                    "    \"homePhone\": \"22334455\",\n" +
                    "    \"workPhone\": \"99887766\"\n" +
                    "  }\n" +
                    "}";
        scenario.log("<b>Request Payload:</b><br><pre>" + payload + "</pre>");
    }

    @When("I submit a POST request to endpoint {string}")
    public void iSubmitAPOSTRequestToEndpoint(String endpoint) {
        response = given().spec(BaseAPI.getRequestSpec()).body(payload).post(endpoint);
    }

    @Then("validate response status code should be {int}")
    public void validateResponseStatusCodeShouldBe(int code) {
        scenario.log("<span style='color:orange'> Validation of Response status code.</span>");
        assertEquals(response.statusCode(), code);
    }

    @Given("{string} to add Account API")
    public void invalidaccountnumberToAddAccountAPI(String aNum) {
        if(aNum.equals("Null")) {
            payload="  \"accountNumber\":,\n" +
                    "  \"bankInformation\": {\n" +
                    "    \"branchName\": \"Main Branch\",\n" +
                    "    \"branchCode\": 101,\n" +
                    "    \"branchAddress\": {\n" +
                    "      \"address1\": \"123 Financial St\",\n" +
                    "      \"address2\": \"Suite 100\",\n" +
                    "      \"city\": \"Nicosia\",\n" +
                    "      \"state\": " + "\"Nicosia\",\n" +
                    "      \"zip\": \"1010\",\n" +
                    "      \"country\": \"Cyprus\"\n" +
                    "    },\n" +
                    "    \"routingNumber\": 987654321\n" +
                    "  },\n" +
                    "  \"accountStatus\": \"ACTIVE\",\n" +
                    "  \"accountType\": \"SAVINGS\",\n" +
                    "  \"accountBalance\": 120,\n" +
                    "  \"accountCreated\": \"2026-07-21T07:35:32.242Z\"\n" +
                    "}";
        } else
            payload="  \"accountNumber\":" + aNum +",\n" +
                    "  \"bankInformation\": {\n" +
                    "    \"branchName\": \"Main Branch\",\n" +
                    "    \"branchCode\": 101,\n" +
                    "    \"branchAddress\": {\n" +
                    "      \"address1\": \"123 Financial St\",\n" +
                    "      \"address2\": \"Suite 100\",\n" +
                    "      \"city\": \"Nicosia\",\n" +
                    "      \"state\": " + "\"Nicosia\",\n" +
                    "      \"zip\": \"1010\",\n" +
                    "      \"country\": \"Cyprus\"\n" +
                    "    },\n" +
                    "    \"routingNumber\": 987654321\n" +
                    "  },\n" +
                    "  \"accountStatus\": \"ACTIVE\",\n" +
                    "  \"accountType\": \"SAVINGS\",\n" +
                    "  \"accountBalance\": 120,\n" +
                    "  \"accountCreated\": \"2026-07-21T07:35:32.242Z\"\n" +
                    "}";
        scenario.log("<b>Request Payload:</b><br><pre>" + payload + "</pre>");
    }

    @When("I submit a POST request to endpoint {string} with {long}")
    public void iSubmitAPOSTRequestToEndpointWithCustomerNumber(String endpoint,long cNum) {
        response = given().spec(BaseAPI.getRequestSpec()).pathParam("customerNumber",cNum)
                .body(payload)
                .post(endpoint+"/{customerNumber}");
        scenario.log("<span style='color:orange'> Post request submitted for add account.</span>");
    }

    @Then("validate the response message {string} for negative tests")
    public void validateTheResponseMessageResponseMessageForNegativeTests(String message) {
        scenario.log("<span style='color:orange'>Validation of Response message</span>");
        String type=response.getHeader("content-type");
        scenario.log("<b>Response Payload:</b><br><pre>" + response.asString() + "</pre>");
        if(type.contains("json"))
            assertEquals(response.jsonPath().getString("message"),message);
        else
            assertEquals(response.asString(),message);
        scenario.log("<b>Response Payload:</b><br><pre>" + response.asString() + "</pre>");
    }

    @Given("existing {long}")
    public void existingCustomerNumber(long cnum) {
        payload = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\",\n" +
                "  \"middleName\": \"D\",\n" +
                "  \"customerNumber\":"+ cnum +",\n" +
                "  \"status\": \"ACTIVE\",\n" +
                "  \"customerAddress\": {\n" +
                "    \"address1\": \"Street 1\",\n" +
                "    \"address2\": \"Apartment 1\",\n" +
                "    \"city\": \"Nicosia\",\n" +
                "    \"state\": \"Nicosia\",\n" +
                "    \"zip\": \"1010\",\n" +
                "    \"country\": \"Cyprus\"\n" +
                "  },\n" +
                "  \"contactDetails\": {\n" +
                "    \"emailId\": \"172522328@mail.com\",\n" +
                "    \"homePhone\": \"22334455\",\n" +
                "    \"workPhone\": \"99887766\"\n" +
                "  }\n" +
                "}";
        scenario.log("<b>Request Payload:</b><br><pre>" + payload + "</pre>");
    }

    @Given("existing account {long}")
    public void existingAccountNumberAndCustomerNumber(long accNum) {
aNum=accNum;
        payload=" {\n" + "\"accountNumber\":" + accNum +",\n" +
                "  \"bankInformation\": {\n" +
                "    \"branchName\": \"Main Branch\",\n" +
                "    \"branchCode\": 101,\n" +
                "    \"branchAddress\": {\n" +
                "      \"address1\": \"123 Financial St\",\n" +
                "      \"address2\": \"Suite 100\",\n" +
                "      \"city\": \"Nicosia\",\n" +
                "      \"state\": " + "\"Nicosia\",\n" +
                "      \"zip\": \"1010\",\n" +
                "      \"country\": \"Cyprus\"\n" +
                "    },\n" +
                "    \"routingNumber\": 987654321\n" +
                "  },\n" +
                "  \"accountStatus\": \"ACTIVE\",\n" +
                "  \"accountType\": \"SAVINGS\",\n" +
                "  \"accountBalance\": 120,\n" +
                "  \"accountCreated\": \"2026-07-21T07:35:32.242Z\"\n" +
                "}";
        scenario.log("<b>Request Payload:</b><br><pre>" + payload + "</pre>");
    }

    @When("a get request is submitted to endpoint {string}")
    public void aGetRequestIsSubmittedToEndpoint(String endpoint) {
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("accountNumber",aNum).get(endpoint+"/{accountNumber}");
        scenario.log("<span style='color:orange'>Get account details request submitted</span>");
        scenario.log("<b>Response Payload:</b><br><pre>" + response.asString() + "</pre>");
    }

    @Given("update request payload is created")
    public void updateRequestPayloadIsCreated() {
        payload = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\",\n" +
                "  \"middleName\": \"D\",\n" +
                "  \"customerNumber\":"+ 1234 +",\n" +
                "  \"status\": \"ACTIVE\",\n" +
                "  \"customerAddress\": {\n" +
                "    \"address1\": \"Street 1\",\n" +
                "    \"address2\": \"Apartment 1\",\n" +
                "    \"city\": \"Nicosia\",\n" +
                "    \"state\": \"Nicosia\",\n" +
                "    \"zip\": \"1010\",\n" +
                "    \"country\": \"Cyprus\"\n" +
                "  },\n" +
                "  \"contactDetails\": {\n" +
                "    \"emailId\": \"172522328@mail.com\",\n" +
                "    \"homePhone\": \"22334455\",\n" +
                "    \"workPhone\": \"99887766\"\n" +
                "  }\n" +
                "}";
        scenario.log("<b>Request Payload:</b><br><pre>" + payload + "</pre>");

    }

    @When("submit a put request to endpoint {string} with {long}")
    public void submitAPutRequestToEndpointWithNonExistingCustomer(String endpoint,long cnum) {
        response = given().spec(BaseAPI.getRequestSpec()).pathParam("customerNumber",cnum)
                .body(payload).log().all()
                .put(endpoint+"/{customerNumber}");
        scenario.log("<span style='color:orange'> Update request submitted for Customer.</span>");
    }

    @Given("submit a delete request to endpoint {string} with {long}")
    public void submitADeleteRequestToEndpointWithNonExistingCustomer(String endpoint,long cnum) {
        scenario.log("<span style='color:orange'> Delete customer request submitted for Customer.</span>");
        response = given().spec(BaseAPI.getRequestSpec()).pathParam("customerNumber",cnum)
                   .delete(endpoint+"/{customerNumber}");

    }
}
