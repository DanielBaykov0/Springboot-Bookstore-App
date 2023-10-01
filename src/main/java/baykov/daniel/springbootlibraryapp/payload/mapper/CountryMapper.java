package baykov.daniel.springbootlibraryapp.payload.mapper;


import baykov.daniel.springbootlibraryapp.entity.Country;
import baykov.daniel.springbootlibraryapp.payload.dto.CountryDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CountryMapper {

    CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);

    CountryDTO entityToDTO(Country country);

    List<CountryDTO> entityToDTO(Iterable<Country> countries);

    Country dtoToEntity(CountryDTO countryDTO);

    List<Country> dtoToEntity(Iterable<CountryDTO> countryDTOS);
}
