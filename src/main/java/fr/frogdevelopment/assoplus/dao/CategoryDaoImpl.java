/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import fr.frogdevelopment.assoplus.entities.Category;
import fr.frogdevelopment.assoplus.entities.Licence;
import org.springframework.stereotype.Repository;

@Repository("categoryDao")
public class CategoryDaoImpl extends CommonDaoImpl<Category> implements CategoryDao {

}
