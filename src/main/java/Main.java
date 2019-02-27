import com.johnchang.Currency;
import proxy.ERC20PaymentProxy;

public class Main {

    public static void main(String[] args) {
        User user = new User<>(1000, Currency.POUNDS, new ERC20PaymentProxy());

        user.createContractWithPayment(user);
    }
}
