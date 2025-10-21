package br.com.agrohub.demo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import br.com.agrohub.demo.repository.UserRepository;
import br.com.agrohub.demo.models.User;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return authentication -> {
			String identifier = authentication.getPrincipal() == null ? "" : authentication.getPrincipal().toString();
			String rawPassword = authentication.getCredentials() == null ? "" : authentication.getCredentials().toString();

			User user = userRepository.findByEmailOrCpfOrCnpj(identifier, identifier, identifier)
					.orElseThrow(() -> new BadCredentialsException("Usuário não encontrado"));

			if (!passwordEncoder.matches(rawPassword, user.getSenha())) {
				throw new BadCredentialsException("Senha inválida");
			}

			return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getSenha(), List.of());
		};
	}

}
