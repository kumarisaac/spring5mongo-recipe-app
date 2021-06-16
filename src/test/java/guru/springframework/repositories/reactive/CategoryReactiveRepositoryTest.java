package guru.springframework.repositories.reactive;

import guru.springframework.domain.Category;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
class CategoryReactiveRepositoryTest {

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @Before
    public void setUp() {

    }


    @Test
    void testSave(){

        //given
        categoryReactiveRepository.deleteAll().block();
        Category category = new Category();
        category.setCategoryName("Indian");


        //when
        Category savedCategory = categoryReactiveRepository.save(category).block();

        //then
        assertEquals(category.getCategoryName(), savedCategory.getCategoryName());
        assertEquals(1L, categoryReactiveRepository.count().block());
    }

    @Test
    void testFindByDescription() {
        //when
        Category category = categoryReactiveRepository.findByCategoryName("Indian").block();

        //then
        assertEquals("Indian", category.getCategoryName());


    }
}