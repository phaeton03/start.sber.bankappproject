package com.company.bankapi.model;

import java.util.*;
public class Client {

    private final long uniqueID;
    private String name;
    private List<BankCard> cards;
    private List<BankAccount> accounts;

    public Client(long uniqueID, String name) {
        this.uniqueID = uniqueID;
        this.name = name;
    }

    public Client(long uniqueID, String name, List<BankAccount> accounts) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.cards = null;
        this.accounts = accounts;
    }

    public Client(long id, String name, List<BankCard> cards, List<BankAccount> bankAccount) {
        this(id, name, bankAccount);
        this.cards = cards;
    }

    public long getUniqueID() {
        return uniqueID;
    }

    public String getName() {
        return name;
    }

    //сделать проверку на ноль в дальнейшем
    public List<BankCard> getCards() {
        return cards;
    }
    //подумать нужна ли здесь проверка на ноль
    public List<BankAccount> getAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        String result = String.format("name: = %s, uniqueID := %d ", name, uniqueID);
        return result;
    }
}
