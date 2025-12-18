package ma.emsi.oussama.bookingservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.emsi.oussama.bookingservice.dtos.AuthResponse;
import ma.emsi.oussama.bookingservice.dtos.LoginRequest;
import ma.emsi.oussama.bookingservice.dtos.RegisterRequest;
import ma.emsi.oussama.bookingservice.dtos.UserDTO;
import ma.emsi.oussama.bookingservice.entities.User;
import ma.emsi.oussama.bookingservice.repositories.UserRepository;
import ma.emsi.oussama.bookingservice.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        @PostMapping("/register")
        public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
                // Check if email already exists
                if (userRepository.existsByEmail(request.getEmail())) {
                        return ResponseEntity
                                        .status(HttpStatus.CONFLICT)
                                        .body(Collections.singletonMap("error", "Email already registered"));
                }

                // Create new user
                User user = new User();
                user.setName(request.getName());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));

                user = userRepository.save(user);

                // Generate token
                String token = jwtService.generateToken(user);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                                AuthResponse.builder()
                                                .token(token)
                                                .userId(user.getId())
                                                .email(user.getEmail())
                                                .name(user.getName())
                                                .build());
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
                try {
                        authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getEmail(),
                                                        request.getPassword()));

                        User user = userRepository.findByEmail(request.getEmail())
                                        .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

                        String token = jwtService.generateToken(user);

                        return ResponseEntity.ok(
                                        AuthResponse.builder()
                                                        .token(token)
                                                        .userId(user.getId())
                                                        .email(user.getEmail())
                                                        .name(user.getName())
                                                        .build());
                } catch (BadCredentialsException e) {
                        return ResponseEntity
                                        .status(HttpStatus.UNAUTHORIZED)
                                        .body(Collections.singletonMap("error", "Invalid email or password"));
                }
        }

        @GetMapping("/me")
        public ResponseEntity<?> getCurrentUser() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication == null || !authentication.isAuthenticated()
                                || authentication.getPrincipal().equals("anonymousUser")) {
                        return ResponseEntity
                                        .status(HttpStatus.UNAUTHORIZED)
                                        .body(Collections.singletonMap("error", "Not authenticated"));
                }

                User user = (User) authentication.getPrincipal();

                return ResponseEntity.ok(
                                UserDTO.builder()
                                                .id(user.getId())
                                                .name(user.getName())
                                                .email(user.getEmail())
                                                .build());
        }
}
