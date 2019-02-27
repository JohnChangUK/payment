package proxy;

import com.google.protobuf.Message;
import com.johnchang.Balance;
import com.johnchang.Currency;
import com.johnchang.Payment;
import com.johnchang.TransactionResponse;
import com.johnchang.TransferRequest;
import contracts.ERC20Contract;
import exception.NotEnoughFundsException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

// Address: 0x6c658dea826b8f8085fc59b5bd96477c52a6a675
public class PaymentProxy implements Proxy {

    private static final Logger log = LoggerFactory.getLogger(PaymentProxy.class);

    private Web3j web3j;
    private Credentials credentials;

    public PaymentProxy() {
        web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));
        credentials = WalletUtils.loadBip39Credentials("test", "test");
    }

    public boolean checkIfUserHasEnoughFunds(User user) throws NotEnoughFundsException {
        if (user.getCurrency().equals(Currency.POUNDS) && user.getAmount() >= 100) {
            return true;
        } else {
            throw new NotEnoughFundsException(user.getName() + " does not have enough funds");
        }
    }

    public Message createContractFromPayment(Payment payment) {
        ERC20Contract erc20Contract = null;

        try {
            erc20Contract = ERC20Contract.deploy(
                    web3j, credentials, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT,
                    new BigInteger(String.valueOf(payment.getInitialQuantity().toStringUtf8())),
                    payment.getName(),
                    BigInteger.valueOf(payment.getDecimalUnits()), payment.getSymbol()
            ).send();
        } catch (Exception e) {
            log.error("Could not create contract: " + e);
        }

        TransactionReceipt receipt = erc20Contract.getTransactionReceipt().get();

        return TransactionResponse.newBuilder()
                .setContractAddress(receipt.getContractAddress())
                .setTransactionHash(receipt.getTransactionHash())
                .setBlockHash(receipt.getBlockHash())
                .setTransactionIndex(receipt.getTransactionIndex().toString())
                .setBlockNumber(receipt.getBlockNumber().toString())
                .build();
    }

    public TransactionReceipt executeTransferRequest(TransferRequest request) {
        String contractAddress = request.getContractAddress();

        ERC20Contract erc20Contract = ERC20Contract.load(
                contractAddress, web3j, credentials, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);

        TransactionReceipt transactionReceipt = null;
        try {
            transactionReceipt = erc20Contract.transfer(
                    request.getToAccountId(), new BigInteger(request.getValue().toStringUtf8())).send();
        } catch (Exception e) {
            log.error("Could not execute transfer request: " + e);
        }

        return transactionReceipt;
    }

    public BigInteger balanceRequest(Balance balance) {
        String contractAddress = balance.getContractAddress();

        ERC20Contract eRC20Contract = ERC20Contract.load(
                contractAddress, web3j, credentials, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT
        );

        BigInteger balancesValue = null;
        try {
            balancesValue = eRC20Contract.balances(balance.getAccountId()).send();
        } catch (Exception e) {
            log.error("Could not execute balance request: " + e);
        }

        return balancesValue;
    }
}