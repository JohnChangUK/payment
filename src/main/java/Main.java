import com.johnchang.Currency;
import proxy.ERC20ContractProxy;

public class Main {

    public static void main(String[] args) {
        User user = new User<>(100, Currency.POUNDS, new ERC20ContractProxy());

        user.createContractWithPayment(user);
    }
}
