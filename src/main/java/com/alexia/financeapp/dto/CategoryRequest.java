package com.alexia.financeapp.dto;

import com.alexia.financeapp.entity.TransactionType;

public class CategoryRequest {

    private String name;
    private TransactionType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
