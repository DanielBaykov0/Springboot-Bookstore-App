package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.City;
import baykov.daniel.springbootlibraryapp.payload.dto.CityDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CityMapper {

    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    @Mapping(target = "countryId", expression = "java(city.getCountry().getId())")
    CityDTO entityToDTO(City city);

    List<CityDTO> entityToDTO(Iterable<City> cities);

    City dtoToEntity(CityDTO cityDTO);

    List<City> dtoToEntity(Iterable<CityDTO> cityDTOS);
}
