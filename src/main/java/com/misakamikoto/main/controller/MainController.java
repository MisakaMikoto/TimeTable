package com.misakamikoto.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by MisakaMikoto on 2017-08-24.
 */
@RestController
@RequestMapping("/")
public class MainController {
    /**
     * Load model and view.
     *
     * @return the model and view
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView load() {
        return new ModelAndView("index");
    }
}
