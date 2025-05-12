package br.com.APIAuth.TOTP.mapper;

import org.mapstruct.Mapper;

import br.com.APIAuth.TOTP.dto.UserDto;
import br.com.APIAuth.TOTP.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	//Mapeamento de User para UserDto.
	UserDto toDto(User user);
	
	//Mapeamento de UserDto para User.
	User toEntity(UserDto userDto);

}
