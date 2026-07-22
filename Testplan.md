# Master Test Plan Document: Bank Operations REST API Automation & Quality Assurance

---

## 1. Introduction & Project Scope

### 1.1 Objective

This Test Plan outlines the strategy, scope, API inventory, functional analysis, defect triage, and automation framework design for validating the **Bank Operations REST API** suite. The primary objective is to verify system functionality, ensure data integrity, identify architectural design gaps, and establish a robust Behavior-Driven Development (BDD) automation pipeline.

### 1.2 Scope of Testing

The testing lifecycle covers two core functional modules:

* **Customer Endpoints:** Customer onboarding, profile updates, retrieval, directory listings, and offboarding/deletion.


* **Accounts and Transactions Endpoints:** Account creation, metadata retrieval, core fund transfers, and transaction history auditing.


---

## 2. Test Approach & Methodology

The project follows a structured 4-phase quality assurance and automation strategy:

1. **Manual Exploratory Validation:** Systematic evaluation of Swagger endpoints to map operational boundaries, verify business workflows, and isolate backend anomalies.


2. **Defect Triage & Scope Isolation:** Segregating unhandled API design gaps and system defects from automated regression pipelines until official backend fixes or architectural clarifications are provided.


3. **Happy Path Workflow Design:** Constructing end-to-end positive automation scenarios representing core banking user journeys.


4. **Negative Test Engineering:** Designing resilience test suites targeting boundary conditions, missing fields, malformed data types, and illegal state transitions.



---

## 3. Detailed Test Scenarios & Functional Analysis

### A. Customer Endpoints

| Endpoint & Method | Test Type | Scenario Description & Validation Criteria |
| --- | --- | --- |
| **`POST /customers`** *(Add a Customer)* | Positive | Add a customer with complete details and invoke `GET` to validate successful creation. *(Note: Expected response code mapped to `201 Created` despite Swagger documentation indicating `200`)*.

|
|  | Negative | Validate that `customerNumber` is mandatory, cannot be `null`, cannot accept string values, and duplicate customer numbers are rejected.

|
|  | Defect | **Critical:** Customer Number incorrectly accepts negative numbers (e.g., `-12`).

|
| **`GET /customers/{customerNumber}`** | Negative / Defect | Querying an invalid customer number incorrectly returns a `200 OK` status code instead of a proper `404 Not Found` and "Customer Not found" response.

|
| **`PUT /customers/{customerNumber}`** | Positive | Update all editable customer details for an existing profile and validate response codes/messages.

|
|  | Negative | Restrict updates for non-existing customer records with proper error messages.

|
|  | Defect | Modifying the immutable `customerNumber` field in the request payload returns a success `200` code even though the backend ignores the change.

|
| **`DELETE /customers/{customerNumber}`** | Positive | Delete an existing customer and validate response codes/messages.

|
|  | Negative | Deleting a non-existing customer must be blocked with proper error responses.

|
|  | Defect | Deletion fails to cascade and remove associated accounts; furthermore, account-to-customer relational mapping is missing.

|
| **`GET /customers/all`** | Positive | Fetch all bank customers, printing total count and customer numbers for validation.

|

---

### B. Accounts and Transactions Endpoints

| Endpoint & Method | Test Type | Scenario Description & Validation Criteria |
| --- | --- | --- |
| **`POST /accounts/add/{customerNumber}`** | Positive | Create an account under a customer profile and validate post-creation details.

|
|  | Negative | Validate mandatory checks for `accountNumber` (cannot be `null`, string, or negative), reject duplicate account numbers, and block creation for invalid customer numbers.

|
|  | Design Gap / Defect | Payload lacks a customer number linkage; account creation response lacks a success confirmation message (e.g., "Account created successfully").

|
| **`GET /accounts/{accountNumber}`** | Negative | Request details for a non-existing account to validate proper error codes and messages.

|
|  | Defect / Failure | Returns an unexpected `302 Found` error response with JSON instead of a `200 OK` and lacks a "Account found successfully" confirmation message.

|
| **`PUT /accounts/transfer/{customerNumber}`** | Positive | Execute fund transfer with valid inputs and verify initial/final account balances.

|
|  | Negative | Validate that transfers with invalid source/destination accounts or amounts exceeding account balances fail.

|
|  | Critical Defects | 1. Transfers succeed even when accounts do not belong to the specified customer.

<br>

<br>2. Transfers between identical source/destination accounts return success and log to transactions.

<br>

<br>3. Negative transfer amounts are accepted and appended.

|
| **`GET /accounts/transactions/{accountNumber}`** | Positive | Fetch transaction history, calculate total count, and compute net credit/debit balances.

|
|  | Defects | 1. Timestamp formatting is broken (date omitted, showing only `HH:MM:SS`).

<br>

<br>2. Non-existing account queries return an empty array `[]` instead of explicit error codes/messages.

|

---

## 4. Defect Summary & Architecture Gaps

* **Data Integrity Risks:** Core endpoints accept negative integers for identifiers (`customerNumber`, `accountNumber`) and financial amounts (fund transfers), compromising auditing standards.


* **HTTP Status Code Discrepancies:** Mismatches between REST standards and Swagger documentation (e.g., `POST /customers` returning `200` instead of `201`; invalid lookups returning `200` or `302` instead of `404`).


* **Relational Disconnects:** Complete decoupling between customer profiles and bank accounts (payloads lack foreign key mapping, and deletion fails to cascade).



---

## 5. Automation Solution Architecture

* **Tech Stack:** Java 17, Rest Assured library, Maven (Build tool).


* **Test Framework:** Cucumber BDD (Behavior-Driven Development) integrated with TestNG assertions.


* **Reporting Engine:** Extent Reports configured via `Extent.properties` and `extent-config.xml`.


* **Feature Organization:**
* `BankOperations.feature` — Focuses on positive/happy path validation workflows.


* `NegativeTestScenarios.feature` — Focuses on negative testing, boundary conditions, and error handling.




* **Execution Control:** Centralized `TestRunner` class configured with Cucumber options, tag filtering, and reporting plugins.



---