package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.User;
import com.ncsu.gradiance.domain.UserValidator;
import com.ncsu.gradiance.service.AccessControlList;
import com.ncsu.gradiance.service.ValidateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@Controller
@RequestMapping("/welcome")
@SessionAttributes("user")
public class LoginController {

    private ValidateUser validateUser;
    private AccessControlList accessControlList;

    @Autowired
    public LoginController(ValidateUser validateUser, AccessControlList
            accessControlList){
        this.validateUser = validateUser;
        this.accessControlList = accessControlList;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String welcome( Model model) {
        model.addAttribute("user", new User());
        return "welcome";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String validate(@ModelAttribute("user") User user,
                           BindingResult result) throws SQLException {

        new UserValidator().validate(user, result);
        if( !result.hasErrors() && validateUser.isUserValid(user, result)) {
            this.accessControlList.populateACL(user, result);
            return !result.hasErrors() ? "redirect:home" : "welcome";
        }
        else  return "welcome";
    }
}
