package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UnitOfMeasureServiceImplTest {

    @Mock
    private UnitOfMeasureReactiveRepository uomRepo;
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
        Flux<UnitOfMeasure> uoms = Flux.just(UnitOfMeasure.builder().id("1").build(),
                                             UnitOfMeasure.builder().id("2").build(),
                                             UnitOfMeasure.builder().id("3").build());
//        uoms.add(UnitOfMeasure.builder().id("1").build());
//        uoms.add(UnitOfMeasure.builder().id("2").build());
//        uoms.add(UnitOfMeasure.builder().id("3").build());
        when(uomRepo.findAll()).thenReturn(uoms);

        //when
        Set<UnitOfMeasureCommand> uomCommands = service.getUnitOfMeasure().collect(Collectors.toSet()).block();

        //then
        assertEquals(3, uomCommands.size());
        verify(uomRepo, times(1)).findAll();
    }
}