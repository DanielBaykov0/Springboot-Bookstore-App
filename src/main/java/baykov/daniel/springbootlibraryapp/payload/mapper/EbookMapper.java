package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.Ebook;
import baykov.daniel.springbootlibraryapp.payload.dto.EbookDTO;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface EbookMapper {

    EbookMapper INSTANCE = Mappers.getMapper(EbookMapper.class);

    EbookDTO entityToDto(Ebook ebook);

    List<EbookDTO> entityToDto(Iterable<Ebook> ebooks);

    Ebook dtoToEntity(EbookDTO ebookDTO);

    List<Ebook> dtoToEntity(Iterable<EbookDTO> ebookDTOS);
}
