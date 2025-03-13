package Agence.AgenceUniversiteItalie_backEnd.config;


import Agence.AgenceUniversiteItalie_backEnd.repository.UtilisateurRepository;
import Agence.AgenceUniversiteItalie_backEnd.security.CustomUserDetailsService;
import Agence.AgenceUniversiteItalie_backEnd.security.JwtAuthenticationFilter;
import Agence.AgenceUniversiteItalie_backEnd.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){return new CustomUserDetailsService();}

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, UtilisateurRepository utilisateurRepository){
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService, utilisateurRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/utilisateurs/register",
                                "/api/utilisateurs/login",
                                "/api/utilisateurs/activer-compte",
                                "/oauth2/**",
                                "/api/password/reset",
                                "/api/password/forgot"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                /*.oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/api/utilisateurs/oauth2/success")
                        .failureUrl("/api/utilisateurs/oauth2/failure")
                        .successHandler(new SimpleUrlAuthenticationSuccessHandler("/api/utilisateurs/oauth2/success"))
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler("/api/utilisateurs/oauth2/failure"))

                ) */
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:4200") // Allow frontend requests
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);
            }
        };
    }


}
