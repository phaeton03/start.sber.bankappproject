package com.company.bankapi.model;

import java.util.Random;
//у Карты может быть только один счет и один пользователь
public class BankCard {
    //Позже можно подумать как сделать так чтобы вводилось именно 16 цифр
    private final String uniqueSN;
    private final int CCV = 333;
    //foreign key на таблицу клиентов
    private final long userID;
    //foreign key на таблицу счетов
    private final long accountID;
  // продумать как лучше реализовать данное поле
  // private String expDate;
    public BankCard(String uniqueSN, long userID, long accountID) {
        this.uniqueSN = uniqueSN;
   //     CCV = 100 + (int) Math.random() * 899;
        this.userID = userID;
        this.accountID = accountID;
     }
    public String getUniqueSN() {
        return uniqueSN;
    }
    public int getCCV() {
        return CCV;
    }

    @Override
    public String toString() {
        String result = String.format("id: %d \n number: %s", accountID, uniqueSN);
        return result;
    }
}
