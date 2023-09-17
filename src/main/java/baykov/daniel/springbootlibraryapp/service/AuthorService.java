package baykov.daniel.springbootlibraryapp.service;

import baykov.daniel.springbootlibraryapp.entity.Author;
import baykov.daniel.springbootlibraryapp.entity.City;
import baykov.daniel.springbootlibraryapp.entity.Country;
import baykov.daniel.springbootlibraryapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.AuthorDTO;
import baykov.daniel.springbootlibraryapp.payload.mapper.AuthorMapper;
import baykov.daniel.springbootlibraryapp.payload.response.AuthorResponse;
import baykov.daniel.springbootlibraryapp.repository.AuthorRepository;
import baykov.daniel.springbootlibraryapp.repository.CityRepository;
import baykov.daniel.springbootlibraryapp.repository.CountryRepository;
import baykov.daniel.springbootlibraryapp.service.helper.AuthorServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static baykov.daniel.springbootlibraryapp.constant.AppConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final AuthorServiceHelper authorServiceHelper;

    public AuthorDTO createAuthor(Long countryId, Long cityId, AuthorDTO authorDTO) {
        log.info("Creating author...");
        Country foundCountry = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, countryId));
        City foundCity = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException(CITY, ID, cityId));

        Author author = new Author(
                AuthorMapper.INSTANCE.dtoToEntity(authorDTO),
                foundCountry,
                foundCity);

        author = authorRepository.save(author);

        log.info("Created author with ID: {}", author.getId());
        return AuthorMapper.INSTANCE.entityToDto(author);
    }

    public AuthorDTO getAuthorById(Long authorId) {
        log.info("Getting author by ID: {}", authorId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException(AUTHOR, ID, authorId));
        log.info("Get author by ID: {}", authorId);
        return AuthorMapper.INSTANCE.entityToDto(author);
    }

    public AuthorResponse getAllAuthorsByCountryId(Long countryId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving authors by country ID: {}", countryId);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Author> authors = authorRepository.findAllByCountryId(countryId, pageable);
        log.info("Retrieved {} authors by country ID: {}", authors.getContent().size(), countryId);
        return authorServiceHelper.getAuthorResponse(authors);
    }

    public AuthorResponse getAllAuthorsByCityId(Long cityId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving authors by city ID: {}", cityId);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Author> authors = authorRepository.findAllByCityId(cityId, pageable);
        log.info("Retrieved {} authors by city ID: {}", authors.getContent().size(), cityId);
        return authorServiceHelper.getAuthorResponse(authors);
    }

    public AuthorResponse getAllAuthors(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving authors...");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Author> authors = authorRepository.findAll(pageable);
        log.info("Retrieved {} authors.", authors.getContent().size());
        return authorServiceHelper.getAuthorResponse(authors);
    }

    public AuthorDTO updateAuthorById(AuthorDTO authorDTO, Long authorId) {
        log.info("Start updating author with ID: {}", authorId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException(AUTHOR, ID, authorId));

        author.update(AuthorMapper.INSTANCE.dtoToEntity(authorDTO));

        authorRepository.save(author);
        log.info("Updated author with ID: {}", authorId);
        return AuthorMapper.INSTANCE.entityToDto(author);
    }

    public void deleteAuthorById(Long authorId) {
        log.info("Deleting author with ID: {}", authorId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException(AUTHOR, ID, authorId));
        authorRepository.delete(author);
        log.info("Author with ID {} deleted successfully.", authorId);
    }

    public AuthorResponse getSearchedAuthors(String name, String country, String city, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving authors...");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        String searchableName = name != null ? "%" + name.trim().toLowerCase().replace(" ", "%") + "%" : null;
        String searchableCountry = country != null ? "%" + country.trim().toLowerCase() + "%" : null;
        String searchableCity = city != null ? "%" + city.trim().toLowerCase() + "%" : null;

        Page<Author> content = authorRepository.findBySearchParams(
                searchableName, searchableCountry, searchableCity, pageable);
        log.info("Retrieved {} authors.", content.getTotalElements());
        return authorServiceHelper.getAuthorResponse(content);
    }
}
