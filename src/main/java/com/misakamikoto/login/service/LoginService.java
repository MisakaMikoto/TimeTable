package com.misakamikoto.login.service;

import com.misakamikoto.login.mapper.LoginMapper;
import com.misakamikoto.login.model.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Login service.
 */
@Service
public class LoginService {
    /**
     * The Login mapper.
     */
    @Autowired
    LoginMapper loginMapper;

    /**
     * Find user login vo.
     *
     * @param id the id
     * @return the login vo
     */
    public LoginVO findUser(String id) {
        return loginMapper.findUser(id);
    }

    /**
     * Create user.
     *
     * @param loginVO the login vo
     */
    @Transactional
    public void createUser(LoginVO loginVO) {
        loginMapper.createUser(loginVO);
    }
}
