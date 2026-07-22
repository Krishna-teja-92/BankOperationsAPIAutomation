package stepdefs;

import base.BaseAPI;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.Scenario;
import net.datafaker.Faker;
import pojo.*;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class BankSteps {
    private final Faker faker = new Faker();
    private Response response;
    private CustomerDetails customerRequest;
    private AccountRequest accReq;
    private static long createdCustomerID;
    private long cNum = Integer.parseInt(faker.number().digits(9));
    private long aNum= faker.number().numberBetween(10000, 999999999);
    private long fAccNum;
    private long tAccNum;
    private double initialFromAccBal;
    private double initialToAccBal;
    private double transferAmount;
    TransferRequest transfer;
    private long fetchAccNum;
    TransactionDetailsResponse[] transactions;
    List<TransactionDetailsResponse> txList;
    CustomerDetails[] custdetails;
    List<CustomerDetails> clist;
    private Scenario scenario;
    ObjectMapper mapper=new ObjectMapper();
    private long updateCNum;
    private long deleteCNum;


    @Before
    public void setup(Scenario scenario) {
        this.scenario = scenario;
    }


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

        customerRequest = new CustomerDetails(
                faker.name().firstName(), faker.name().lastName(), faker.name().firstName(),
                cNum, "ACTIVE", addr, contact
        );
        scenario.log("<span style='color:orange'> Customer request payload is created.</span>");
        String payload=mapper.writeValueAsString(customerRequest);
        scenario.log("<b>Request Payload:</b><br><pre>" + payload + "</pre>");

    }

    @When("I submit a POST request to {string}")
    public void submitPostRequest(String endpoint) {
        response = given().spec(BaseAPI.getRequestSpec()).body(customerRequest).post(endpoint);
        scenario.log("<span style='color:orange'> Post request submitted for add customer.</span>");
        scenario.log("<b>Response Payload:</b><br><pre>" + response.asString() + "</pre>");

    }

    @Then("the response status code should be {int}")
    public void validateResponseStatusCode(int statusCode) {
        scenario.log("<span style='color:orange'> Validation of Response status code.</span>");
        assertEquals(response.statusCode(), statusCode);
    }

    @And("validate the customer details with endpoint {string} and customerNumber")
    public void validateCustomerDetailsWithEndpoint(String endpoint) {
        response = given().spec(BaseAPI.getRequestSpec()).pathParam("customerNumber", cNum).when().get(endpoint + "/{customerNumber}");
        createdCustomerID=response.jsonPath().getInt("customerNumber");
        scenario.log("<span style='color:orange'> Validation of customer details</span>");
        scenario.log("<b>Response Payload:</b><br><pre>" + response.asString() + "</pre>");
    }

    @And("the response code should be {int} and response customerNumber should match the request")
    public void matchCustomerNumberInResponse(int statusCode) {
        scenario.log("<span style='color:orange'> Validation of response code and customer Number from Response</span>");
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
        scenario.log("<span style='color:orange'> Account request payload is created.</span>");
        String payload=mapper.writeValueAsString(accReq);
        scenario.log("<b>Request Payload:</b><br><pre>" + payload + "</pre>");

    }

    @And("add the account to the customer with endpoint {string}")
    public void addTheAccountToTheCustomerWithEndpoint(String endpoint) {
        response = given().spec(BaseAPI.getRequestSpec()).pathParam("customerNumber",createdCustomerID)
                .body(accReq).log().all()
                .post(endpoint+"/{customerNumber}");
        scenario.log("<span style='color:orange'> Post request submitted for add account.</span>");
        scenario.log("<b>Response Payload:</b><br><pre>" + response.asString() + "</pre>");
        //assertEquals(response.asString(),"Account created successfully","Validation message for account creation API failed");

    }


    @And("Validate the account details for created account")
    public void validateTheAccountDetailsForCreatedAccount() {
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("accountNumber",aNum).get("/accounts/{accountNumber}");
        scenario.log("<span style='color:orange'> Validation of account details</span>");
        scenario.log("<b>Response Payload:</b><br><pre>" + response.asString() + "</pre>");
        assertEquals(response.jsonPath().getInt("accountNumber"),aNum,"Account number did not match,Account not created");
        assertEquals(response.asString(),"Account found successfully","Validation message for get account details failed");
    }

    @Given("{int} and {int} details")
    public void getAccountNumDetailsForTransfer(long fANum,long tANum) {
       fAccNum= fANum;
       tAccNum=tANum;
       scenario.log("<span style='color:orange'> Capturing from and to account numbers for Funds Transfer</span>");

    }

    @And("fetch the initial account balances for the given accounts")
    public void fetchTheInitialAccountBalancesForTheGivenAccounts() {

        response=given().spec(BaseAPI.getRequestSpec()).pathParam("accountNumber",fAccNum).get("/accounts/{accountNumber}");
        initialFromAccBal=response.jsonPath().getDouble("accountBalance");
        scenario.log("<span style='color:orange'> Initial account balance for" +fAccNum+"is :"+initialFromAccBal+"</span>");
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("accountNumber",tAccNum).get("/accounts/{accountNumber}");
        initialToAccBal=response.jsonPath().getDouble("accountBalance");
        scenario.log("<span style='color:orange'> Initial account balance for" +tAccNum+"is :"+initialToAccBal+"</span>");


    }

    @And("Initiate funds transfer request for {double} with {long} to endpoint {string}")
    public void fundsTransferRequest(double amount,long custNum,String endpoint) {
        transferAmount=amount;
        transfer=new TransferRequest(fAccNum,tAccNum,amount);
        scenario.log("<span style='color:orange'> Transfer request payload is created.</span>");
        String payload=mapper.writeValueAsString(transfer);
        scenario.log("<b>Request Payload:</b><br><pre>" + payload + "</pre>");
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("customerNumber",custNum).body(transfer).log().all().put(endpoint+"/{customerNumber}");
        assertEquals(response.asString(),"Success: Amount transferred for Customer Number "+custNum,"Success message not validated");
        scenario.log("<span style='color:orange'>Funds Transfer completed</span>");
        scenario.log("<b>Response Payload:</b><br><pre>" + response.asString() + "</pre>");
    }

    @And("validate the final balances in both accounts")
    public void validateTheFinalBalancesInBothAccounts() {
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("accountNumber",fAccNum).get("/accounts/{accountNumber}");
        scenario.log("<span style='color:orange'>Final balance for account number :"+fAccNum+" is :"+response.jsonPath().getDouble("accountBalance")+"</span>");
        scenario.log("<b>Response Payload:</b><br><pre>" + response.asString() + "</pre>");
        assertEquals(response.jsonPath().getDouble("accountBalance"),initialFromAccBal-transferAmount,"From Account balance not updated");
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("accountNumber",tAccNum).get("/accounts/{accountNumber}");
        scenario.log("<span style='color:orange'>Final balance for account number :"+tAccNum+" is :"+response.jsonPath().getDouble("accountBalance")+"</span>");
        scenario.log("<b>Response Payload:</b><br><pre>" + response.asString() + "</pre>");
        assertEquals(response.jsonPath().getDouble("accountBalance"),initialToAccBal+transferAmount,"To Account balance not updated");

    }

    @Given("{long} for fetching transactions")
    public void accountNumberForFetchingTransactions(long accNum) {
        fetchAccNum=accNum;
        scenario.log("<span style='color:orange'>Capturing account number for fetching the transactions"+accNum+"</span>");

    }

    @When("submit a get request to endpoint {string}")
    public void submitAGetRequestToEndpoint(String endpoint) {
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("accountNumber",fetchAccNum).get(endpoint+"/{accountNumber}");
        scenario.log("<span style='color:orange'>Get all transactions request submitted</span>");
        scenario.log("<b>Response Payload:</b><br><pre>" + response.asString() + "</pre>");
        transactions=response.as(TransactionDetailsResponse[].class);
        txList= Arrays.asList(transactions);
    }

    @And("display the number of transactions and net amount for the account")
    public void displayTheNumberOfTransactionsAndNetAmountForTheAccount() {
        scenario.log("<span style='color:orange'>No. of transactions for account " + fetchAccNum +":"+ txList.size()+"</span>");
        double creditAmount=0.0;
        double debitAmount=0.0;
        double balance;
        for(TransactionDetailsResponse tx:txList)
        {
            if(tx.getTxType().equals("CREDIT"))
                creditAmount+= tx.getTxAmount();
            else
                debitAmount+=tx.getTxAmount();
        }
if(creditAmount>debitAmount) {
balance=creditAmount-debitAmount;
    scenario.log("<span style='color:orange'>The account has net credit for an amount:" +balance+"</span>");
}
else
{
    balance=debitAmount-creditAmount;
    scenario.log("<span style='color:orange'>The account has net debit for an amount " +balance+"</span>");
}
    }


    @Given("Get request is submitted to endpoint {string} for fetching all customers")
    public void getRequestIsSubmittedToEndpointForFetchingAllCustomers(String endpoint) {
        response=given().spec(BaseAPI.getRequestSpec()).get(endpoint);
        scenario.log("<span style='color:orange'>Get Request is submitted to fetch all customers</span>");


    }

    @And("print the number of customers and list of customer numbers")
    public void printTheNumberOfCustomersAndListOfCustomerNumbers() {
        custdetails=response.as(CustomerDetails[].class);
        clist=Arrays.asList(custdetails);
        int count=1;
        scenario.log("<b>Response Payload:</b><br><pre>" + response.asString() + "</pre>");
        scenario.log("<span style='color:orange'>No. of customers in the bank :"+ clist.size()+"</span>");
        for(CustomerDetails c:clist)
        {
            scenario.log("<span style='color:orange'>Customer "+count+" : "+ c.getCustomerNumber()+"</span>");
            count++;
        }

    }

    @Given("{long} for which update has to be done")
    public void customernumberForWhichUpdateHasToBeDone(long custNum) {
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

        customerRequest = new CustomerDetails(
                faker.name().firstName(), faker.name().lastName(), faker.name().firstName(),
                custNum, "ACTIVE", addr, contact
        );
        updateCNum=custNum;
        scenario.log("<span style='color:orange'> Customer request payload is created.</span>");
        String payload=mapper.writeValueAsString(customerRequest);
        scenario.log("<b>Request Payload:</b><br><pre>" + payload + "</pre>");

    }

    @When("submit put request to endpoint {string}")
    public void submitPutRequestToEndpoint(String endpoint) {
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("customerNumber",updateCNum).body(customerRequest).put(endpoint+"/{customerNumber}");
        scenario.log("<span style='color:orange'>Put Request is submitted to update customer</span>");

    }

    @Given("{long} for which deletion has to be done")
    public void customernumberForWhichDeletionHasToBeDone(long custNum) {
        deleteCNum=custNum;
        scenario.log("<span style='color:orange'>Capturing Customer number for deletion"+custNum+"</span>");
    }

    @When("submit delete request to endpoint {string}")
    public void submitDeleteRequestToEndpoint(String endpoint) {
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("customerNumber",deleteCNum).delete(endpoint+"/{customerNumber}");
        scenario.log("<span style='color:orange'>Delete Request is submitted for the customer</span>");

    }

    @And("validate the response message {string}")
    public void validateTheResponseMessageResponseMessage(String message) {
        scenario.log("<span style='color:orange'>Validation of Response message</span>");
        String type=response.getHeader("content-type");
        if(type.contains("json"))
            assertEquals(response.jsonPath().getString("message"),message);
        else
            assertEquals(response.asString(),message);
    }

    @And("Initiate funds transfer for {double} with {long} to endpoint {string}")
    public void initiateFundsTransferForTransferAmountWithCustomerNumberToEndpoint(double amount,long custNum,String endpoint) {
        transferAmount=amount;
        transfer=new TransferRequest(fAccNum,tAccNum,amount);
        scenario.log("<span style='color:orange'> Transfer request payload is created.</span>");
        String payload=mapper.writeValueAsString(transfer);
        scenario.log("<b>Request Payload:</b><br><pre>" + payload + "</pre>");
        response=given().spec(BaseAPI.getRequestSpec()).pathParam("customerNumber",custNum).body(transfer).log().all().put(endpoint+"/{customerNumber}");
    }
}

