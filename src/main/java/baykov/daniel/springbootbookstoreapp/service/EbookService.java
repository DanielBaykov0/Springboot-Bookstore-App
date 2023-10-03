package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Author;
import baykov.daniel.springbootbookstoreapp.entity.Category;
import baykov.daniel.springbootbookstoreapp.entity.Ebook;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.EbookRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.EbookResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.mapper.EbookMapper;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.AuthorRepository;
import baykov.daniel.springbootbookstoreapp.repository.CategoryRepository;
import baykov.daniel.springbootbookstoreapp.repository.EbookRepository;
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
public class EbookService {

    private final EbookRepository ebookRepository;
    private final ServiceUtil serviceUtil;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public EbookResponseDTO createEbook(EbookRequestDTO ebookRequestDTO) {
        log.info("Creating ebook...");

        List<Author> authors = authorRepository.findAllById(ebookRequestDTO.getCategoryIDs());
        serviceUtil.validateIDs(ebookRequestDTO.getAuthorIDs(), authors, AUTHOR);
        log.debug("Fetched authors: {}", authors);

        List<Category> categories = categoryRepository.findAllById(ebookRequestDTO.getCategoryIDs());
        serviceUtil.validateIDs(ebookRequestDTO.getCategoryIDs(), categories, CATEGORY);
        log.debug("Fetched categories: {}", categories);

        Ebook ebook = new Ebook(
                EbookMapper.INSTANCE.dtoToEntity(ebookRequestDTO),
                authors,
                categories);

        ebookRepository.save(ebook);
        log.info("Created ebook with ID: {}", ebook.getId());
        return EbookMapper.INSTANCE.entityToDTO(ebook);
    }

    public EbookResponseDTO getEbookById(Long ebookId) {
        log.info("Getting ebook by ID: {}", ebookId);
        Ebook ebook = ebookRepository.findById(ebookId)
                .orElseThrow(() -> new ResourceNotFoundException(EBOOK, ID, ebookId));
        log.info("Ebook with ID {} retrieved successfully.", ebookId);
        return EbookMapper.INSTANCE.entityToDTO(ebook);
    }

    @Transactional
    public GenericResponse<EbookResponseDTO> getAllEbooks(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving ebooks....");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Ebook> ebooks = ebookRepository.findAll(pageable);
        List<EbookResponseDTO> content = EbookMapper.INSTANCE.entityToDTO(ebooks.getContent());
        log.info("Retrieved {} ebooks.", ebooks.getContent().size());
        return serviceUtil.createGenericResponse(ebooks, content);
    }

    @Transactional
    public EbookResponseDTO updateEBookById(Long ebookId, EbookRequestDTO ebookRequestDTO) {
        log.info("Start updating ebook with ID: {}", ebookId);
        Ebook ebook = ebookRepository.findById(ebookId)
                .orElseThrow(() -> new ResourceNotFoundException(EBOOK, ID, ebookId));
        log.debug("Fetched ebook: {}", ebook);

        List<Author> authors = null;
        if (ebookRequestDTO.getAuthorIDs() != null) {
            authors = authorRepository.findAllById(ebookRequestDTO.getAuthorIDs());
            serviceUtil.validateIDs(ebookRequestDTO.getAuthorIDs(), authors, AUTHOR);
        }
        log.debug("Fetched authors: {}", authors);

        List<Category> categories = null;
        if (ebookRequestDTO.getCategoryIDs() != null) {
            categories = categoryRepository.findAllById(ebookRequestDTO.getCategoryIDs());
            serviceUtil.validateIDs(ebookRequestDTO.getCategoryIDs(), categories, CATEGORY);
        }
        log.debug("Fetched categories: {}", categories);

        ebook.update(EbookMapper.INSTANCE.dtoToEntity(ebookRequestDTO), authors, categories);
        ebookRepository.save(ebook);
        log.info("Ebook with ID {} updated successfully.", ebookId);
        return EbookMapper.INSTANCE.entityToDTO(ebook);
    }

    @Transactional
    public void deleteEbookById(Long ebookId) {
        log.info("Deleting ebook with ID: {}", ebookId);
        Ebook ebook = ebookRepository.findById(ebookId)
                .orElseThrow(() -> new ResourceNotFoundException(EBOOK, ID, ebookId));
        ebookRepository.delete(ebook);
        log.info("Ebook with ID {} deleted successfully.", ebookId);
    }

    public GenericResponse<EbookResponseDTO> getSearchedEbooks(String title, String authorName, String description, String category, Integer publicationYear, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Getting ebooks by title: {}, authorName: {}, description: {}, category: {}, publicationYear: {}, page: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                title, authorName, description, category, publicationYear, pageNo, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        String searchableTitle = title != null ? "%" + title.trim().toLowerCase().replace(" ", "%") + "%" : null;
        String searchableAuthorName = authorName != null ? "%" + authorName.trim().toLowerCase().replace(" ", "%") + "%" : null;
        String searchableDescription = description != null ? "%" + description.trim().toLowerCase().replace(" ", "%") + "%" : null;
        String searchableCategory = category != null ? "%" + category.trim().toLowerCase().replace(" ", "%") + "%" : null;

        Page<Ebook> ebooks = ebookRepository
                .findBySearchedParams(searchableTitle, searchableAuthorName, searchableDescription, searchableCategory, publicationYear, pageable);
        List<EbookResponseDTO> content = EbookMapper.INSTANCE.entityToDTO(ebooks.getContent());
        log.info("Found {} ebooks matching the params.", ebooks.getTotalElements());
        return serviceUtil.createGenericResponse(ebooks, content);
    }
}
