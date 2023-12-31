package baykov.daniel.springbootbookstoreapp.payload.mapper;

import baykov.daniel.springbootbookstoreapp.entity.Product;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.ProductProfileResponseDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookMapper.class, AudiobookMapper.class, EbookMapper.class})
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductProfileResponseDTO entityToDTO(Product product);

    @InheritInverseConfiguration
    Product dtoToEntity(ProductProfileResponseDTO productProfileResponseDTO);

    List<ProductProfileResponseDTO> entityToDTO(List<Product> products);

    List<Product> dtoToEntity(List<ProductProfileResponseDTO> productProfileResponseDTOS);
}
