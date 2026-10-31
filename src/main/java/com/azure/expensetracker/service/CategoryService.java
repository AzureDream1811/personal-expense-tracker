package com.azure.expensetracker.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azure.expensetracker.dto.request.CategoryRequest;
import com.azure.expensetracker.dto.response.CategoryResponse;
import com.azure.expensetracker.entity.Category;
import com.azure.expensetracker.entity.User;
import com.azure.expensetracker.exception.DuplicateResourceException;
import com.azure.expensetracker.exception.ResourceNotFoundException;
import com.azure.expensetracker.repository.CategoryRepository;
import com.azure.expensetracker.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public CategoryResponse createCategory(Long userId, CategoryRequest categoryRequest) {
        User user = getUserById(userId);

        if (categoryRepository.existsByUser_IdAndNameAndDeletedAtIsNull(userId, categoryRequest.name())) {
            throw new DuplicateResourceException(
                    "Category with name '" + categoryRequest.name() + "' already exists.");
        }

        Category category = Category.builder()
                .name(categoryRequest.name())
                .description(categoryRequest.description())
                .user(user)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long userId, Long categoryId) {
        Category category = categoryRepository.findByIdAndUser_IdAndDeletedAtIsNull(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        return mapToCategoryResponse(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories(Long userId) {
        return categoryRepository.findAllByUser_IdAndDeletedAtIsNull(userId)
                .stream()
                .map(this::mapToCategoryResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse updateCategory(Long userId, Long categoryId, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findByIdAndUser_IdAndDeletedAtIsNull(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        if (categoryRequest.name() != null
                && !categoryRequest.name().equals(category.getName())) {

            if (categoryRepository.existsByUser_IdAndNameAndDeletedAtIsNull(
                    userId, categoryRequest.name())) {
                throw new DuplicateResourceException(
                        "Category with name '" + categoryRequest.name() + "' already exists.");
            }

            category.setName(categoryRequest.name());
        }

        if (categoryRequest.description() != null) {
            category.setDescription(categoryRequest.description());
        }

        return mapToCategoryResponse(category);
    }

    @Transactional
    public void deleteCategory(Long userId, Long categoryId) {
        Category category = categoryRepository.findByIdAndUser_IdAndDeletedAtIsNull(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        category.setDeletedAt(LocalDateTime.now());
    }

    private User getUserById(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription());
    }
}
