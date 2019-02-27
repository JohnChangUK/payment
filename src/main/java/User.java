import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.johnchang.Currency;
import com.johnchang.Payment;
import com.johnchang.TransactionResponse;
import com.johnchang.TransferRequest;
import model.ContractCreationData;
import model.TransactionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import proxy.PaymentProxy;

import java.math.BigInteger;

public class User<T extends PaymentProxy> {

    public static final Logger log = LoggerFactory.getLogger(User.class);

    private long amount;
    private Currency currency;
    private T proxy;
    private String contractAddress;

    User(long amount, Currency currency, T proxy) {
        this.amount = amount;
        this.currency = currency;
        this.proxy = proxy;
    }

    void createContractWithPayment(User user) {
        if (user.currency.equals(Currency.POUNDS) && user.amount >= 100) {
            Payment payment = createPayment(user.amount, user.currency, "1000",
                    "James's contract", 6, "ERC20");

            Message transactionResponse = proxy.createContractFromPayment(payment);
            printContractCreationData((TransactionResponse) transactionResponse);
            TransferRequest request = createTransferRequest("0xd20973DEE7602f8AEA53ea2520266Dd7C16FCd4D", "100");
            TransactionReceipt receipt = proxy.executeTransferRequest(request);

            String transactionHash = receipt.getTransactionHash();
            String blockHash = receipt.getBlockHash();
            BigInteger gasUsed = receipt.getGasUsed();

            TransactionData transactionData = creationTransactionData(transactionHash,
                    blockHash, gasUsed);
            System.out.println(transactionData);
        }
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
}
