package pojo;

public class TransferRequest {
    public int fromAccountNumber;
    public int toAccountNumber;
    public double transferAmount;

    public TransferRequest(int from, int to, double amount) {
        this.fromAccountNumber = from;
        this.toAccountNumber = to;
        this.transferAmount = amount;
    }
}
