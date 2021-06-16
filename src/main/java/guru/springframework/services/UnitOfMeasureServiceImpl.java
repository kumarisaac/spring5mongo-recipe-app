package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

   // private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand commandConverter;

    public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
                                    UnitOfMeasureToUnitOfMeasureCommand commandConverter) {
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.commandConverter = commandConverter;
    }

    @Override
    public Flux<UnitOfMeasureCommand> getUnitOfMeasure() {

        log.debug("Inside Unit of measure service : getUnitOfMeasure method");
//        Set<UnitOfMeasureCommand> unitOfMeasureSet =  new HashSet<>();
//
//        unitOfMeasureRepository.findAll()
//                .forEach(unitOfMeasure -> {unitOfMeasureSet.add(commandConverter.convert(unitOfMeasure));});

        return unitOfMeasureReactiveRepository.findAll().map(commandConverter::convert);
    }
}
