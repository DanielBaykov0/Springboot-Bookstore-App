package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Author;
import baykov.daniel.springbootbookstoreapp.entity.City;
import baykov.daniel.springbootbookstoreapp.entity.Country;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.AuthorRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.AuthorResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.mapper.AuthorMapper;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.AuthorRepository;
import baykov.daniel.springbootbookstoreapp.repository.CityRepository;
import baykov.daniel.springbootbookstoreapp.repository.CountryRepository;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final ServiceUtil serviceUtil;

    @Transactional
    public AuthorResponseDTO createAuthor(AuthorRequestDTO authorRequestDTO) {
        log.info("Creating author...");

        Country foundCountry = countryRepository.findById(authorRequestDTO.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, authorRequestDTO.getCountryId()));
        log.debug("Fetched country: {}", foundCountry);
        City foundCity = cityRepository.findById(authorRequestDTO.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException(CITY, ID, authorRequestDTO.getCityId()));
        log.debug("Fetched city: {}", foundCity);

        Author author = new Author(
                AuthorMapper.INSTANCE.dtoToEntity(authorRequestDTO),
                foundCountry,
                foundCity);

        authorRepository.save(author);

        log.info("Created author with ID: {}", author.getId());
        return AuthorMapper.INSTANCE.entityToDTO(author);
    }

    public AuthorResponseDTO getAuthorById(Long authorId) {
        log.info("Getting author by ID: {}", authorId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException(AUTHOR, ID, authorId));
        log.info("Author with ID {} retrieved successfully.", authorId);
        return AuthorMapper.INSTANCE.entityToDTO(author);
    }

    public GenericResponse<AuthorResponseDTO> getAllAuthors(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving authors...");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Author> authors = authorRepository.findAll(pageable);
        List<AuthorResponseDTO> content = AuthorMapper.INSTANCE.entityToDTO(authors.getContent());
        log.info("Retrieved {} authors.", authors.getContent().size());
        return serviceUtil.createGenericResponse(authors, content);
    }

    @Transactional
    public AuthorResponseDTO updateAuthorById(AuthorRequestDTO authorRequestDTO, Long authorId) {
        log.info("Start updating author with ID: {}", authorId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException(AUTHOR, ID, authorId));
        Country foundCountry = null;
        if (authorRequestDTO.getCountryId() != null) {
            foundCountry = countryRepository.findById(authorRequestDTO.getCountryId())
                    .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, authorRequestDTO.getCountryId()));
        }
        log.debug("Fetched country: {}", foundCountry);

        City foundCity = null;
        if (authorRequestDTO.getCityId() != null) {
            foundCity = cityRepository.findById(authorRequestDTO.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException(CITY, ID, authorRequestDTO.getCityId()));
        }
        log.debug("Fetched city: {}", foundCity);

        Author updatedAuthor = AuthorMapper.INSTANCE.dtoToEntity(authorRequestDTO);
        author.update(updatedAuthor, foundCountry, foundCity);

        authorRepository.save(author);
        log.info("Author with ID {} updated successfully.", authorId);
        return AuthorMapper.INSTANCE.entityToDTO(author);
    }

    @Transactional
    public void deleteAuthorById(Long authorId) {
        log.info("Deleting author with ID: {}", authorId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException(AUTHOR, ID, authorId));
        authorRepository.delete(author);
        log.info("Author with ID {} deleted successfully.", authorId);
    }

    public GenericResponse<AuthorResponseDTO> getSearchedAuthors(String name, Long categoryId, Long cityId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Getting authors by name: {}, category ID: {}, city ID: {}, page: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                name, categoryId, cityId, pageNo, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        String searchableName = name != null ? "%" + name.trim().toLowerCase().replace(" ", "%") + "%" : null;

        Page<Author> authors = authorRepository.findBySearchParams(
                searchableName, categoryId, cityId, pageable);
        List<AuthorResponseDTO> content = AuthorMapper.INSTANCE.entityToDTO(authors.getContent());
        log.info("Found {} authors matching the params.", authors.getTotalElements());
        return serviceUtil.createGenericResponse(authors, content);
    }
}
