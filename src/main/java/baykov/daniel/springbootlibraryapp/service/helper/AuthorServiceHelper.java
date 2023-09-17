package baykov.daniel.springbootlibraryapp.service.helper;

import baykov.daniel.springbootlibraryapp.entity.Author;
import baykov.daniel.springbootlibraryapp.payload.dto.AuthorDTO;
import baykov.daniel.springbootlibraryapp.payload.mapper.AuthorMapper;
import baykov.daniel.springbootlibraryapp.payload.response.AuthorResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorServiceHelper {
    public AuthorResponse getAuthorResponse(Page<Author> authors) {
        List<AuthorDTO> content = AuthorMapper.INSTANCE.entityToDto(authors.getContent());

        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setContent(content);
        authorResponse.setPageNo(authors.getNumber());
        authorResponse.setPageSize(authors.getSize());
        authorResponse.setTotalElements(authors.getTotalElements());
        authorResponse.setTotalPages(authors.getTotalPages());
        authorResponse.setLast(authors.isLast());
        return authorResponse;
    }
}
