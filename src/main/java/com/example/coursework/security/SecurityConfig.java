package com.example.coursework.security;

import com.example.coursework.jwt.JwtConfig;
import com.example.coursework.jwt.TokenVerifier;
import com.example.coursework.jwt.UsernameAndPasswordAuthFilter;
import com.example.coursework.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.crypto.SecretKey;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({JwtConfig.class})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final StudentService userService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder,
                          StudentService userDetailsService,
                          SecretKey secretKey,
                          JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userDetailsService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        configuration.setAllowCredentials(true);
        //the below three lines will add the relevant CORS response headers
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /*    @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    // This wildcard pattern matches any host from domain.com and url patterns like "https:microservice.division.domain.com/version1/some_endpoint"
                    registry.addMapping("/**").allowedMethods("*").allowedOriginPatterns("https://*");
                }
            };
        }*/
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
            }
        };
    }

    /* @Override
     protected void configure(HttpSecurity http) throws Exception {
         http
                 *//* .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                 .and()*//* // logout - post!
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // можем взять метод authManager(), тк наследуемся от WebSecConfigAdapter
                // в котором такой метод есть
                .addFilter(new UsernameAndPasswordAuthFilter(authenticationManager()))
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(UserRole.STUDENT.name())
                .anyRequest()
                .authenticated();
               *//* .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses", true)
                .and()
                .rememberMe()
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)).key("secret")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // delete when csrf enable
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login");*//*
    }*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
             //   .cors().and()
                .csrf().disable()
                //  .cors().configurationSource(corsConfigurationSource())
                //.and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // .addFilter(new UsernamePasswordAuthenticationFilter(authenticationManager()))
                .addFilter(new UsernameAndPasswordAuthFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new TokenVerifier(secretKey, jwtConfig), UsernameAndPasswordAuthFilter.class)
                .authorizeRequests()
                .antMatchers("/**", "/api/students", "/internships", "index", "/css/*", "/js/*").permitAll()
                // .antMatchers("/organizations/registration").hasRole(UserRole.LEADER.name())
                //.antMatchers("/api/**").hasRole(UserRole.STUDENT.name())
                .anyRequest()
                .authenticated();
    }
    /*    .antMatchers("/management/api/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.LEADER.name())
                       .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(UserPermission.COURSE_WRITE.getPermission())
                       .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(UserPermission.COURSE_WRITE.getPermission())
                       .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(UserPermission.COURSE_WRITE.getPermission())*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
