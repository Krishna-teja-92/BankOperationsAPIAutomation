package pojo;

public class TransactionDetailsResponse {

        private long accountNumber;
        private String txDateTime;
        private String txType;
        private double txAmount;

        public TransactionDetailsResponse()
        {
        }
    public long getAccountNumber() { return accountNumber; }
    public String getTxDateTime() { return txDateTime; }
    public String getTxType() { return txType; }
    public double getTxAmount() { return txAmount; }

    public void setAccountNumber(long accountNumber) { this.accountNumber = accountNumber; }
    public void setTxDateTime(String txDateTime) { this.txDateTime = txDateTime; }
    public void setTxType(String txType) { this.txType = txType; }
    public void setTxAmount(double txAmount) { this.txAmount = txAmount; }
}
