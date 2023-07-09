package baykov.daniel.springbootlibraryapp.services.impl;

import baykov.daniel.springbootlibraryapp.entities.Author;
import baykov.daniel.springbootlibraryapp.exceptions.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.AuthorDTO;
import baykov.daniel.springbootlibraryapp.payload.response.AuthorResponse;
import baykov.daniel.springbootlibraryapp.repositories.AuthorRepository;
import baykov.daniel.springbootlibraryapp.services.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper mapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, ModelMapper mapper) {
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

    @Override
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = mapToEntity(authorDTO);
        Author newAuthor = authorRepository.save(author);
        return mapToDTO(newAuthor);
    }

    @Override
    public AuthorDTO getAuthorById(long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        return mapToDTO(author);
    }

    @Override
    public AuthorResponse getAllAuthors(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Author> allAuthors = authorRepository.findAll(pageable);

        List<Author> authorList = allAuthors.getContent();

        List<AuthorDTO> authors = authorList.stream().map(this::mapToDTO).collect(Collectors.toList());;
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setContent(authors);
        authorResponse.setPageNo(allAuthors.getNumber());
        authorResponse.setPageSize(allAuthors.getSize());
        authorResponse.setTotalElements(allAuthors.getTotalElements());
        authorResponse.setTotalPages(allAuthors.getTotalPages());
        authorResponse.setLast(allAuthors.isLast());
        return authorResponse;
    }

    @Override
    public AuthorDTO updateAuthorById(AuthorDTO authorDTO, long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        author.setAuthorFirstName(authorDTO.getAuthorFirstName());
        author.setAuthorLastName(authorDTO.getAuthorLastName());
        author.setAuthorCountryBorn(authorDTO.getAuthorCountryBorn());
        author.setAuthorBirthDate(authorDTO.getAuthorBirthDate());
        author.setAuthorDeathDate(authorDTO.getAuthorDeathDate());
        author.setAuthorAlive(authorDTO.isAuthorAlive());

        Author newAuthor = authorRepository.save(author);
        return mapToDTO(newAuthor);
    }

    @Override
    public void deleteAuthorById(long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        authorRepository.delete(author);
    }

    private Author mapToEntity(AuthorDTO authorDTO) {
        return mapper.map(authorDTO, Author.class);
    }

    private AuthorDTO mapToDTO(Author author) {
        return mapper.map(author, AuthorDTO.class);
    }
}
