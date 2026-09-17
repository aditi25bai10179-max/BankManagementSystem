# BankManagementSystem

## Overview

A simple **Java-based Bank Management System** that allows users to manage bank accounts and perform basic banking operations. The system also includes a **rule-based fraud-check simulation** to identify suspicious withdrawal transactions.

## Features

* Create a new bank account
* Deposit money
* Withdraw money
* Check account balance
* Display all accounts
* Display flagged accounts
* Delete an account
* Track withdrawal history
* Detect suspicious withdrawals
* Flag suspicious accounts for review
* Menu-driven console interface

## Fraud-Check Simulation

The system checks withdrawals using simple rules:

* Withdrawal above **₹50,000**
* Withdrawal much higher than the account's usual average
* Multiple withdrawals in the same session
* Withdrawal that uses nearly the entire account balance

Suspicious transactions generate a warning and allow the user to either continue or cancel the transaction.

> **Note:** The fraud detection is an educational simulation and is not a real banking security system.

## Technologies Used

* **Java**
* **Object-Oriented Programming (OOP)**
* **ArrayList**
* **Scanner**
* Java Collections

## Project Structure

```text
BankManagementSystem/
│
├── BankManagementSystemWithFraudCheck.java
└── README.md
```

## How to Run

### 1. Compile the program

```bash
javac BankManagementSystemWithFraudCheck.java
```

### 2. Run the program

```bash
java BankManagementSystemWithFraudCheck
```

## Main Menu

```text
===== Simple Bank Management System =====

1. Create New Account
2. Deposit Money
3. Withdraw Money
4. Check Balance
5. Display All Accounts
6. Display Flagged Accounts
7. Delete Account
8. Exit
```

## Purpose

This project was developed for educational purposes to demonstrate **Java programming, Object-Oriented Programming, collections, account management, and basic fraud-detection logic**.

## Future Improvements

* Database integration
* User login and authentication
* Transaction records
* JUnit testing
* Multithreading
* Custom exception handling
* File-based audit logging
* Graphical User Interface (GUI)
