package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand commandConverter;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository, UnitOfMeasureToUnitOfMeasureCommand commandConverter) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.commandConverter = commandConverter;
    }

    @Override
    public Set<UnitOfMeasureCommand> getUnitOfMeasure() {

        log.debug("Inside Unit of measure service : getUnitOfMeasure method");
        Set<UnitOfMeasureCommand> unitOfMeasureSet =  new HashSet<>();

        unitOfMeasureRepository.findAll()
                .forEach(unitOfMeasure -> {unitOfMeasureSet.add(commandConverter.convert(unitOfMeasure));});

        return unitOfMeasureSet;
    }
}
