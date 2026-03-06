package com.alexia.financeapp.repository;

import com.alexia.financeapp.entity.Category;
import com.alexia.financeapp.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserId(UUID userId);
    List<Category> findByUserIdAndType(UUID userId, TransactionType type);
    boolean existsByName(String name);
}
