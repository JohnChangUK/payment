package model;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.johnchang.Balance;
import com.johnchang.Currency;
import com.johnchang.Payment;
import com.johnchang.TransactionResponse;
import com.johnchang.TransferRequest;
import exception.NotEnoughFundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import proxy.Proxy;

import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Consumer;

public class User<T extends Proxy> {

    private static final Logger log = LoggerFactory.getLogger(User.class);

    private String name;
    private long amount;
    private Currency currency;
    private T proxy;
    private String contractAddress;
    private Consumer<Object> printFunctionResponse;

    public User(String name, long amount, Currency currency, T proxy) {
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.proxy = proxy;
        printFunctionResponse = this::printFunctionResponse;
    }

    public void createContractWithPayment(User user) {
        try {
            if (proxy.checkIfUserHasEnoughFunds(user)) {
                deductUserAccountBalance(user);

                Payment payment = createPayment(user.amount, user.currency, "1000000",
                        "James's contract", 6, "WPAY");

                Message transactionResponse = proxy.createContractFromPayment(payment);
                printContractCreationData((TransactionResponse) transactionResponse);

                TransferRequest request = createTransferRequest(
                        "0xd20973DEE7602f8AEA53ea2520266Dd7C16FCd4D", "10000");

                try {
                    TransactionReceipt receipt = proxy.executeTransferRequest(request);
                    TransactionData transactionData = creationTransactionData(
                            receipt.getTransactionHash(), receipt.getBlockHash(), receipt.getGasUsed());

                    printFunctionResponse.accept(transactionData);
                } catch (Exception e) {
                    log.error("Error when creating function call to Ethereum: ", e);
                }

                Balance balance = createBalance(contractAddress, "0xd20973DEE7602f8AEA53ea2520266Dd7C16FCd4D");

                try {
                    BigInteger totalBalance = proxy.balanceRequest(balance);
                    printFunctionResponse.accept("Total balance: " + totalBalance);
                } catch (Exception e) {
                    log.error("Error when creating function call to Ethereum: ", e);
                }
            }
        } catch (NotEnoughFundsException e) {
            log.error("Error: ", e);
        }
    }

    private Balance createBalance(String contractAddress, String address) {
        return Balance.newBuilder()
                .setContractAddress(contractAddress)
                .setAccountId(address)
                .build();
    }

    private TransactionData creationTransactionData(
            String transactionHash, String blockHash, BigInteger gasUsed) {
        return new TransactionData(transactionHash, blockHash, gasUsed);
    }

    private void printContractCreationData(TransactionResponse transactionResponse) {
        contractAddress = transactionResponse.getContractAddress();
        String blockNumber = transactionResponse.getBlockNumber();
        String transactionHash = transactionResponse.getTransactionHash();

        ContractCreationData contractCreationData = new ContractCreationData(contractAddress, blockNumber, transactionHash);
        System.out.println(contractCreationData);
    }

    private TransferRequest createTransferRequest(String accountId, String value) {
        return TransferRequest.newBuilder()
                .setContractAddress(contractAddress)
                .setToAccountId(accountId)
                .setValue(ByteString.copyFromUtf8(value))
                .build();
    }

    private Payment createPayment(long amount, Currency currency, String quantity,
                                  String name, int decimalUnits, String symbol) {
        return Payment.newBuilder()
                .setAmount(amount)
                .setCurrency(currency)
                .setInitialQuantity(ByteString.copyFromUtf8(quantity))
                .setName(name)
                .setDecimalUnits(decimalUnits)
                .setSymbol(symbol)
                .build();
    }

    private void deductUserAccountBalance(User user) {
        long originalUserAccountBalance = user.getAmount();
        user.setAmount(originalUserAccountBalance -= 100);
    }

    private void printFunctionResponse(Object message) {
        System.out.println(message);
    }

    public String getName() {
        return name;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User<?> user = (User<?>) o;
        return amount == user.amount &&
                Objects.equals(name, user.name) &&
                currency == user.currency &&
                Objects.equals(proxy, user.proxy) &&
                Objects.equals(contractAddress, user.contractAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, amount, currency, proxy, contractAddress);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", currency=" + currency +
                ", proxy=" + proxy +
                ", contractAddress='" + contractAddress + '\'' +
                '}';
    }
}
