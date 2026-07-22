Feature: Negative scenario testing for Bank API's

  Scenario Outline: Call Add customer API with invalid inputs and validate the response code
    Given <InvalidCustomerNumber> to add customer API
    When I submit a POST request to endpoint "/customers/add"
    Then validate response status code should be 400

    Examples:
    |InvalidCustomerNumber|
    |"Null"               |
    |"ABC"                |

  Scenario Outline: Call Add account API with invalid inputs and validate the response code
    Given <InvalidAccountNumber> to add Account API
    When I submit a POST request to endpoint "/accounts/add" with <customerNumber>
    Then validate response status code should be 400

    Examples:
      |InvalidAccountNumber|customerNumber|
      |"Null"              |172522328     |
      |"ABC"               |172522328     |

   Scenario Outline: Call Add customer API with existing customer Number
     Given existing <customerNumber>
     When I submit a POST request to endpoint "/customers/add"
     Then validate response status code should be 400
     Then validate the response message <responseMessage> for negative tests

     Examples:
       |customerNumber|responseMessage|
       |75931          |"Customer with id: 75931 already exist"|


  Scenario Outline: Call Add Account API with existing Account Number and invalid customer Number
    Given existing account <accountNumber>
    When I submit a POST request to endpoint "/accounts/add" with <customerNumber>
    Then validate response status code should be 400
    Then validate the response message <responseMessage> for negative tests

    Examples:
    |accountNumber|customerNumber|responseMessage|
    |98174       |75931          |"Account with id: 98174 already exist!"|
    |98174       |719181         |"Customer with id: 719181 does not exist!"|


  Scenario Outline: Call get account details with non existing account number
    Given existing account <invalidAccountNumber>
    When  a get request is submitted to endpoint "/accounts"
    Then validate response status code should be 404
    Then validate the response message <responseMessage> for negative tests


    Examples:
    |invalidAccountNumber|responseMessage|
    |981741               |"Account Number 981741 not found."|


  Scenario Outline: Call update customer with non existent customer number
    Given update request payload is created
    When submit a put request to endpoint "/customers" with <nonExistingCustomer>
    Then validate response status code should be 404
    Then validate the response message <responseMessage> for negative tests

    Examples:
    |nonExistingCustomer|responseMessage|
    |719181             |"Customer Number 719181 not found."|

    Scenario Outline: Call Delete customer API for non existent customer number
      Given submit a delete request to endpoint "/customers" with <nonExistingCustomer>
      Then validate response status code should be 400
      Then validate the response message <responseMessage> for negative tests

      Examples:
        |nonExistingCustomer|responseMessage|
        |719181             |"Customer does not exist."|



    Scenario Outline: Call Transfer Funds API with invalid from and to account numbers
      Given <fromAccountNum> and <toAccountNum> details
      And Initiate funds transfer for <transferAmount> with <customerNumber> to endpoint "/accounts/transfer"
      Then the response status code should be 404
      And validate the response message <responseMessage>

      Examples:
        |fromAccountNum|toAccountNum|transferAmount|customerNumber|responseMessage|
        |981741        |59105       |100.00        |14603         |"From Account Number 981741 not found."|
        |59105         |981741      |200.00        |14603         |"To Account Number 981741 not found." |


  Scenario Outline: Call Transfer Funds API with valid account numbers but transfer amount more than account balance
    Given <fromAccountNum> and <toAccountNum> details
    And Initiate funds transfer for <transferAmount> with <customerNumber> to endpoint "/accounts/transfer"
    Then the response status code should be 400
    And validate the response message <responseMessage>

    Examples:
      |fromAccountNum|toAccountNum|transferAmount|customerNumber|responseMessage|
      |98174        |59105       |10000.00        |14603         |"Insufficient Funds."|