package baykov.daniel.springbootbookstoreapp.payload.mapper;

import baykov.daniel.springbootbookstoreapp.entity.UserProfile;
import baykov.daniel.springbootbookstoreapp.payload.dto.RegisterDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.UserProfileRequestDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserProfileMapper {

    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    UserProfile registerDTOToEntity(RegisterDTO registerDTO);

    @Mapping(target = "email", ignore = true)
    UserProfile dtoToEntity(UserProfileRequestDTO userProfileRequestDTO);
}
