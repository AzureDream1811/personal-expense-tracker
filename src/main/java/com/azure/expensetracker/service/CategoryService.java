package com.azure.expensetracker.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azure.expensetracker.dto.request.CategoryRequest;
import com.azure.expensetracker.dto.response.CategoryResponse;
import com.azure.expensetracker.entity.Category;
import com.azure.expensetracker.exception.DuplicateResourceException;
import com.azure.expensetracker.exception.ResourceNotFoundException;
import com.azure.expensetracker.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.name());
        category.setDescription(categoryRequest.description());

        Category savedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        return mapToCategoryResponse(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToCategoryResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        if (categoryRequest.name() != null) {
            if (categoryRequest.name().equals(category.getName())) {
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
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        category.setDeletedAt(LocalDateTime.now());
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription());
    }
}
