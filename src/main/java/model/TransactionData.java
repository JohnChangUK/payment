package model;

import java.math.BigInteger;
import java.util.Objects;

public class TransactionData {

    private String transactionHash;
    private String blockHash;
    private BigInteger gasUsed;

    public TransactionData(String transactionHash, String blockHash, BigInteger gasUsed) {
        this.transactionHash = transactionHash;
        this.blockHash = blockHash;
        this.gasUsed = gasUsed;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public BigInteger getGasUsed() {
        return gasUsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionData that = (TransactionData) o;
        return Objects.equals(transactionHash, that.transactionHash) &&
                Objects.equals(blockHash, that.blockHash) &&
                Objects.equals(gasUsed, that.gasUsed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionHash, blockHash, gasUsed);
    }

    @Override
    public String toString() {
        return "Transaction data: " + "\n" +
                "transactionHash: " + transactionHash + '\n' +
                "BlockHash: " + blockHash + '\n' +
                "Gas used: " + gasUsed + '\n';
    }
}
