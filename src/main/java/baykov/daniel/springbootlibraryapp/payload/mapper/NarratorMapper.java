package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.Narrator;
import baykov.daniel.springbootlibraryapp.payload.dto.NarratorDTO;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface NarratorMapper {

    NarratorMapper INSTANCE = Mappers.getMapper(NarratorMapper.class);

    @Mapping(target = "countryId", expression = "java(narrator.getCountry().getId())")
    @Mapping(target = "cityId", expression = "java(narrator.getCity().getId())")
    NarratorDTO entityToDto(Narrator narrator);

    List<NarratorDTO> entityToDto(Iterable<Narrator> narrators);

    Narrator dtoToEntity(NarratorDTO narratorDTO);

    List<Narrator> dtoToEntity(Iterable<NarratorDTO> narratorDTOS);
}
