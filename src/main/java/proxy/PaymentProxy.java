package proxy;

import com.google.protobuf.Message;
import com.johnchang.Payment;
import com.johnchang.TransferRequest;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

public interface PaymentProxy {

    Message createContractFromPayment(Payment payment);

    TransactionReceipt executeTransferRequest(TransferRequest transferRequest);
}
