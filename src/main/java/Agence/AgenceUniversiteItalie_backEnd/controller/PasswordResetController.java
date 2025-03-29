package Agence.AgenceUniversiteItalie_backEnd.controller;


import Agence.AgenceUniversiteItalie_backEnd.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password")
public class PasswordResetController {


    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/forgot")
    public void forgotPassword(@RequestParam String email) {
        passwordResetService.sendResetPasswordEmail(email);
    }


    @PostMapping("/reset")
    public void resetPassword(@RequestParam String token,
                              @RequestParam String newPassword,
                              @RequestParam String confirmPassword ) {
        passwordResetService.resetPassword(token, newPassword, confirmPassword);
    }
}
