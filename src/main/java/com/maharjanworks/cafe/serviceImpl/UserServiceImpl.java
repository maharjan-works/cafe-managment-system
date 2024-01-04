package com.maharjanworks.cafe.serviceImpl;

import com.maharjanworks.cafe.constants.CafeConstants;
import com.maharjanworks.cafe.model.User;
import com.maharjanworks.cafe.repository.UserRepository;
import com.maharjanworks.cafe.service.UserService;
import com.maharjanworks.cafe.utils.CafeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

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
                    this.logger.info("Signup Unsuccessful: {}","Email Already Exists");
                    return CafeUtils.getResponseEntity("Email Already Exists.", HttpStatus.BAD_REQUEST);
                }
            } else {
                this.logger.info("Invalid Data: {}", requestMap);
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validateSignRequest(Map<String, String> requestMap){
      if( requestMap.containsKey("firstname") && requestMap.containsKey("lastname") && requestMap.containsKey("contactNumber")
                &&requestMap.containsKey("email") && requestMap.containsKey("password")){
          return true;
      }
      return false;
    }

    private User getUserFromMap(Map<String,String> requestMap){
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
}
