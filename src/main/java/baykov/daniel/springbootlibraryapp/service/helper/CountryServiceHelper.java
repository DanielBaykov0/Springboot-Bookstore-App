package baykov.daniel.springbootlibraryapp.service.helper;

import baykov.daniel.springbootlibraryapp.entity.Country;
import baykov.daniel.springbootlibraryapp.payload.dto.CountryDTO;
import baykov.daniel.springbootlibraryapp.payload.mapper.CountryMapper;
import baykov.daniel.springbootlibraryapp.payload.response.CountryResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountryServiceHelper {

    public CountryResponse getCountryResponse(Page<Country> countries) {
        List<CountryDTO> content = CountryMapper.INSTANCE.entityToDTO(countries.getContent());

        CountryResponse countryResponse = new CountryResponse();
        countryResponse.setContent(content);
        countryResponse.setPageNo(countries.getNumber());
        countryResponse.setPageSize(countries.getSize());
        countryResponse.setTotalElements(countries.getTotalElements());
        countryResponse.setPageSize(countries.getSize());
        countryResponse.setLast(countries.isLast());
        return countryResponse;
    }
}
