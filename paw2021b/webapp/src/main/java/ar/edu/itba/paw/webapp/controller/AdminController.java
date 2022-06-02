package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.PaginatedResult;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.AdminPanelForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class AdminController {
    @Autowired
    UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @RequestMapping("/adminPanel")
    public ModelAndView adminPanel(@ModelAttribute("paginationFormAdmin") final AdminPanelForm adminForm, @ModelAttribute("paginationFormBanned") final AdminPanelForm bannedForm) {
        LOGGER.info("In /adminPanel");
        PaginatedResult<User> admins = userService.findAdmins(adminForm.getOrderA(), adminForm.getPageA());
        PaginatedResult<User> bannedUsers = userService.findBannedUsers(bannedForm.getOrder(), bannedForm.getPage());
        ModelAndView mav = new ModelAndView("/adminPanel");
        mav.addObject("admins", admins);
        mav.addObject("bannedUsers", bannedUsers);
        return mav;
    }
}
