package model;

import java.util.Objects;

public class ContractCreationData {

    private String contractAddress;
    private String blockNumber;
    private String transactionHash;

    public ContractCreationData(String contractAddress, String blockNumber, String transactionHash) {
        this.contractAddress = contractAddress;
        this.blockNumber = blockNumber;
        this.transactionHash = transactionHash;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractCreationData that = (ContractCreationData) o;
        return Objects.equals(contractAddress, that.contractAddress) &&
                Objects.equals(blockNumber, that.blockNumber) &&
                Objects.equals(transactionHash, that.transactionHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contractAddress, blockNumber, transactionHash);
    }

    @Override
    public String toString() {
        return "Contract data: " + "\n" +
                "Contract address: " + contractAddress + '\n' +
                "Block Number: " + blockNumber + '\n' +
                "Transaction Hash: " + transactionHash + '\n';
    }
}
