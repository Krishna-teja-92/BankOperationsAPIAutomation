package pojo;

public class TransferRequest {
    public String fromAccountId;
    public String toAccountId;
    public double amount;

    public TransferRequest(String from, String to, double amount) {
        this.fromAccountId = from;
        this.toAccountId = to;
        this.amount = amount;
    }
}
