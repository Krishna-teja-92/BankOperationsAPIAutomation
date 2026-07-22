Feature: Bank API Operations

  @createCustomer
  Scenario: Create a customer and validate get customer details
  Given I have valid customer details
  When I submit a POST request to "/customers/add"
  Then the response status code should be 201
  And validate the customer details with endpoint "/customers" and customerNumber
  And the response code should be 200 and response customerNumber should match the request

  @CreateAccount
  Scenario Outline: Create accounts under customer and validate get account details
    And create account information with <type> and <balance>
    And add the account to the customer with endpoint "/accounts/add"
    Then the response status code should be 201
    And Validate the account details for created account
    Then the response status code should be 200

    Examples:
    |type|balance|
    |"SAVINGS"|1000.00|
    |"CURRENT"|5000.00|
    |"BUSINESS"|10000.00|

    @transferFunds
    Scenario Outline: Transfer funds between accounts and validate the final balance in both accounts
      Given <fromAccountNum> and <toAccountNum> details
      And fetch the initial account balances for the given accounts
      And Initiate funds transfer request for <transferAmount> with <customerNumber> to endpoint "/accounts/transfer"
      Then the response status code should be 200
      And validate the response message <responseMessage>
      And validate the final balances in both accounts


      Examples:
      |fromAccountNum|toAccountNum|transferAmount|customerNumber|responseMessage|
      |98174         |59105       |100.00          |14603       |"Success: Amount transferred for Customer Number 14603"|
      |59105         |98174       |200.00          |14603       |"Success: Amount transferred for Customer Number 14603"|

    @transactionDetails
    Scenario Outline:  Get all transactions of the account and print net amount with credit or debit
      Given <accountNumber> for fetching transactions
      When submit a get request to endpoint "/accounts/transactions"
      Then the response status code should be 200
      And display the number of transactions and net amount for the account

      Examples:
      |accountNumber|
      |98174        |
      |59105        |

    @getAllCustomers
    Scenario: Get list of all customers in the bank
      Given Get request is submitted to endpoint "/customers/all" for fetching all customers
      Then the response status code should be 200
      And print the number of customers and list of customer numbers

    @updateCustomer
    Scenario Outline: Update the customer details
     Given <customerNumber> for which update has to be done
     When submit put request to endpoint "/customers"
     Then the response status code should be 200
     And validate the response message <responseMessage>

      Examples:
      |customerNumber|responseMessage|
      |86969         |"Success: Customer updated."|

    @deleteCustomer
    Scenario Outline: Delete the customer details
      Given <customerNumber> for which deletion has to be done
      When submit delete request to endpoint "/customers"
      Then the response status code should be 200
      And validate the response message <responseMessage>

      Examples:
      |customerNumber|responseMessage|
      |0             |"Success: Customer deleted."|























