package stepdefs;

import base.BaseAPI;
import io.cucumber.java.PendingException;
import net.datafaker.Faker;
import pojo.*;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class BankSteps {
    private final Faker faker = new Faker();
    private Response response;
    private CustomerRequest customerRequest;
    private AccountRequest accReq;
    private static int createdCustomerID;
    private int cNum = Integer.parseInt(faker.number().digits(5));
    private int aNum= faker.number().numberBetween(10000, 99999);
    private int fAccNum;
    private int tAccNum;
    private double initialFromAccBal;
    private double initialToAccBal;
    private double transferAmount;
    TransferRequest transfer;


    @Given("I have valid customer details")
    public void generateCustomerPayload() {
        CustomerAddress addr = new CustomerAddress(
                faker.address().streetAddress(), "",
                faker.address().city(), faker.address().state(),
                faker.address().zipCode(), "USA"
        );

        ContactDetails contact = new ContactDetails(
                faker.internet().emailAddress(),
                faker.phoneNumber().cellPhone(),
                faker.phoneNumber().phoneNumber()
        );

        customerRequest = new CustomerRequest(
                faker.name().firstName(), faker.name().lastName(), faker.name().firstName(),
                cNum, "ACTIVE", addr, contact
        );
    }

    @When("I submit a POST request to {string}")
    public void submitPostRequest(String endpoint) {
        response = given().spec(BaseAPI.getRequestSpec()).body(customerRequest).post(endpoint);

    }

    @Then("the response status code should be {int}")
    public void validateResponseStatusCode(int statusCode) {
        assertEquals(response.statusCode(), statusCode);
    }

    @And("validate the customer details with endpoint {string} and customerNumber")
    public void validateCustomerDetailsWithEndpoint(String endpoint) {
        response = given().spec(BaseAPI.getRequestSpec()).pathParam("customerNumber", cNum).when().get(endpoint + "/{customerNumber}");
        createdCustomerID=response.jsonPath().getInt("customerNumber");
    }

    @And("the response code should be {int} and response customerNumber should match the request")
    public void matchCustomerNumberInResponse(int statusCode) {
        assertEquals(response.statusCode(), statusCode);
        assertEquals(response.jsonPath().getInt("customerNumber"), cNum, "The Customer Number did not match,Customer not created");
    }

    @And("create account information with {string} and {double}")
    public void createAccountInformationWithTypeAndBalance(String type,double balance) {
        BranchAddress bAddr = new BranchAddress(
                faker.address().streetAddress(), "",
                faker.address().city(), faker.address().state(),
                faker.address().zipCode(), "USA"
        );

        BankInformation bInfo = new BankInformation(
                "Main St Branch", 101, bAddr, 998877
        );

        // 2. Build the final AccountRequest
        accReq = new AccountRequest(aNum
               ,
                bInfo,
                "ACTIVE",
                type,
                balance,
                java.time.Instant.now().toString() // Generates the ISO timestamp
        );

    }

    @And("add the account to the customer with endpoint {string}")
    public void addTheAccountToTheCustomerWithEndpoint(String endpoint) {
        response = given().spec(BaseAPI.getRequestSpec()).pathParam("customerNumber",createdCustomerID)
                .body(accReq).log().all()
                .post(endpoint+"/{customerNumber}");
        assertEquals(response.asString(),"Account created successfully","Validation message for account creation API failed");

    }


    @And("Validate the account details for created account")
    public void validateTheAccountDetailsForCreatedAccount() {
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("accountNumber",aNum).get("/accounts/{accountNumber}");
        assertEquals(response.jsonPath().getInt("accountNumber"),aNum,"Account number did not match,Account not created");
        assertEquals(response.asString(),"Account found successfully","Validation message for get account details failed");
    }

    @Given("{int} and {int} details")
    public void getAccountNumDetailsForTransfer(int fANum,int tANum) {
       fAccNum= fANum;
       tAccNum=tANum;

    }

    @And("fetch the initial account balances for the given accounts")
    public void fetchTheInitialAccountBalancesForTheGivenAccounts() {
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("accountNumber",fAccNum).get("/accounts/{accountNumber}");
        initialFromAccBal=response.jsonPath().getDouble("accountBalance");
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("accountNumber",tAccNum).get("/accounts/{accountNumber}");
        initialToAccBal=response.jsonPath().getDouble("accountBalance");

    }

    @And("Initiate funds transfer request for {double} with {int} to endpoint {string}")
    public void fundsTransferRequest(double amount,int custNum,String endpoint) {
        transferAmount=amount;
        transfer=new TransferRequest(fAccNum,tAccNum,amount);
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("customerNumber",custNum).body(transfer).log().all().put(endpoint+"/{customerNumber}");
        assertEquals(response.asString(),"Success: Amount transferred for Customer Number "+custNum,"Success message not validated");
    }

    @And("validate the final balances in both accounts")
    public void validateTheFinalBalancesInBothAccounts() {
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("accountNumber",fAccNum).get("/accounts/{accountNumber}");
        assertEquals(response.jsonPath().getDouble("accountBalance"),initialFromAccBal-transferAmount,"From Account balance not updated");
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("accountNumber",tAccNum).get("/accounts/{accountNumber}");
        assertEquals(response.jsonPath().getDouble("accountBalance"),initialToAccBal+transferAmount,"To Account balance not updated");

    }

    @Given("{int} for fetching transactions")
    public void accountNumberForFetchingTransactions(int accNum) {

    }
}

