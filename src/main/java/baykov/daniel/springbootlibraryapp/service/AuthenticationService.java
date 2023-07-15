package baykov.daniel.springbootlibraryapp.service;

import baykov.daniel.springbootlibraryapp.payload.dto.ChangePasswordDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.ForgotPasswordDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.LoginDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.RegisterDTO;

public interface AuthenticationService {

    String login(LoginDTO loginDTO);

    String register(RegisterDTO registerDTO);

    String changePassword(ChangePasswordDTO changePasswordDTO, Long userId);

    String forgotPassword(ForgotPasswordDTO forgotPasswordDTO);

    String resetPassword(ForgotPasswordDTO forgotPasswordDTO, String token);
}
