package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.User;
import baykov.daniel.springbootlibraryapp.payload.dto.RegisterDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User registerDTOToEntity(RegisterDTO registerDTO);
}
