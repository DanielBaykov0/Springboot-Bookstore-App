package baykov.daniel.springbootbookstoreapp.payload.mapper;

import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.payload.dto.RegisterDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.UserProfileRequestDTO;
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
