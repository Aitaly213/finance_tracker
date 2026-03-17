package org.example.finance_tracker.controller;

import org.example.finance_tracker.dto.CategoryDto;
import org.example.finance_tracker.service.CategoryService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(
            @RequestBody CategoryDto categoryDto
    ){
        return ResponseEntity.ok().body(service.createCategory(categoryDto));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory(){
        return ResponseEntity.ok().body(service.getAllCategory());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ){
        service.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDto categoryToUpdate
    ){

        return ResponseEntity.ok().body(service.updateCategory(id,categoryToUpdate));
    }

}
