package baykov.daniel.springbootlibraryapp.service;

import baykov.daniel.springbootlibraryapp.payload.dto.AuthorDTO;
import baykov.daniel.springbootlibraryapp.payload.response.AuthorResponse;

public interface AuthorService {

    AuthorDTO createAuthor(AuthorDTO authorDTO);

    AuthorDTO getAuthorById(Long id);

    AuthorResponse getAllAuthors(int pageNo, int pageSize, String sortBy, String sortDir);

    AuthorDTO updateAuthorById(AuthorDTO authorDTO, Long id);

    void deleteAuthorById(Long id);

    AuthorResponse getAllAuthorsByAuthorFirstNameOrAuthorLastNameOrAuthorCountry(String firstName, String lastName, String country, int pageNo, int pageSize, String sortBy, String sortDir);
}
