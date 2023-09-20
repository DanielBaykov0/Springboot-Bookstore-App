package baykov.daniel.springbootlibraryapp.service.helper;

import baykov.daniel.springbootlibraryapp.entity.Narrator;
import baykov.daniel.springbootlibraryapp.payload.dto.NarratorDTO;
import baykov.daniel.springbootlibraryapp.payload.mapper.NarratorMapper;
import baykov.daniel.springbootlibraryapp.payload.response.NarratorResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NarratorServiceHelper {

    public NarratorResponse getNarratorResponse(Page<Narrator> narrators) {
        List<NarratorDTO> content = NarratorMapper.INSTANCE.entityToDto(narrators.getContent());

        NarratorResponse narratorResponse = new NarratorResponse();
        narratorResponse.setContent(content);
        narratorResponse.setPageNo(narrators.getNumber());
        narratorResponse.setPageSize(narrators.getSize());
        narratorResponse.setTotalElements(narrators.getTotalElements());
        narratorResponse.setTotalPages(narrators.getTotalPages());
        narratorResponse.setLast(narrators.isLast());
        return narratorResponse;
    }
}
