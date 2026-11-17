# AggieVault 💰

### Personal Finance & Budget Management Application

AggieVault is a Java-based personal finance application designed to help users manage their finances, track income and expenses, and monitor their spending habits. Built using object-oriented programming and SQLite database integration, the application provides tools for organizing financial transactions and reviewing monthly financial activity.

## Overview

Managing personal finances requires keeping track of income, expenses, and spending patterns. AggieVault provides a centralized application for recording financial transactions, organizing expenses by category, and reviewing financial summaries.

## Features

- **Transaction Management:** Add, edit, and delete income and expense transactions.
- **Expense Categorization:** Organize financial transactions into categories for easier tracking.
- **SQLite Database Integration:** Store financial records locally using SQLite.
- **Monthly Financial Reports:** Review monthly income, expenses, and net cash flow.
- **Category-Based Summaries:** Monitor expenses across different spending categories.
- **Data Persistence:** Retain financial records between application sessions.

## Technologies & Tools

| Technology | Application |
|---|---|
| Java | Core application development |
| Java Swing | Graphical user interface |
| SQLite | Relational database management |
| JDBC | Database connectivity |
| Object-Oriented Programming | Application architecture |
| IntelliJ IDEA | Development environment |
| Git & GitHub | Version control |

## Application Structure

```text
AggieVault/
│
├── main/
│   └── java/
│       ├── BudgetTrackApp.java
│       ├── Database.java
│       ├── Transaction.java
│       └── TransactionRepository.java
│
├── test/
│
├── data/
│   └── budgettrack.db
│
├── .gitignore
└── README.md
```

*The SQLite database is created locally and should not be committed to GitHub.*

## Getting Started

### Prerequisites

- Java Development Kit (JDK)
- IntelliJ IDEA
- SQLite JDBC Driver

### Installation & Setup

**1. Clone the repository**

```bash
git clone https://github.com/dcarter05/AggieVault.git
```

**2. Open the project**

Open the cloned project folder in IntelliJ IDEA.

**3. Configure SQLite JDBC**

Add the SQLite JDBC driver to the project's module dependencies in IntelliJ IDEA.
https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.54.1.0/sqlite-jdbc-3.54.1.0.jar

**4. Configure the database directory**

Ensure a `data` directory exists in the project root so SQLite can create and access the local database file.

**5. Run the application**

Locate `BudgetTrackApp.java` and run its `main` method through IntelliJ IDEA.

## Testing

The application can be tested by performing the following operations:

- Creating income and expense transactions.
- Editing existing transaction records.
- Deleting transactions.
- Reviewing monthly financial calculations.
- Checking category-based expense summaries.
- Restarting the application to verify database persistence.

## Future Enhancements

- Interactive financial dashboards and spending visualizations.
- Customizable monthly spending limits.
- Savings goals and progress tracking.
- Advanced transaction filtering and search.
- Expanded financial reporting and analytics.

## Developer

**Dorian Carter**  
Business Information Technology Student  
North Carolina A&T State University

- **GitHub:** [@dcarter05](https://github.com/dcarter05)
- **LinkedIn:** [Dorian Carter](https://www.linkedin.com/in/dorian-l-carter/)

## Project Purpose

AggieVault was developed as a personal software engineering project to strengthen practical experience in Java application development, object-oriented programming, database management, and software design.

Through this project, I applied programming concepts to build a functional application that addresses a real-world need while continuing to develop my technical skills.

---

*Built with Java and Aggie pride. 💙💛*
