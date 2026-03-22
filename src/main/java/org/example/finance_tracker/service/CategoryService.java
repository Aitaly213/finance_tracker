package org.example.finance_tracker.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.finance_tracker.utils.mapper.Mapper;
import org.example.finance_tracker.dto.CategoryDto;
import org.example.finance_tracker.entity.CategoryEntity;
import org.example.finance_tracker.entity.UserEntity;
import org.example.finance_tracker.repository.CategoryRepository;
import org.example.finance_tracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final Mapper mapper;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository repository, Mapper mapper, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    private UserEntity getCurrentUser() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }


    public CategoryDto createCategory(CategoryDto categoryToCrete) {
        UserEntity user=getCurrentUser();

        var entity = new CategoryEntity(null, categoryToCrete.title(),
                categoryToCrete.emoji(), user);
        var createdCategory = repository.save(entity);

        return mapper.toDtoCategory(createdCategory);
    }


    public List<CategoryDto> getAllCategory() {
        UserEntity user=getCurrentUser();

        return repository.findAllByUserId(user.getId()).stream().map(mapper::toDtoCategory).toList();
    }

    @Transactional
    public void deleteCategory(Long id) {

        UserEntity user=getCurrentUser();

        if (!repository.existsByIdAndUserId(id, user.getId())) {
            throw new EntityNotFoundException("Not found category by this id : " + id);
        }
        repository.deleteByIdAndUserId(id, user.getId());
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryToUpdate) {
        UserEntity user=getCurrentUser();

        var oldCategory = repository.findByIdAndUserId(id, user.getId()).orElseThrow(()
                -> new EntityNotFoundException("Category not found by id =" + id));


        var newCategory = new CategoryEntity(
                oldCategory.getId(),
                categoryToUpdate.title(),
                categoryToUpdate.emoji(),
                user
        );

        repository.save(newCategory);
        return mapper.toDtoCategory(newCategory);
    }
}
