package model;

public class ContractData {

    private String contractAddress;
    private String blockNumber;
    private String transactionHash;

    public ContractData(String contractAddress, String blockNumber, String transactionHash) {
        this.contractAddress = contractAddress;
        this.blockNumber = blockNumber;
        this.transactionHash = transactionHash;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContractData that = (ContractData) o;

        if (contractAddress != null ? !contractAddress.equals(that.contractAddress) : that.contractAddress != null)
            return false;
        if (blockNumber != null ? !blockNumber.equals(that.blockNumber) : that.blockNumber != null) return false;
        return transactionHash != null ? transactionHash.equals(that.transactionHash) : that.transactionHash == null;
    }

    @Override
    public int hashCode() {
        int result = contractAddress != null ? contractAddress.hashCode() : 0;
        result = 31 * result + (blockNumber != null ? blockNumber.hashCode() : 0);
        result = 31 * result + (transactionHash != null ? transactionHash.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Contract Data{" +
                "Contract address='" + contractAddress + '\'' +
                ", Block Number='" + blockNumber + '\'' +
                ", Transaction Hash='" + transactionHash + '\'' +
                '}';
    }
}
