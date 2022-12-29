package com.my1stle.customer.portal.web.controller.image;

import com.my1stle.customer.portal.service.avatar.AvatarService;
import com.my1stle.customer.portal.web.controller.settings.SettingsController;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping(value = "/image")
public class ImageController {

    private static final String AVATAR_PATH = "/avatar";

    private AvatarService avatarService;

    @Autowired
    public ImageController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping(value = AVATAR_PATH)
    @ResponseBody
    public Resource getAvatar(@AuthenticationPrincipal User currentUser) {
        return avatarService.getAvatar(currentUser);
    }

    @PostMapping(value = AVATAR_PATH)
    public RedirectView setAvatar(@AuthenticationPrincipal User currentUser,
                                  @RequestParam("avatar") MultipartFile file, HttpServletRequest request) throws IOException {
        this.avatarService.setAvatar(currentUser, file.getInputStream());
        return new RedirectView(SettingsController.BASE_PATH);

    }

}