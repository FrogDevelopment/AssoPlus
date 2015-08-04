/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dto.CategoryDto;
import fr.frogdevelopment.assoplus.entities.Category;
import org.springframework.stereotype.Service;

@Service("categoriesService")
public class CategoriesServiceImpl extends AbstractService<Category, CategoryDto> implements CategoriesService {

    CategoryDto createDto(Category bean) {
        CategoryDto dto = new CategoryDto();
	    dto.setId(bean.getId());
	    dto.setCode(bean.getCode());
	    dto.setLabel(bean.getLabel());

	    return dto;
    }

    Category createBean(CategoryDto dto) {
        Category bean = new Category();
	    bean.setId(dto.getId());
	    bean.setCode(dto.getCode());
	    bean.setLabel(dto.getLabel());

	    return bean;
    }

}
