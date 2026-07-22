API Testing & Automation Documentation
1. API Test Scenarios & Analysis
A. Customer Endpoints
1) POST /customers (Add a Customer)

Design Change: The endpoint should not contain the verb "add"; the POST method inherently indicates resource creation.  


Negative Test Scenarios:

Customer Number is mandatory for adding a customer and cannot be null, as it uniquely identifies the customer.  

Customer Number cannot accept string values.  

The system must not allow the creation of a customer with an already existing customer number.  

Defect: Customer Number is currently accepting negative numbers, which is incorrect.  

Positive Scenario: Add a customer with all details and invoke the GET customer details endpoint to validate successful creation.  

Assumption: Although the POST request should return a 201 Created status code for new resource creation, Swagger documents it as 200. Validation is mapped to expect 201.  

2) GET /customers/{customerNumber} (Get Customer Details)
Defect: When called with an invalid customer number, the API returns a status code of 200 instead of a proper "Customer Not found" response.  

Positive Scenario: Validated as part of the add customer workflow.  

3) PUT /customers/{customerNumber} (Update a Customer)
Negative Test Scenario: Updating a non-existing customer should be restricted, returning a proper error message and response code.  

Defect: While customer numbers should not be updateable (though other details can be), modifying the customer number in the request payload returns a success 200 response code even though the backend does not alter the number. A proper validation message should be displayed.  

Positive Scenario: Update all details for an existing customer and validate the response code and message.  

4) DELETE /customers/{customerNumber} (Delete Customer & Related Accounts)
Negative Test Scenario: Deleting a non-existing customer must be blocked with a proper error message and response code.  

Defect: Deleting a customer should automatically remove all associated accounts; however, the system fails to delete linked accounts. Furthermore, no endpoint is available to check accounts associated with a customer number, and there is no relational mapping between accounts and customers (account details lack an associated customer number).  

Positive Scenario: Delete an existing customer and validate the response code and message.  

5) GET /customers/all (Find All Customers)
Positive Scenario: Fetch all customers in the bank, printing the total customer count and their customer numbers for validation.  

B. Accounts and Transactions Endpoints

1) POST /accounts/add/{customerNumber} (Add a New Account)
Design Gap: The add account request payload lacks a customer number, causing a missing linkage between accounts and customers. Additionally, no endpoint exists to fetch all accounts for a given customer.  

Negative Test Scenarios:

Account Number is mandatory and cannot be null.  

Account Number cannot accept string values.  

The system must reject duplicate account numbers during creation.  

Adding an account with an invalid customer number must fail with appropriate validation codes and messages.  

Defect: Account Number incorrectly accepts negative values.  

Positive Scenario: Create an account for a customer and validate account details post-creation.  

Defect: Account creation response lacks a success confirmation message like "Account created successfully".  

2) GET /accounts/{accountNumber} (Get Account Details)
Negative Test Scenario: Requesting details for a non-existing account must return proper error status codes and messages.  

Positive Scenario: Validated as part of the add account workflow.  

Defect/Failure: Get account details fails to display a confirmation message ("Account found successfully"). Instead of a 200 success code with the account details JSON, it returns an unexpected 302 error response.  

3) PUT /accounts/transfer/{customerNumber} (Transfer Funds)
Critical Context: A core banking business operation requiring strict operational validation.  

Design Gap: Customer numbers are technically not required for fund transfers between two accounts, yet the system throws an error response when an invalid customer number is provided.  

Defects:

Funds transfer succeeds even when the source and destination accounts do not belong to the specified customer number.  

Initiating a transfer with identical source and destination account numbers returns a success response code and logs it into the transaction details list.  

Negative transfer amounts are accepted by the system and appended to the transaction list.  

Negative Test Scenarios:

Transfers with invalid source account numbers must fail with correct response codes and messages.  

Transfers with invalid destination account numbers must fail.  

Transfers exceeding the available account balance must be blocked with an appropriate response message and code.  

Positive Scenario: Execute fund transfers with valid inputs and verify initial/final balances for both accounts.  

4) GET /accounts/transactions/{accountNumber} (Get All Transactions)
Defects:

Transaction timestamp formatting is broken (date is omitted, showing only time in HH:MM:SS format).  

Requesting transactions for non-existing or invalid account numbers returns an empty array [] instead of proper error codes and validation messages.  

Positive Scenario: Fetch all transactions for an account, calculate total transaction count, and compute net credit/debit balances for validation.  

2. Functional Analysis of Bank APIs
Customer Onboarding: Registering a customer with all necessary details.  

Customer Profile Updates: Modifying attributes like email IDs and phone numbers post-onboarding.  

Customer Retrieval: Fetching individual customer details (Note: does not include account information).  

Customer Exit: Deleting a customer profile from the bank system.  

Customer Directory: Fetching details for all registered customers.  

Account Management: Accounts are mandatory for executing banking transactions, with each customer linking to one or more accounts. Accounts can be added under a customer, though update, closure, and fetch-by-customer endpoints are missing.  

Transactions: Performing financial actions (such as fund transfers) once accounts are active, followed by transaction history lookups.  

3. Test Approach
Manual Validation: Execute exploratory and functional testing of APIs based on the functional analysis and relational mappings.  

Defect Triage & Automation Scope Exclusion: Isolate design gaps, defects, and unhandled observations, excluding them from automated test suites until fixes or clarifications are provided.  

Happy Path Design: Construct positive test workflows derived from the core business functional analysis.  

Negative Test Design: Analyze potential failure states and edge cases within the banking API ecosystem to implement robust negative automation coverage.  

4. Automation Solution Architecture
  
Tech Stack: Java 17, Rest Assured library, Maven (Build tool & dependency manager).  

Framework Pattern: Cucumber BDD (Behavior-Driven Development) integrated with TestNG assertions.  

Reporting: Extent Reports (configured via Extent.properties and extent-config.xml).  

Design Highlights:

Scenarios are written in business-readable Gherkin syntax to ensure clarity for business users.  

All request and response payloads are mapped cleanly using dedicated POJO classes.  

Feature files and step definitions are clearly segregated.  

Project Feature Structure:

BankOperations.feature — Focuses on positive/happy path workflows.  

NegativeTestScenarios.feature — Focuses on negative test cases and validation handling.  

Test Runner Configuration: A central runner class controls test execution, allows tag filtering, and defines reporting adapters.  

Use of AI :-

AI is used to solve warnings issues in the project dependencies.

AI is used for document presentation by providing my draft document.

AI is used in getting sample config files for reporting.

AI is used in designing static payload strings quickly.

Assumptions/Notes:- 

1)Authentication is not used for accessing endpoints as details are not available.

2)All Negative test scenarios are using static payloads.

3)Defects,Design gaps,Observations are noted and those areas are not included in the automation solution.

4)Postive business critical workflow is prioritized followed by Negative tests to avoid data integrity issues.

https://github.com/Krishna-teja-92/BankOperationsAPIAutomation.git
