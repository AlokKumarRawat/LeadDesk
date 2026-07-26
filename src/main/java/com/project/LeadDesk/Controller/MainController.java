package com.project.LeadDesk.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.LeadDesk.Dto.EnquiryDto;
import com.project.LeadDesk.Dto.UserDto;
import com.project.LeadDesk.Model.Enquiry;
import com.project.LeadDesk.Model.Enquiry.UserStatus;
import com.project.LeadDesk.Model.Users;

import com.project.LeadDesk.Model.Users.userRole;
import com.project.LeadDesk.Repository.EnquiryRepo;
import com.project.LeadDesk.Repository.UsersRepo;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	
	@Autowired
	UsersRepo usersRepo;
	@Autowired
	EnquiryRepo enquiryRepo;
	@Autowired
	HttpSession session;
	
	@GetMapping("/")
	public String ShowHome(Model model) {
		EnquiryDto dto=new EnquiryDto();
		model.addAttribute("dto", dto);
		return "index";
	}
	
	@PostMapping("/")
	public String SaveEnquiry(@ModelAttribute EnquiryDto dto,RedirectAttributes attributes) {
		try {
			Enquiry enquiry=new Enquiry();
			enquiry.setName(dto.getName());
			enquiry.setEmail(dto.getEmail());
			enquiry.setBudget(dto.getBudget());
			enquiry.setMessage(dto.getMessage());
			enquiry.setStatus(UserStatus.New);
			enquiry.setEnquiryAt(LocalDateTime.now());
			attributes.addFlashAttribute("msg", "Submitted Successfully");
			enquiryRepo.save(enquiry);
			return "redirect:/";
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Something went wrong");
			return "redirect:/";
		}
		
	}
	
	@GetMapping("/login")
	public String ShowLogin() {
		return "Login";
	}
	
	@GetMapping("/admin")
	public String ShowAdmin(Model model) {
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/login";
		}
		List<Enquiry> enquiries=enquiryRepo.findAll();
		model.addAttribute("enquiries", enquiries);
		return "Admin";
	}
	
	@GetMapping("/status/{id}/{status}")
	public String updateStatus(@PathVariable Long id,
	                           @PathVariable String status) {

	    Enquiry enquiry = enquiryRepo.findById(id).orElse(null);

	    if (enquiry != null) {
	        enquiry.setStatus(Enquiry.UserStatus.valueOf(status));
	        enquiryRepo.save(enquiry);
	    }

	    return "redirect:/viewEnquiry";
	}
	
	
	@PostMapping("/login")
	public String VerifyLogin(@ModelAttribute UserDto dto,RedirectAttributes attributes) {
		try {
			String email=dto.getEmail();
			String password=dto.getPassword();
			
			if(!usersRepo.existsByEmail(email)) {
				attributes.addFlashAttribute("msg", "User does not exist.");
				return "redirect:/login";
			}
			
			Users user=usersRepo.findByEmail(email);
			if((user.getEmail().equals(email)) && (user.getPassword().equals(password))) {
				if(user.getRole().equals(userRole.user)) {
						session.setAttribute("loggedInUser", user);
						return "redirect:/";
				}
				else if(user.getRole().equals(userRole.admin)){
					session.setAttribute("loggedInAdmin", user);
					return "redirect:/admin";
				}
			}
		}
			catch (Exception e) {
		        e.printStackTrace();
		        attributes.addFlashAttribute("msg", "Something went wrong.");
		        return "redirect:/login";
		    }

		    return "redirect:/login";
	}
	
	@GetMapping("/register")
	public String ShowRegister() {
		return "Register";
	}
	
	@PostMapping("/register")
	public String Register(@ModelAttribute UserDto dto,RedirectAttributes attributes) {
		try {
			if(usersRepo.existsByEmail(dto.getEmail())) {
				attributes.addFlashAttribute("msg", "User already exists");
				return "redirect:/login";
			}
			Users user=new Users();
			
			user.setEmail(dto.getEmail());
			user.setPassword(dto.getPassword());
			user.setRole(userRole.user);
			usersRepo.save(user);
			
			attributes.addFlashAttribute("msg", "Registration Successfull.OTP is sended to you by gmail,please veriy it");
			
			
			return "redirect:/login";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
		}
		return "redirect:/register";
	}
}
