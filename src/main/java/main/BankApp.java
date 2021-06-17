package main;

import com.company.bankapi.model.Bank;
import com.company.bankapi.model.Client;
import server.BankServer;

public class BankApp {
    public static void main(String[] args) {
            Bank bank = new Bank();
            bank.createAndFillTables();
            BankServer.start();
        }
}
