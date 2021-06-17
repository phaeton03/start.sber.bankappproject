package com.company.bankapi.model;

import java.math.BigDecimal;
import java.util.List;

public class BankAccount {
    //Уникальный банковский номер счета
    private final long accountID;
    //foreign key к таблице Client
    private final long clientID;
//    неизвестно понадобится ли данное поле
//    private static int numberOfAccounts = 100_000;
    private BigDecimal balance = BigDecimal.ZERO;

    public BankAccount(long accountNumber, BigDecimal balance, long clientID) {
        this.accountID = accountNumber;
        this.clientID = clientID;
        this.balance = balance;
     }
    public long getAccountNumber() {
        return accountID;
    }

    public long getClientID() {
        return clientID;
    }

    public BigDecimal getBalance() {
        return balance;
    }
// - нужно подумать как его правильнее изменять
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public void deposit(BigDecimal newBalance) {
        balance = balance.add(newBalance);
    }
    @Override
    public String toString() {
        return "accountID:= " + accountID + " balance:= " + balance;
    }
}
