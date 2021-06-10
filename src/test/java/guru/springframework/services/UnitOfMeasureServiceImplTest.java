package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UnitOfMeasureServiceImplTest {

    @Mock
    private UnitOfMeasureRepository uomRepo;
    private UnitOfMeasureToUnitOfMeasureCommand commandConverter = new UnitOfMeasureToUnitOfMeasureCommand();
    UnitOfMeasureServiceImpl service;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new UnitOfMeasureServiceImpl(uomRepo,commandConverter);
    }

    @Test
    public void getUnitOfMeasure() {
        //given
        Set<UnitOfMeasure> uoms = new HashSet<>();
        uoms.add(UnitOfMeasure.builder().id("1").build());
        uoms.add(UnitOfMeasure.builder().id("2").build());
        uoms.add(UnitOfMeasure.builder().id("3").build());
        when(uomRepo.findAll()).thenReturn(uoms);

        //when
        Set<UnitOfMeasureCommand> uomCommands = service.getUnitOfMeasure();

        //then
        assertEquals(3, uomCommands.size());
        verify(uomRepo, times(1)).findAll();
    }
}