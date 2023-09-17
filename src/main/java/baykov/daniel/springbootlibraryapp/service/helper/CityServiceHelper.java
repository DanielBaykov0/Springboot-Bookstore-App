package baykov.daniel.springbootlibraryapp.service.helper;

import baykov.daniel.springbootlibraryapp.entity.City;
import baykov.daniel.springbootlibraryapp.payload.dto.CityDTO;
import baykov.daniel.springbootlibraryapp.payload.mapper.CityMapper;
import baykov.daniel.springbootlibraryapp.payload.response.CityResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityServiceHelper {

    public CityResponse getCityResponse(Page<City> cities) {
        List<CityDTO> content = CityMapper.INSTANCE.entityToDTO(cities.getContent());

        CityResponse cityResponse = new CityResponse();
        cityResponse.setContent(content);
        cityResponse.setPageNo(cities.getNumber());
        cityResponse.setPageSize(cities.getSize());
        cityResponse.setTotalElements(cities.getTotalElements());
        cityResponse.setPageSize(cities.getSize());
        cityResponse.setLast(cities.isLast());
        return cityResponse;
    }
}
