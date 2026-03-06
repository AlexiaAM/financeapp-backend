package com.alexia.financeapp.controller;


import com.alexia.financeapp.dto.CategoryRequest;
import com.alexia.financeapp.entity.Category;
import com.alexia.financeapp.entity.TransactionType;
import com.alexia.financeapp.entity.User;
import com.alexia.financeapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Category createCategory(@RequestBody CategoryRequest request){
        UUID userId = getAuthenticatedUserId();
        return categoryService.createCategory(userId, request.getName(), request.getType());
    }

    @GetMapping
    public List<Category> getCategories() {
        UUID userId = getAuthenticatedUserId();
        return categoryService.getUserCategories(userId);
    }

    @GetMapping("/type/{type}")
    public List<Category> getCategoriesByType(@PathVariable TransactionType type) {
        UUID userId = getAuthenticatedUserId();
        return categoryService.getUserCategoriesByType(userId, type);
    }

    private UUID getAuthenticatedUserId() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return user.getId();
    }
}
