package com.misakamikoto.login.model;

import java.io.Serializable;

/**
 * Created by MisakaMikoto on 2017-08-24.
 */
public class LoginVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The Id.
     */
    String id;
    /**
     * The Password.
     */
    String password;

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
