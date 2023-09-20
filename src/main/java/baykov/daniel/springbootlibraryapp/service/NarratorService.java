package baykov.daniel.springbootlibraryapp.service;

import baykov.daniel.springbootlibraryapp.entity.City;
import baykov.daniel.springbootlibraryapp.entity.Country;
import baykov.daniel.springbootlibraryapp.entity.Narrator;
import baykov.daniel.springbootlibraryapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.NarratorDTO;
import baykov.daniel.springbootlibraryapp.payload.mapper.NarratorMapper;
import baykov.daniel.springbootlibraryapp.payload.response.NarratorResponse;
import baykov.daniel.springbootlibraryapp.repository.CityRepository;
import baykov.daniel.springbootlibraryapp.repository.CountryRepository;
import baykov.daniel.springbootlibraryapp.repository.NarratorRepository;
import baykov.daniel.springbootlibraryapp.service.helper.NarratorServiceHelper;
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
public class NarratorService {

    private final NarratorRepository narratorRepository;
    private final NarratorServiceHelper narratorServiceHelper;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public NarratorDTO createNarrator(Long countryId, Long cityId, NarratorDTO narratorDTO) {
        log.info("Creating narrator...");
        Country foundCountry = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY, ID, countryId));
        City foundCity = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException(CITY, ID, cityId));

        Narrator narrator = new Narrator(
                NarratorMapper.INSTANCE.dtoToEntity(narratorDTO),
                foundCountry,
                foundCity);

        narratorRepository.save(narrator);

        log.info("Created narrator with ID: {}", narrator.getId());
        return NarratorMapper.INSTANCE.entityToDto(narrator);
    }

    public NarratorDTO getNarratorById(Long narratorId) {
        log.info("Getting narrator by ID: {}", narratorId);
        Narrator narrator = narratorRepository.findById(narratorId)
                .orElseThrow(() -> new ResourceNotFoundException(NARRATOR, ID, narratorId));
        log.info("Get narrator by ID: {}", narratorId);
        return NarratorMapper.INSTANCE.entityToDto(narrator);
    }

    public NarratorResponse getAllNarratorsByCountryId(Long countryId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving narrators by country ID: {}", countryId);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Narrator> narrators = narratorRepository.findAllByCountryId(countryId, pageable);
        log.info("Retrieved {} narrators by country ID: {}", narrators.getContent().size(), countryId);
        return narratorServiceHelper.getNarratorResponse(narrators);
    }

    public NarratorResponse getAllNarratorsByCityId(Long cityId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving narrators by city ID: {}", cityId);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Narrator> narrators = narratorRepository.findAllByCityId(cityId, pageable);
        log.info("Retrieved {} narrators by city ID: {}", narrators.getContent().size(), cityId);
        return narratorServiceHelper.getNarratorResponse(narrators);
    }

    public NarratorResponse getAllNarrators(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving narrators...");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Narrator> narrators = narratorRepository.findAll(pageable);
        log.info("Retrieved {} narrators.", narrators.getContent().size());
        return narratorServiceHelper.getNarratorResponse(narrators);
    }

    public NarratorDTO updateNarratorById(Long narratorId, NarratorDTO narratorDTO) {
        log.info("Start updating narrator with ID: {}", narratorId);
        Narrator narrator = narratorRepository.findById(narratorId)
                .orElseThrow(() -> new ResourceNotFoundException(NARRATOR, ID, narratorId));

        narrator.update(NarratorMapper.INSTANCE.dtoToEntity(narratorDTO));

        narratorRepository.save(narrator);
        log.info("Updated narrator with ID: {}", narratorId);
        return NarratorMapper.INSTANCE.entityToDto(narrator);
    }

    public void deleteNarratorById(Long narratorId) {
        log.info("Deleting narrator with ID: {}", narratorId);
        Narrator narrator = narratorRepository.findById(narratorId)
                .orElseThrow(() -> new ResourceNotFoundException(NARRATOR, ID, narratorId));
        narratorRepository.delete(narrator);
        log.info("Narrator with ID {} deleted successfully.", narratorId);
    }

    public NarratorResponse getSearchedNarrators(String name, Long countryId, Long cityId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving narrators...");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        String searchableName = name != null ? "%" + name.trim().toLowerCase().replace(" ", "%") + "%" : null;

        Page<Narrator> content = narratorRepository.findBySearchParams(
                searchableName, countryId, cityId, pageable);
        log.info("Retrieved {} narrators.", content.getTotalElements());
        return narratorServiceHelper.getNarratorResponse(content);
    }
}
