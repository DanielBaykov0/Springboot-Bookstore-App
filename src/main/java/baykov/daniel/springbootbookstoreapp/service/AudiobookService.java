package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Audiobook;
import baykov.daniel.springbootbookstoreapp.entity.Author;
import baykov.daniel.springbootbookstoreapp.entity.Category;
import baykov.daniel.springbootbookstoreapp.entity.Narrator;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.AudiobookRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.AudiobookResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.mapper.AudiobookMapper;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.AudiobookRepository;
import baykov.daniel.springbootbookstoreapp.repository.AuthorRepository;
import baykov.daniel.springbootbookstoreapp.repository.CategoryRepository;
import baykov.daniel.springbootbookstoreapp.repository.NarratorRepository;
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
public class AudiobookService {

    private final AudiobookRepository audiobookRepository;
    private final ServiceUtil serviceUtil;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final NarratorRepository narratorRepository;

    @Transactional
    public AudiobookResponseDTO createAudiobook(AudiobookRequestDTO audiobookRequestDTO) {
        log.info("Creating audiobook...");

        List<Author> authors = authorRepository.findAllById(audiobookRequestDTO.getAuthorIDs());
        serviceUtil.validateIDs(audiobookRequestDTO.getAuthorIDs(), authors, AUTHOR);
        log.debug("Fetched authors: {}", authors);

        List<Category> categories = categoryRepository.findAllById(audiobookRequestDTO.getCategoryIDs());
        serviceUtil.validateIDs(audiobookRequestDTO.getCategoryIDs(), categories, CATEGORY);
        log.debug("Fetched categories: {}", categories);

        Narrator narrator = narratorRepository.findById(audiobookRequestDTO.getNarratorId())
                .orElseThrow(() -> new ResourceNotFoundException(NARRATOR, ID, audiobookRequestDTO.getNarratorId()));
        log.debug("Fetched narrator: {}", narrator);

        Audiobook audiobook = new Audiobook(
                AudiobookMapper.INSTANCE.dtoToEntity(audiobookRequestDTO),
                authors,
                categories,
                narrator);

        audiobookRepository.save(audiobook);
        log.info("Created audiobook with ID: {}", audiobook.getId());
        return AudiobookMapper.INSTANCE.entityToDTO(audiobook);
    }

    public AudiobookResponseDTO getAudiobookById(Long audiobookId) {
        log.info("Getting audiobook by ID: {}", audiobookId);
        Audiobook audiobook = audiobookRepository.findById(audiobookId)
                .orElseThrow(() -> new ResourceNotFoundException(AUDIOBOOK, ID, audiobookId));
        log.info("Audiobook with ID {} retrieved successfully.", audiobookId);
        return AudiobookMapper.INSTANCE.entityToDTO(audiobook);
    }

    public GenericResponse<AudiobookResponseDTO> getAllAudiobooks(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving audiobooks...");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Audiobook> audiobooks = audiobookRepository.findAll(pageable);
        List<AudiobookResponseDTO> content = AudiobookMapper.INSTANCE.entityToDTO(audiobooks.getContent());
        log.info("Retrieved {} audiobooks.", audiobooks.getContent().size());
        return serviceUtil.createGenericResponse(audiobooks, content);
    }

    @Transactional
    public AudiobookResponseDTO updateAudiobookById(Long audiobookId, AudiobookRequestDTO audiobookRequestDTO) {
        log.info("Start updating audiobook with ID: {}", audiobookId);
        Audiobook audiobook = audiobookRepository.findById(audiobookId)
                .orElseThrow(() -> new ResourceNotFoundException(AUDIOBOOK, ID, audiobookId));
        log.debug("Fetched audiobook: {}", audiobook);

        List<Author> authors = null;
        if (audiobookRequestDTO.getAuthorIDs() != null) {
            authors = authorRepository.findAllById(audiobookRequestDTO.getAuthorIDs());
            serviceUtil.validateIDs(audiobookRequestDTO.getAuthorIDs(), authors, AUTHOR);
        }
        log.debug("Fetched authors: {}", authors);

        List<Category> categories = null;
        if (audiobookRequestDTO.getCategoryIDs() != null) {
            categories = categoryRepository.findAllById(audiobookRequestDTO.getCategoryIDs());
            serviceUtil.validateIDs(audiobookRequestDTO.getCategoryIDs(), categories, CATEGORY);
        }
        log.debug("Fetched categories: {}", categories);

        Narrator narrator = null;
        if (audiobookRequestDTO.getNarratorId() != null) {
            narrator = narratorRepository.findById(audiobookRequestDTO.getNarratorId())
                    .orElseThrow(() -> new ResourceNotFoundException(NARRATOR, ID, audiobookRequestDTO.getNarratorId()));
        }
        log.debug("Fetched narrator: {}", narrator);

        Audiobook updatedAudiobook = AudiobookMapper.INSTANCE.dtoToEntity(audiobookRequestDTO);
        audiobook.update(
                updatedAudiobook,
                authors,
                categories,
                narrator);
        audiobookRepository.save(audiobook);

        log.info("Audiobook with ID {} updated successfully.", audiobookId);
        return AudiobookMapper.INSTANCE.entityToDTO(audiobook);
    }

    @Transactional
    public void deleteAudiobookById(Long audiobookId) {
        log.info("Deleting audiobook with ID: {}", audiobookId);
        Audiobook audiobook = audiobookRepository.findById(audiobookId)
                .orElseThrow(() -> new ResourceNotFoundException(AUDIOBOOK, ID, audiobookId));
        audiobookRepository.delete(audiobook);
        log.info("Audiobook with ID {} deleted successfully.", audiobookId);
    }

    public GenericResponse<AudiobookResponseDTO> getSearchedAudiobooks(String title, String authorName, String description, String category, Integer publicationYear, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Getting audiobooks by title: {}, authorName: {}, description: {}, category: {}, publicationYear: {}, page: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                title, authorName, description, category, publicationYear, pageNo, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        String searchableTitle = title != null ? "%" + title.trim().toLowerCase().replace(" ", "%") + "%" : null;
        String searchableAuthorName = authorName != null ? "%" + authorName.trim().toLowerCase().replace(" ", "%") + "%" : null;
        String searchableDescription = description != null ? "%" + description.trim().toLowerCase().replace(" ", "%") + "%" : null;
        String searchableCategory = category != null ? "%" + category.trim().toLowerCase().replace(" ", "%") + "%" : null;

        Page<Audiobook> audiobooks = audiobookRepository
                .findBySearchedParams(searchableTitle, searchableAuthorName, searchableDescription, searchableCategory, publicationYear, pageable);
        List<AudiobookResponseDTO> content = AudiobookMapper.INSTANCE.entityToDTO(audiobooks.getContent());
        log.info("Found {} audiobooks matching the params.", audiobooks.getTotalElements());
        return serviceUtil.createGenericResponse(audiobooks, content);
    }
}
