Feature: Bank API Operations

@first
  Scenario: Create a customer and validate get customer details
  Given I have valid customer details
  When I submit a POST request to "/customers/add"
  Then the response status code should be 200
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

    @transfer
    Scenario Outline: Transfer funds between accounts and validate the final balance in both accounts
      Given <fromAccountNum> and <toAccountNum> details
      And fetch the initial account balances for the given accounts
      And Initiate funds transfer request for <transferAmount> with <customerNumber> to endpoint "/accounts/transfer"
      Then the response status code should be 200
      And validate the final balances in both accounts

      Examples:
      |fromAccountNum|toAccountNum|transferAmount|customerNumber|
      |98174         |59105       |100.00          |81545       |
      |59105         |98174       |200.00          |81545       |


    Scenario Outline:  Get all transactions of the account and print total credit and debit amounts
      Given <accountNumber> for fetching transactions
      When submit a get request to endpoint "/accounts/transactions"
      Then the response status code should be 200
      And display the number of transactions and net amount for the account

      Examples:
      |accountNumber|
      |98174        |
      |59105        |














