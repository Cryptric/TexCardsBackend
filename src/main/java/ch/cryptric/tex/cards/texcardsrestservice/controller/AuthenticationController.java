package ch.cryptric.tex.cards.texcardsrestservice.controller;

import ch.cryptric.tex.cards.texcardsrestservice.api.request.APIRegister;
import ch.cryptric.tex.cards.texcardsrestservice.model.TexCardsUser;
import ch.cryptric.tex.cards.texcardsrestservice.repository.TexCardsUserRepository;
import ch.cryptric.tex.cards.texcardsrestservice.service.JwtUserDetailsService;
import ch.cryptric.tex.cards.texcardsrestservice.util.JwtRequest;
import ch.cryptric.tex.cards.texcardsrestservice.util.JwtTokenUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {

    private enum RegisterStatus {USERNAME_TAKEN, USERNAME_INVALID, EMAIL_INVALID, PASSWORD_INVALID, OK};

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

    protected final Log logger = LogFactory.getLog(getClass());

    final TexCardsUserRepository texCardsUserRepository;
    final AuthenticationManager authenticationManager;
    final JwtUserDetailsService userDetailsService;
    final JwtTokenUtil jwtTokenUtil;

    public AuthenticationController(TexCardsUserRepository texCardsUserRepository, AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.texCardsUserRepository = texCardsUserRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody JwtRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            if (!texCardsUserRepository.existsByUserName(request.getUsername())) {
                throw new BadCredentialsException("No such user");
            }
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            if (auth.isAuthenticated()) {
                logger.info("Logged In");
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
                String token = jwtTokenUtil.generateToken(userDetails);
                responseMap.put("error", false);
                responseMap.put("message", "Logged In");
                responseMap.put("token", token);
                return ResponseEntity.ok(responseMap);
            } else {
                responseMap.put("error", true);
                responseMap.put("message", "Invalid Credentials");
                return ResponseEntity.status(401).body(responseMap);
            }
        } catch (DisabledException e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "User is disabled");
            return ResponseEntity.status(500).body(responseMap);
        } catch (BadCredentialsException e) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid Credentials");
            return ResponseEntity.status(401).body(responseMap);
        } catch (Error | Exception e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "Something went wrong");
            return ResponseEntity.status(500).body(responseMap);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody APIRegister apiRegister) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            RegisterStatus regStat = checkRegisterRequest(apiRegister);
            if (regStat == RegisterStatus.USERNAME_INVALID) {
                responseMap.put("error", true);
                responseMap.put("message", "Username invalid");
                return ResponseEntity.status(400).body(responseMap);
            } else if (regStat == RegisterStatus.USERNAME_TAKEN) {
                responseMap.put("error", true);
                responseMap.put("message", "Username already taken");
                return ResponseEntity.status(400).body(responseMap);
            } else if (regStat == RegisterStatus.EMAIL_INVALID) {
                responseMap.put("error", true);
                responseMap.put("message", "Email invalid");
                return ResponseEntity.status(400).body(responseMap);
            } else if (regStat == RegisterStatus.PASSWORD_INVALID) {
                responseMap.put("error", true);
                responseMap.put("message", "Password invalid");
                return ResponseEntity.status(400).body(responseMap);
            }
            TexCardsUser user = new TexCardsUser();
            user.setEmail(apiRegister.getEmail());
            user.setPassword(new BCryptPasswordEncoder().encode(apiRegister.getPassword()));
            user.setUserName(apiRegister.getUsername());
            UserDetails userDetails = userDetailsService.createUserDetails(apiRegister.getUsername(), user.getPassword());
            String token = jwtTokenUtil.generateToken(userDetails);
            texCardsUserRepository.save(user);
            responseMap.put("error", false);
            responseMap.put("username", apiRegister.getUsername());
            responseMap.put("message", "Account created successfully");
            responseMap.put("token", token);
            return ResponseEntity.ok(responseMap);
        } catch (Error | Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Could not parse registration request");
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

    private RegisterStatus checkRegisterRequest(APIRegister apiRegister) {
        if (apiRegister.getUsername() == null || apiRegister.getUsername().length() < 4) {
            return RegisterStatus.USERNAME_INVALID;
        } else if (texCardsUserRepository.existsByUserName(apiRegister.getUsername())) {
            return RegisterStatus.USERNAME_TAKEN;
        } else if (apiRegister.getEmail() == null || !apiRegister.getEmail().matches(EMAIL_REGEX)) {
            return RegisterStatus.EMAIL_INVALID;
        } else if (apiRegister.getPassword() == null || apiRegister.getPassword().length() < 8) {
            return RegisterStatus.PASSWORD_INVALID;
        }
        return RegisterStatus.OK;
    }
}

