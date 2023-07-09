package baykov.daniel.springbootlibraryapp.services;

import baykov.daniel.springbootlibraryapp.payload.dto.AuthorDTO;
import baykov.daniel.springbootlibraryapp.payload.response.AuthorResponse;

public interface AuthorService {

    AuthorDTO createAuthor(AuthorDTO authorDTO);

    AuthorDTO getAuthorById(long id);

    AuthorResponse getAllAuthors(int pageNo, int pageSize, String sortBy, String sortDir);

    AuthorDTO updateAuthorById(AuthorDTO authorDTO, long id);

    void deleteAuthorById(long id);
}
