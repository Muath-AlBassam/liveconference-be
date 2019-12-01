package com._4coders.liveconference.entities.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
}
