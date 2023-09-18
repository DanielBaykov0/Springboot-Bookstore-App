package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.Audiobook;
import baykov.daniel.springbootlibraryapp.payload.dto.AudiobookDTO;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface AudiobookMapper {

    AudiobookMapper INSTANCE = Mappers.getMapper(AudiobookMapper.class);

    AudiobookDTO entityToDto(Audiobook audiobook);

    List<AudiobookDTO> entityToDto(Iterable<Audiobook> audiobooks);

    Audiobook dtoToEntity(AudiobookDTO audiobookDTO);

    List<Audiobook> dtoToEntity(Iterable<AudiobookDTO> audiobookDTOS);
}
