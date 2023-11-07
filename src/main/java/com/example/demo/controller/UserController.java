package com.example.demo.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.repos.UserRepository;

@RestController
@RequestMapping("user")
@CrossOrigin("*")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	private String otp;
	public void sendOtpByEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("OTP Verification");
        this.otp=generateOtp();
        message.setText("Your OTP is: " + otp);
        javaMailSender.send(message);
    }

    public String generateOtp() {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }
	
	@PostMapping("/add")
	public User useradd(@RequestBody User user) {
		User newUser=new User();
		newUser.setAdmin(false);
		newUser.setEmail(user.getEmail());
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		newUser.setPassword(hashedPassword);
		userRepository.save(newUser);
		
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(newUser.getEmail());
        message.setSubject("Welcome to Your Application");
        message.setText("Hello " + newUser.getFirstName() + ",\nThank you for signing up!");
        javaMailSender.send(message);
		return newUser;
	}
	
	@GetMapping("/get")
	public boolean getAdmin(@RequestParam("email") String email,@RequestParam("password") String pass) {
		User user = userRepository.findByEmail(email);
         
        if (user != null) {
            // Use the PasswordEncoder to verify the password
        	sendOtpByEmail(email);
            return passwordEncoder.matches(pass, user.getPassword());
        }

        return false;
}
	
	
	@GetMapping("/verify")
	public boolean verify(@RequestParam("otp") String ot) {
		if(this.otp.equalsIgnoreCase(ot)) {
			return true;
		}
		return false;
		
	}
}
