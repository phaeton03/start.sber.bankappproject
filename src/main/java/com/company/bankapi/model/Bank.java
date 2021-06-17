package com.company.bankapi.model;

import dao.*;
import dao.dbService.DBService;
import dao.executor.Executor;
import exceptions.NegativeDepositAmount;
import exceptions.NullCardsException;
import server.BankServer;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * Сервисный класс где происходит вся основная логика программы
 * @author      Nikolay Nikolskiy
 * @version     %I% %G%
 * @since       1.0
 */
public class Bank {
    private Connection h2Connection = DBService.getH2Connection();
    private Executor executor = new Executor(h2Connection);
    private BankCardDAOInt bankCardDAO = new BankCardDAO(executor);
    private BankAccountDAOInt bankAccountDAO = new BankAccountDAO(executor);
    private ClientDAOInt clientDAO = new ClientDAO(executor);

    /**
     * Получение объекта класса Client из БД
     * @param id - уникальный идентификатор пользователя
     * @return - Client
     */
    public Client getClient(long id) {
        try {
            return clientDAO.getClient(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return null;
    }

    /**
     * Получение объекта класса Account из БД
     * @param accountID - уникальный идентификатор счета
     * @return - Account
     */
    public BankAccount getAccount(long accountID) {
        try {
            BankAccount account = bankAccountDAO.getAccount(accountID);
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Выпуск новой карты
     * @param uniqueSN - номер карты
     * @param accountID - уникальный идентификатор счета
     */
    public void issueCard(String uniqueSN, long accountID) {
        try {
        BankAccount account = getAccount(accountID);
        //новая запись в таблице о выпуске новой Карты
        bankCardDAO.insertCard(account.getClientID(), accountID, uniqueSN);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получения списка карт клиента
     * @param userID - уникальный идентификатор полбзователя
     * @return - List<BankCard> - список
     */
    public List<BankCard> lookClientsCards(long userID) {
        Client client = getClient(userID);
        if(client.getCards() == null) {
            try {
                throw new NullCardsException(String.format("Clent id = %d has not any cards yet", userID));
            } catch (NullCardsException e) {
                e.printStackTrace();
            }
        }

        return client.getCards();
    }

    /**
     * Пополнение счета по его уникальному индентификационному номеру
     * @param accountID - уникальный индентификационный номер счета
     * @param amount - сумма пополнения
     */
    public void deposit(long accountID, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
        try {
          throw new NegativeDepositAmount("negative amount: " + amount);
        } catch (NegativeDepositAmount e) {
            e.printStackTrace();
        }
        }
        BankAccount bankAccount = getAccount(accountID);
        bankAccount.deposit(amount);
        BigDecimal newBalance = bankAccount.getBalance();
        try {
        bankAccountDAO.updateBalance(accountID, newBalance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * проверка баланса счетов по уникальному индентификационному номеру клиента
     * @param userID - уникальный индентификационный номер клиента
     * @return - Map<Long, BigDecimal> ключ - индентификационный номер счета, значение - сумма денег на счете
     */
    public Map<Long, BigDecimal> checkBalance(long userID) {
        Map<Long, BigDecimal> mapAccount = new HashMap<>();
        Client client = getClient(userID);
        List<BankAccount> accounts = client.getAccounts();
        for(BankAccount ba : accounts)
            mapAccount.put(ba.getAccountNumber(), ba.getBalance());
        return mapAccount;
    }

    /**
     * Прекращение работы с программой и отключение соединения
     */
    public void exit() {
        try {
            h2Connection.close();
            } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обнуление БД
     * @throws Exception
     */
    public void dropAll() throws Exception{
        executor.execUpdate("DROP table cards");
        executor.execUpdate("DROP table accounts");
        executor.execUpdate("DROP table clients");
    }

    /**
     * создание и заполнение БД
     */
    public void createAndFillTables() {
        try {
            clientDAO.createTable();
            bankAccountDAO.createTable();
            bankCardDAO.createTable();
            fillTableFile();
         } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Служебный метод для заполнения SQL таблиц
     */
    private void fillTableFile() {
        try(BufferedReader readerClients = new BufferedReader(new FileReader("src/main/resources/Client"));
                BufferedReader readerAccounts = new BufferedReader(new FileReader("src/main/resources/Accounts"));
                BufferedReader readerCards = new BufferedReader(new FileReader("src/main/resources/Cards")))
        {
            String clientsParam;
            String accountsParam;
            String cardsParam;

            while((clientsParam = readerClients.readLine()) != null) {
                long id = Long.parseLong(clientsParam.split(";")[0]);
                String name = clientsParam.split(";")[1];
                clientDAO.insertClient(id, name);
            }
            while((accountsParam = readerAccounts.readLine()) != null) {
                long accountID = Long.parseLong(accountsParam.split(";")[0]);
                long clientID = Long.parseLong(accountsParam.split(";")[1]);
                BigDecimal amount = new BigDecimal(accountsParam.split(";")[2]);
                bankAccountDAO.insertAccount(accountID, clientID, amount);
            }
            while((cardsParam = readerCards.readLine()) != null) {
                long clientID = Long.parseLong(cardsParam.split(";")[0]);
                long accountID = Long.parseLong(cardsParam.split(";")[1]);
                String sn = cardsParam.split(";")[2];
                bankCardDAO.insertCard(clientID, accountID, sn);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
