package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.User;
import baykov.daniel.springbootlibraryapp.payload.dto.RegisterDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.request.UserProfileRequestDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User registerDTOToEntity(RegisterDTO registerDTO);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isEmailVerified", ignore = true)
    User dtoToEntity(UserProfileRequestDTO userProfileRequestDTO);
}
