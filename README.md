# 💱 Currency Converter API (AWS Lambda + API Gateway)

This project implements a currency conversion API using AWS infrastructure. The main flow is:

1. The client sends an HTTP request with currency conversion parameters.
2. The request is routed through the API Gateway.
3. The Lambda function (written in Java) processes the data and calls an external currency exchange API.
4. The conversion result is returned to the client.

---

## 📌 Technologies Used

- **AWS Lambda (Java 17)**
- **Amazon API Gateway**
- **Java SDK 11+**
- **Maven**
- **External Currency Exchange API**
- **AWS IAM (for permissions)**

---

## ⚙️ Architecture Diagram

```mermaid
sequenceDiagram
    actor User
    User->>API Gateway: GET /currency?from=BRL&to=USD&amount=100
    API Gateway->>Lambda (Java): Invoke function
    Lambda (Java)->>API Exchange: GET /convert?from=BRL&to=USD&amount=100
    API Exchange-->>Lambda (Java): JSON with converted value
    Lambda (Java)-->>API Gateway: Formatted JSON
    API Gateway-->>User: HTTP Response
