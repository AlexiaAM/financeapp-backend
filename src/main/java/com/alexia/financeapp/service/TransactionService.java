package com.alexia.financeapp.service;

import com.alexia.financeapp.entity.Category;
import com.alexia.financeapp.entity.Transaction;
import com.alexia.financeapp.entity.TransactionType;
import com.alexia.financeapp.entity.User;
import com.alexia.financeapp.repository.CategoryRepository;
import com.alexia.financeapp.repository.TransactionRepository;
import com.alexia.financeapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionService {


    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    public Transaction createTransaction(UUID userId, BigDecimal amount,
                                         String description, LocalDate date,
                                         TransactionType type, UUID categoryId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new RuntimeException("Category does not belong to this user");
        }

        if (category.getType() != type) {
            throw new RuntimeException("Category type does not match transaction type");
        }

        Transaction transaction = new Transaction();

        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setCategory(category);
        transaction.setDate(date);
        transaction.setType(type);
        transaction.setUser(user);
        transaction.setCreatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getUserTransactions(UUID userId) {
        return transactionRepository.findByUserIdOrderByDateDesc(userId);
    }

    public Transaction getUserTransactionById(UUID userId, UUID transactionId){
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        return transaction;
    }

    public BigDecimal getCurrentBalance(UUID userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        BigDecimal income = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expenses = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return income.subtract(expenses);
    }

    public void deleteTransaction(UUID userId, UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        transactionRepository.deleteById(transactionId);
    }

    public Transaction updateTransaction(UUID userId, UUID transactionId, BigDecimal amount, String description, LocalDate date, TransactionType type, UUID categoryId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setDate(date);
        transaction.setType(type);
        transaction.setCategory(category);

        return transactionRepository.save(transaction);
    }

    public Map<String, BigDecimal> getMonthlySummary(UUID userId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Transaction> transactions = transactionRepository
                .findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate);

        BigDecimal income = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expenses = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = income.subtract(expenses);

        Map<String, BigDecimal> summary = new HashMap<>();
        summary.put("income", income);
        summary.put("expenses", expenses);
        summary.put("balance", balance);

        return summary;
    }
}
