package com.alexia.financeapp.service;

import com.alexia.financeapp.entity.Category;
import com.alexia.financeapp.entity.TransactionType;
import com.alexia.financeapp.entity.User;
import com.alexia.financeapp.repository.CategoryRepository;
import com.alexia.financeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    public Category createCategory(UUID userId, String name, TransactionType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = new Category();

        category.setType(type);
        category.setName(name);
        category.setUser(user);

        return categoryRepository.save(category);
    }

    public List<Category> getUserCategories(UUID userId) {
        return categoryRepository.findByUserId(userId);
    }

    public List<Category> getUserCategoriesByType(UUID userId, TransactionType type) {
        return categoryRepository.findByUserIdAndType(userId, type);
    }
}