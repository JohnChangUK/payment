package proxy;

import com.google.protobuf.Message;
import com.johnchang.Balance;
import com.johnchang.Payment;
import com.johnchang.TransferRequest;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

public interface PaymentProxy {

    Message createContractFromPayment(Payment payment);

    TransactionReceipt executeTransferRequest(TransferRequest transferRequest);

    BigInteger balanceRequest(Balance message);
}
