import com.johnchang.Currency;
import model.User;
import proxy.PaymentProxy;

public class Main {

    public static void main(String[] args) {
        User user = new User<>("James", 1000, Currency.POUNDS, new PaymentProxy());

        user.createContractWithPayment(user);
    }
}
