import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.johnchang.Currency;
import com.johnchang.Payment;
import com.johnchang.TransactionResponse;
import com.johnchang.TransferRequest;
import model.ContractData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import proxy.PaymentProxy;

import java.util.concurrent.TimeUnit;

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
            printContractData((TransactionResponse) transactionResponse);
            TransferRequest request = createTransferRequest("100");
            TransactionReceipt receipt = proxy.executeTransferRequest(request);
            System.out.println(receipt);
        }
    }

    private void printContractData(TransactionResponse transactionResponse) {
        contractAddress = transactionResponse.getContractAddress();
        String blockNumber = transactionResponse.getBlockNumber();
        String transactionHash = transactionResponse.getTransactionHash();

        ContractData contractData = new ContractData(contractAddress, blockNumber, transactionHash);
        System.out.println(contractData);
    }

    private TransferRequest createTransferRequest(String value) {
        return TransferRequest.newBuilder()
                .setContractAddress(contractAddress)
                .setToAccountId("0x")
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

    private void waitForContractAddress() {
        while (contractAddress == null) {
            try {
                log.info("Waiting for the contract address");
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException("Could not block for contract address", e);
            }
        }
        log.info("Received contract address: " + contractAddress);
    }
}
