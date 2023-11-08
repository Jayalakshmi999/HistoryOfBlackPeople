package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.AdminUser;
import com.example.demo.entities.User;
import com.example.demo.repos.AdminRepository;
import com.example.demo.repos.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@RestController
@CrossOrigin("*")
public class AdminController {
@Autowired	
private AdminRepository adminRepository;

@Autowired
private JavaMailSender javaMailSender;

@Autowired
private UserRepository userRepository;

@Autowired
private PasswordEncoder passwordEncoder;

private String otp;

@GetMapping("/sendotp")
public void sendOtpByEmail(@RequestParam("email") String email) {
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

@GetMapping("/add")
public String addAdmin(@RequestParam("email") String email) {
	
	

	
	 MimeMessage message = javaMailSender.createMimeMessage();
     MimeMessageHelper helper = new MimeMessageHelper(message);
	User luser=userRepository.findByEmail(email);
	
	String baseUrl = "https://app-qc1f.onrender.com";
	String token = luser.getEmail();
	boolean condition =luser.isAdmin() ;

	String confirmationUrl = "https://app-3bdk.onrender.com" + "/confirm-action?token=" +token+  "&condition=" + condition;
	
	if(luser!=null)
	{
		try {
            helper.setTo(email);
            helper.setSubject("Admin Access Request");
            helper.setText("Your admin access request has been approved. To accept, click the following link: "+ confirmationUrl, true);

            javaMailSender.send(message);
            return "sent";
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle the exception
            
            return "exceptio";
        }
	}
	
		return "not acces";
		
	
}

@GetMapping("/confirm-action")
public ResponseEntity<String> confirmAction(@RequestParam("token") String token,@RequestParam("condition") boolean condition) {
	User user=userRepository.findByEmail(token);
	User us=userRepository.findBy_id(user.get_id());
	us.setAdmin(true);
	
	userRepository.save(us);
	AdminUser adminUser=new AdminUser();
	adminUser.setEmail(user.getEmail());
	adminUser.setPassword(user.getPassword());
	adminRepository.save(adminUser);
	String htmlContent = "<html><body><h1>Hi, Access Granted</h1></body></html>";
    return ResponseEntity.ok().body(htmlContent);
	
}

@GetMapping("/get")
public boolean getAdmin(@RequestParam("email") String email,@RequestParam("password") String pass) {
	AdminUser adminuser = adminRepository.findByEmail(email);
    
    if (adminuser != null) {
        // Use the PasswordEncoder to verify the password
    	sendOtpByEmail(email);
        return passwordEncoder.matches(pass, adminuser.getPassword());
    }

    return false;
}


@GetMapping("/adminverify")
public boolean verify(@RequestParam("otp") String ot) {
	if(this.otp.equalsIgnoreCase(ot)) {
		return true;
	}
	return false;
	
}

@GetMapping("/allusers")

public List<User> allusers()
{
	List<User> users=new ArrayList<>();
	  
	users=userRepository.findAll();
	
	return users;
	
}


@DeleteMapping("/deleteadmin/{email}")
public String deleteadmin(@PathVariable String email)
{
	User use=userRepository.findByEmail(email);
	User us=userRepository.findBy_id(use.get_id());
	
	AdminUser user=adminRepository.findByEmail(email);
	if(user!=null) {
		us.setAdmin(false);
		userRepository.save(us);
		adminRepository.delete(user);
		return "Admin deleted";
	}
	
	return "Not found admin";
	
}





}

