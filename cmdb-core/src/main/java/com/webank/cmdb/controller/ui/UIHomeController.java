package com.webank.cmdb.controller.ui;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webank.cmdb.controller.ui.helper.UIUserManagerService;
import com.webank.cmdb.exception.CmdbException;

@Controller
public class UIHomeController {

    @Autowired
    private UIUserManagerService userManagerService;
    @Value("${cmdb.security.enabled}")
    boolean securityEnabled;

    @GetMapping(value = { "/", "index.html" })
    public String index() {
        return "index.html";
    }

    @GetMapping(value = { "/home" })
    public String home(Model model, Principal principal) {
        model.addAttribute("system_name", "CMDB Core");
        model.addAttribute("login_user", principal.getName());
        return "home.html";
    }

    @GetMapping(value = { "login-privacy-free.html" })
    public String loginPrivacyFreePage() {
        return "login-privacy-free.html";
    }

    @GetMapping(value = { "login-with-password.html" })
    public String loginWithPasswordPage() {
        return "login-with-password.html";
    }

    @GetMapping("/ui/v2/my-menus")
    @ResponseBody
    public Object retrieveRoleMenus(Principal principal) {
        if (principal == null) {
            if (!securityEnabled) {
                principal = (Principal) new User("admin", null, false, false, false, false, null);
            } else {
                throw new CmdbException("Logon user not found.");
            }

        }
        return userManagerService.getMenuDtosByUsername(principal.getName(), true);
    }
}