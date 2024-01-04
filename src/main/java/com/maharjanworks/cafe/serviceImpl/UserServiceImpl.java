package com.maharjanworks.cafe.serviceImpl;

import com.maharjanworks.cafe.constants.CafeConstants;
import com.maharjanworks.cafe.jwt.CustomerUserDetailsService;
import com.maharjanworks.cafe.jwt.JwtFilter;
import com.maharjanworks.cafe.jwt.JwtUtils;
import com.maharjanworks.cafe.model.User;
import com.maharjanworks.cafe.repository.UserRepository;
import com.maharjanworks.cafe.service.UserService;
import com.maharjanworks.cafe.utils.CafeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        this.logger.info("Inside signup {}", requestMap);
        try {
            if (validateSignRequest(requestMap)) {
                User user = userRepository.findByEmail(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    this.userRepository.save(this.getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity(CafeConstants.SIGNUP_SUCCESSFULLY, HttpStatus.CREATED);
                } else {
                    this.logger.info("Signup Unsuccessful: {}", "Email Already Exists");
                    return CafeUtils.getResponseEntity("Email Already Exists.", HttpStatus.BAD_REQUEST);
                }
            } else {
                this.logger.info("Invalid Data: {}", requestMap);
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private boolean validateSignRequest(Map<String, String> requestMap) {
        if (requestMap.containsKey("firstname") && requestMap.containsKey("lastname") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setFirstname(requestMap.get("firstname"));
        user.setLastname(requestMap.get("lastname"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }


    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        this.logger.info("Inside login: {}", requestMap);
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtils.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                                    customerUserDetailsService.getUserDetail().getRole()) + "\"}",
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Wait for Admin Approval." + "\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            this.logger.error("{}", ex);
        }
        return new ResponseEntity<String>("{\"message\":\"" + "Bad Credentials." + "\"}",
                HttpStatus.BAD_REQUEST);
    }



}
