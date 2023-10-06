package baykov.daniel.springbootbookstoreapp.payload.mapper;

import baykov.daniel.springbootbookstoreapp.entity.City;
import baykov.daniel.springbootbookstoreapp.payload.dto.CityDTO;
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
