package pojo;

public class TransferRequest {
    public long fromAccountNumber;
    public long toAccountNumber;
    public double transferAmount;

    public TransferRequest(long from, long to, double amount) {
        this.fromAccountNumber = from;
        this.toAccountNumber = to;
        this.transferAmount = amount;
    }
}
