package codewiththugboi.celowibank.controller;

import codewiththugboi.celowibank.dto.request.AuthenticationRequest;
import codewiththugboi.celowibank.dto.request.RegisterRequest;
import codewiththugboi.celowibank.dto.response.AuthenticationResponse;
import codewiththugboi.celowibank.dto.response.RegisterResponse;
import codewiththugboi.celowibank.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl service;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.login(request));
    }
    @GetMapping(path = "/confirm/{token}")
    public boolean confirm(@PathVariable String token){
        System.out.println("this is the token " + token);
        return service.enableAppUser(token);
    }
}
