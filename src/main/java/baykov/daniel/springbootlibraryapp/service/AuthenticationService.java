package baykov.daniel.springbootlibraryapp.service;

import baykov.daniel.springbootlibraryapp.payload.dto.LoginDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.RegisterDTO;

public interface AuthenticationService {

    String login(LoginDTO loginDTO);

    String register(RegisterDTO registerDTO);
}
