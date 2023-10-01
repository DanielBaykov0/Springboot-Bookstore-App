package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.UserProfile;
import baykov.daniel.springbootlibraryapp.payload.dto.RegisterDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.request.UserProfileRequestDTO;
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
