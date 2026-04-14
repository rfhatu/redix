# Loan Payment System — Radix Tech Assessment

Single Spring Boot application containing two logically separated domains:

- **Loan Domain**: Create and retrieve loans
- **Payment Domain**: Record payments against loans

Uses an **H2 in-memory database**.

---

## Build & Run (Java 21)

```bash
mvn clean test
mvn spring-boot:run
```

App runs on: `http://localhost:8080`

---

## H2 Console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:loanpaydb`
- User: `sa`
- Password: *(blank)*

---

## API Testing (curl)

### Create loan

```bash
curl -i -X POST http://localhost:8080/loans   -H 'Content-Type: application/json'   -d '{"loanAmount": 10000, "term": 12}'
```

### Get loan

```bash
curl -i http://localhost:8080/loans/1
```

### Make payment

```bash
curl -i -X POST http://localhost:8080/payments   -H 'Content-Type: application/json'   -d '{"loanId": 1, "paymentAmount": 1500}'
```

### Overpayment (expected 400)

```bash
curl -i -X POST http://localhost:8080/payments   -H 'Content-Type: application/json'   -d '{"loanId": 1, "paymentAmount": 999999}'
```

---

## ✅ Test Steps

### Run all tests

```bash
mvn test
```

### Run a specific test class

```bash
mvn -Dtest=LoanServiceTest test
mvn -Dtest=PaymentServiceTest test
```
