package Agence.AgenceUniversiteItalie_backEnd.config;


import Agence.AgenceUniversiteItalie_backEnd.repository.UtilisateurRepository;
import Agence.AgenceUniversiteItalie_backEnd.security.CustomUserDetailsService;
import Agence.AgenceUniversiteItalie_backEnd.security.JwtAuthenticationFilter;
import Agence.AgenceUniversiteItalie_backEnd.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){return new CustomUserDetailsService();}

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, UtilisateurRepository utilisateurRepository){
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService, utilisateurRepository);
    }

    // to be continued I'm tired right now









}
