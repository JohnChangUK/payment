package proxy;

import com.google.protobuf.Message;
import com.johnchang.Balance;
import com.johnchang.Payment;
import com.johnchang.TransferRequest;
import exception.NotEnoughFundsException;
import model.User;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

public interface Proxy {

    boolean checkIfUserHasEnoughFunds(User user) throws NotEnoughFundsException;

    Message createContractFromPayment(Payment payment);

    TransactionReceipt executeTransferRequest(TransferRequest transferRequest);

    BigInteger balanceRequest(Balance message);
}
