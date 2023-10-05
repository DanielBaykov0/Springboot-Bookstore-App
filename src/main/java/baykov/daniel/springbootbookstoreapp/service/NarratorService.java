package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.City;
import baykov.daniel.springbootbookstoreapp.entity.Country;
import baykov.daniel.springbootbookstoreapp.entity.Narrator;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.NarratorRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.NarratorResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.mapper.NarratorMapper;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.CityRepository;
import baykov.daniel.springbootbookstoreapp.repository.CountryRepository;
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
public class NarratorService {

    private final NarratorRepository narratorRepository;
    private final ServiceUtil serviceUtil;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    @Transactional
    public NarratorResponseDTO createNarrator(NarratorRequestDTO narratorRequestDTO) {
        log.info("Creating narrator...");
        Country foundCountry = countryRepository.findById(narratorRequestDTO.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, narratorRequestDTO.getCountryId()));
        log.debug("Fetched country: {}", foundCountry);

        City foundCity = cityRepository.findById(narratorRequestDTO.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException(CITY, ID, narratorRequestDTO.getCityId()));
        log.debug("Fetched city: {}", foundCity);

        Narrator narrator = new Narrator(
                NarratorMapper.INSTANCE.dtoToEntity(narratorRequestDTO),
                foundCountry,
                foundCity);

        narratorRepository.save(narrator);

        log.info("Created narrator with ID: {}", narrator.getId());
        return NarratorMapper.INSTANCE.entityToDTO(narrator);
    }

    public NarratorResponseDTO getNarratorById(Long narratorId) {
        log.info("Getting narrator by ID: {}", narratorId);
        Narrator narrator = narratorRepository.findById(narratorId)
                .orElseThrow(() -> new ResourceNotFoundException(NARRATOR, ID, narratorId));
        log.info("Narrator with ID {} retrieved successfully.", narratorId);
        return NarratorMapper.INSTANCE.entityToDTO(narrator);
    }

    @Transactional
    public GenericResponse<NarratorResponseDTO> getAllNarrators(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving narrators...");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Narrator> narrators = narratorRepository.findAll(pageable);
        List<NarratorResponseDTO> content = NarratorMapper.INSTANCE.entityToDTO(narrators.getContent());
        log.info("Retrieved {} narrators.", narrators.getContent().size());
        return serviceUtil.createGenericResponse(narrators, content);
    }

    @Transactional
    public NarratorResponseDTO updateNarratorById(Long narratorId, NarratorRequestDTO narratorRequestDTO) {
        log.info("Start updating narrator with ID: {}", narratorId);
        Narrator narrator = narratorRepository.findById(narratorId)
                .orElseThrow(() -> new ResourceNotFoundException(NARRATOR, ID, narratorId));
        Country foundCountry = null;
        if (narratorRequestDTO.getCountryId() != null) {
            foundCountry = countryRepository.findById(narratorRequestDTO.getCountryId())
                    .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, narratorRequestDTO.getCountryId()));
        }
        log.debug("Fetched country: {}", foundCountry);

        City foundCity = null;
        if (narratorRequestDTO.getCityId() != null) {
            foundCity = cityRepository.findById(narratorRequestDTO.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException(CITY, ID, narratorRequestDTO.getCityId()));
        }
        log.debug("Fetched city: {}", foundCity);

        Narrator updatedNarrator = NarratorMapper.INSTANCE.dtoToEntity(narratorRequestDTO);
        narrator.update(updatedNarrator, foundCountry, foundCity);

        narratorRepository.save(narrator);
        log.info("Narrator with ID {} updated successfully.", narratorId);
        return NarratorMapper.INSTANCE.entityToDTO(narrator);
    }

    @Transactional
    public void deleteNarratorById(Long narratorId) {
        log.info("Deleting narrator with ID: {}", narratorId);
        Narrator narrator = narratorRepository.findById(narratorId)
                .orElseThrow(() -> new ResourceNotFoundException(NARRATOR, ID, narratorId));
        narratorRepository.delete(narrator);
        log.info("Narrator with ID {} deleted successfully.", narratorId);
    }

    public GenericResponse<NarratorResponseDTO> getSearchedNarrators(String name, Long countryId, Long cityId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Getting narrators by name: {}, country ID: {}, city ID: {}, page: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                name, countryId, cityId, pageNo, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        String searchableName = name != null ? "%" + name.trim().toLowerCase().replace(" ", "%") + "%" : null;

        Page<Narrator> narrators = narratorRepository.findBySearchParams(
                searchableName, countryId, cityId, pageable);
        List<NarratorResponseDTO> content = NarratorMapper.INSTANCE.entityToDTO(narrators.getContent());
        log.info("Found {} narrators matching the params.", narrators.getTotalElements());
        return serviceUtil.createGenericResponse(narrators, content);
    }
}
