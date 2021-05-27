import server.BankServer;

public class BankApp {
    public static void main(String[] args) {
        BankServer server = new BankServer();

        server.start();
    }
}
