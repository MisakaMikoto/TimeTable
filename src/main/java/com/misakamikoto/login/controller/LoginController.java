package com.misakamikoto.login.controller;

import com.misakamikoto.login.model.LoginVO;
import com.misakamikoto.login.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by MisakaMikoto on 2017-08-23.
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * The Login service.
     */
    @Autowired
    LoginService loginService;

    @RequestMapping(method = RequestMethod.POST)
    public boolean login(@RequestParam("id") String id, @RequestParam("password") String password) {
        logger.info("method : login, param(id: " + id + ")" + " and param(password: "+ password + ")");
        boolean isLogin = false;

        try {
            LoginVO loginVO = loginService.findUser(id);
            if(loginVO.getPassword().equals(password)) {
                isLogin = true;
            } else {
                isLogin = false;
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return isLogin;
    }

    /**
     * Find user boolean.
     *
     * @param id the id
     * @return the boolean
     */
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public boolean findUser(@RequestParam("id") String id) {
        logger.info("method : findUser, param(id: " + id + ")");
        boolean isFind = false;

        try {
            LoginVO loginVO = loginService.findUser(id);

            if (loginVO != null) {
                isFind = true;

            } else {
                isFind = false;
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return isFind;
    }

    /**
     * Create user boolean.
     *
     * @param id       the id
     * @param password the password
     * @return the boolean
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public boolean createUser(@RequestParam("id") String id, @RequestParam("password") String password) {
        logger.info("method : createUser, param(id: " + id + ")" + " and param(password: "+ password + ")");

        LoginVO loginVO = new LoginVO();
        loginVO.setId(id);
        loginVO.setPassword(password);

        boolean isCreated = false;
        try {
            loginService.createUser(loginVO);
            isCreated = true;

        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        return isCreated;
    }
}