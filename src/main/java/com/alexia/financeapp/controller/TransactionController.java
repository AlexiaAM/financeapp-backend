package com.alexia.financeapp.controller;

import com.alexia.financeapp.dto.TransactionRequest;
import com.alexia.financeapp.entity.Transaction;
import com.alexia.financeapp.entity.User;
import com.alexia.financeapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Transaction createTransaction(@RequestBody TransactionRequest request) {
        UUID userId = getAuthenticatedUserId();
        return transactionService.createTransaction(
                userId,
                request.getAmount(),
                request.getDescription(),
                request.getDate(),
                request.getType(),
                request.getCategoryId()
        );
    }

    //this method handles GET requests (reading data)
    @GetMapping
    public List<Transaction> getTransactions() {
        UUID userId = getAuthenticatedUserId();
        return transactionService.getUserTransactions(userId);
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable("id") UUID transactionId){
        UUID userId = getAuthenticatedUserId();
        return transactionService.getUserTransactionById(userId, transactionId);
    }

    @GetMapping("/balance")
    public BigDecimal getBalance() {
        UUID userId = getAuthenticatedUserId();
        return transactionService.getCurrentBalance(userId);
    }

    private UUID getAuthenticatedUserId() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return user.getId();
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        transactionService.deleteTransaction(userId, id);
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable("id") UUID transactionId,
                                         @RequestBody TransactionRequest request) {
        UUID userId = getAuthenticatedUserId();
        return transactionService.updateTransaction(
                userId,
                transactionId,
                request.getAmount(),
                request.getDescription(),
                request.getDate(),
                request.getType(),
                request.getCategoryId()
        );
    }

    @GetMapping("/summary")
    public Map<String, BigDecimal> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        UUID userId = getAuthenticatedUserId();
        return transactionService.getMonthlySummary(userId, year, month);
    }

}
