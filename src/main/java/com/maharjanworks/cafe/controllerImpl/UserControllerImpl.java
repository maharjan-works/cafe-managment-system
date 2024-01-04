package com.maharjanworks.cafe.controllerImpl;

import com.maharjanworks.cafe.constants.CafeConstants;
import com.maharjanworks.cafe.controller.UserController;
import com.maharjanworks.cafe.service.UserService;
import com.maharjanworks.cafe.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        try{
            return userService.signup(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
