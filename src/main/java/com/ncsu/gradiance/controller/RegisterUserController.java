package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.User;
import com.ncsu.gradiance.domain.UserValidator;
import com.ncsu.gradiance.service.AccessControlList;
import com.ncsu.gradiance.service.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import static com.ncsu.gradiance.service.QueriesFactory.*;

import java.util.List;

@Controller
@RequestMapping("/register")
@SessionAttributes("user")
public class RegisterUserController {

    private RegisterUser registerUser;
    private AccessControlList accessControlList;

    @Autowired
    public RegisterUserController(RegisterUser registerUser,
                                  AccessControlList accessControlList){
        this.registerUser = registerUser;
        this.accessControlList = accessControlList;
    }



    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@ModelAttribute("user") User user, Model model){
        if(user == null) model.addAttribute("user", new User());
        return "registerUser";

    }

    @RequestMapping(method = RequestMethod.POST)
    public String register(@ModelAttribute("user") User user,
                           BindingResult result){
        new UserValidator().validate(user, result);
        if(!result.hasErrors()){
            this.registerUser.register(user, result);
            this.accessControlList.populateACL(user,result);
            return !result.hasErrors() ? "redirect:home" : "registerUser";

        }
        else return "registerUser";
    }

    @ModelAttribute("roles")
    public List<String> getRoles(){
        return this.registerUser.getRoles();
    }
}
