# start.sber.bankappproject
**Постановка задачи:** Проектная задача Bank API Требуется реализовать веб-сервис, реализующий логику работы клиентов с банковскими счетами. API Физического лица (клиента), должно позволять выполнять следующие действия по счетам:
1.	Выпуск новой карты по счету
2.	Проcмотр списка карт
3.	Внесение вредств
4.	Проверка баланса Структура приложения, структура схем таблиц базы данных, состав моделей и наименования точек входа не регламентируется. Необходимо самостоятельно решить, какие данные могут потребоваться для корректной работы приложения. Технические требования: Приложение должно быть написано на Java 8 с использованием Spring* (или Spring-boot) в любой конфигурации. Приложение должно собираться в *.jar и быть исполняемым из командной строки. Желательно должна быть применена архитектура MVC (Model View Controller). Стиль кода должен соответствовать java code conventions. В качестве базы данных допускается использование любой известной БД, но для простоты разработки и тестирования желательно использовать H2 Embedded с инициализацией схемы и предзаполнением БД при старте приложения. При использовании предустановленной БД, в проект необходимо добавить скрипты создания схемы БД. Взаимодействие с БД из приложения может быть реализовано как с использованием фреймворков (hibernate, jooq), так и на чистом JDBC. Не допускается использование SpringData. Схема БД должна быть нормализована, иметь корректные связи между сущностями и должны быть созданы индексы, где они необходимы. 1
Приложение должно принимать на вход и отдавать данные в формате JSON. Например возвращаемые данные для запроса списка карт: [ { "id": 1, "number": "1111 2222 3333 4444" }, { "id": 2, "number": "1111 2222 3333 4444" }, { "id": 3, "number": "1111 2222 3333 4444" } ] На все точки входа должны быть написаны интеграционные тесты, так же должны быть написаны юнит тесты. *При отсутствии навыков работы со Spring, допускается разработка приложения любым известным способом.
**Применяемые технологии**
1. Java 13
2. Junit 5.7.0
3. h2database 1.4.2
4. maven 4.0.0
**Структура классов и пакетов:** 
- Пакет Server : 
  - классы BankServer - класс для запуска сервера 
  - Handlers - классы реализующий интерфейс HttpHandler для REST запросов 
- Пакет Exceptions : 
  - классы NegativeDepositAmount - исключения если внесенная сумма отрицательная
  - NullCardsException - исключение если карт нет 
- Пакет DAO : 
  - классы BankAccountDAO, BankCardDAO, ClientDAO и их интерфейсы - стандартные операции с БД 
- пакет DBService: 
  - класс DBService - вспомогательный класс для установки соединения с сервером с методом getH2Connection. Вынесен в отдельный класс на случай если нужно будет взаимодействовать с другой БД 
- пакет Executor: 
 - класс Executor - вспомогательный класс реализующий метод execUpdate и execQuery для реализации SQL запросов. Идея данного класса в уменьшение написания повторяющегося кода при создании Statement а также гарантия что все открытые соединения будут закрыты 
интерфейс ResultHandler - функциональный интерфейс метод которого принимает ResultSet и возвращающий значение из него. Реализован чтобы гарантировать закрытие ResultSet 
- Пакет Model : 
 - класс Client, BankAccount, BankCard - классические entities. 
 - Bank - непосредственно интерфейс для взаимодействия с банком,  такими как выпуск карты, пополение счета и т.д


