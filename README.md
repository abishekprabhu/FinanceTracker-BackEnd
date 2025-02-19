
# FinanceTracker-BackEnd

FinanceTracker-BackEnd is a Spring Boot-based REST API designed for efficient personal finance management. It provides features for tracking income, expenses, budgets, wallet balances, and bill payments with seamless Razorpay integration.

## Features

- **User Management**: Secure authentication and session handling.  
- **Income & Expense Tracking**: Categorized transactions with real-time updates.  
- **Budget Management**: Set and monitor budgets with visual analytics.  
- **Wallet System**: Add money, track balances, and make bill payments.  
- **Bill Payments**: Integrated Razorpay for seamless transactions.  
- **Notifications**: WebSocket-based real-time alerts.  
- **PDF Reports**: Export monthly transaction summaries.  
- **Caching**: Redis integration for optimized performance.  

## Tech Stack

- **Backend**: Spring Boot, Hibernate, MySQL, Redis, WebSockets  
- **Frontend**: [FinanceTracker-FrontEnd](https://github.com/abishekprabhu/FinanceTracker-FrontEnd) (Angular)  
- **Payment Integration**: Razorpay  

## Setup Instructions

### Prerequisites
- Java 17+
- MySQL
- Redis
- Maven

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/abishekprabhu/FinanceTracker-BackEnd.git
   cd FinanceTracker-BackEnd
   ```
2. Configure **application.properties** with your database and Redis details.
3. Build and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
4. API runs at `http://localhost:8080`.

## API Endpoints

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Authenticate user |
| `GET`  | `/api/transactions` | Fetch transaction history |
| `POST` | `/api/wallet/add` | Add money to wallet |
| `POST` | `/api/bills/pay` | Pay upcoming bills |
| `GET`  | `/api/budget/summary` | Get budget insights |

## Contributions
Feel free to contribute via pull requests. Fork the repository, make changes, and submit a PR.

## License
This project is licensed under the MIT License.

---

Build by **Abishek Prabhu**
```

This README is structured, concise, and informative, making it easy for users to understand and use your API. 🚀
