package pojo;

public class BankInformation {
    public String branchName;
    public int branchCode;
    public BranchAddress branchAddress;
    public int routingNumber;

    public BankInformation(String bName, int bCode, BranchAddress addr, int rNum) {
        this.branchName = bName;
        this.branchCode = bCode;
        this.branchAddress = addr;
        this.routingNumber = rNum;
    }
}
