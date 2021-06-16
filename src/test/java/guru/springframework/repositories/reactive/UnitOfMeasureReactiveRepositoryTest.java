package guru.springframework.repositories.reactive;

import guru.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataMongoTest
class UnitOfMeasureReactiveRepositoryTest {

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    UnitOfMeasure unitOfMeasure;

    @BeforeEach
    void setUp() {

        unitOfMeasureReactiveRepository.deleteAll().block();
        unitOfMeasure = UnitOfMeasure.builder().description("teaspoon").build();

    }

    @Test
    void testSave(){
        //when
        UnitOfMeasure savedUom = unitOfMeasureReactiveRepository.save(unitOfMeasure).block();

        //then
        assertEquals("teaspoon", savedUom.getDescription());
    }

    @Test
    void findByDescription() {
        //given
        unitOfMeasureReactiveRepository.save(unitOfMeasure).block();

        //when
        List<UnitOfMeasure> uoms = unitOfMeasureReactiveRepository.findAll().collectList().block();

        //given
        assertNotNull(uoms);
        assertEquals(1, uoms.size());

    }
}