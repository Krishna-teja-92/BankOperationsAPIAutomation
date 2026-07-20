package pojo;

public class AccountRequest {
    public long accountNumber;
    public BankInformation bankInformation;
    public String accountStatus, accountType;
    public double accountBalance;
    public String accountCreated;

    public AccountRequest(long accNum, BankInformation bankInfo, String status,
                          String type, double balance, String created) {
        this.accountNumber = accNum;
        this.bankInformation = bankInfo;
        this.accountStatus = status;
        this.accountType = type;
        this.accountBalance = balance;
        this.accountCreated = created;
    }
}
