package com.Ambalika.AIMT.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Ambalika.AIMT.Dto.AdminInfoDto;
import com.Ambalika.AIMT.Dto.StudentInfoDto;
import com.Ambalika.AIMT.Repository.AdminInfoRepo;
import com.Ambalika.AIMT.Repository.StudentInfoRepo;
import com.Ambalika.AIMT.model.AdminInfo;
import com.Ambalika.AIMT.model.StudentInfo;

import jakarta.servlet.http.HttpSession;

@Controller
@CrossOrigin(origins = "*")
public class MainController {
	
	@Autowired
	AdminInfoRepo adrepo;
	
	@Autowired
	StudentInfoRepo stdrepo;
	

	@GetMapping("/")
	public String ShowIndex(Model model)
	{
		model.addAttribute("active1", "active nav-link");
		return "index";
	}
	
//	@GetMapping("/aboutus")
//	public String ShowAboutUs(Model model)
//	{
//		model.addAttribute("active2", "active nav-link");
//		return "aboutus";
//	}
	
	@GetMapping("/adminlogin")
	public String ShowAdminLogin(Model model)
	{
		AdminInfoDto dto = new AdminInfoDto();
		model.addAttribute("dto", dto);
		model.addAttribute("active3", "active nav-link");
		return "adminlogin";
	}
	
	@PostMapping("/adminlogin")
	public String AdminLogin(@ModelAttribute AdminInfoDto dto, RedirectAttributes attributes, HttpSession session)
	{
		try {
			AdminInfo adinfo = adrepo.getById(dto.getUserid());
			if(adinfo.getPassword().equals(dto.getPassword()))
			{
				session.setAttribute("admin", dto.getUserid());
				return "redirect:/admin/adhome";
			}
			else
			{
				attributes.addFlashAttribute("msg","Invalid User!");
				return "redirect:/adminlogin";
			}
			
		} catch (Exception e) {
			attributes.addFlashAttribute("msg","User does not Exists!");
			return "redirect:/adminlogin";
		}
	}
	
	    // Show Student Login Page
	    @GetMapping("/studentlogin")
	    public String showStudentLogin(Model model) {
	        model.addAttribute("dto", new StudentInfoDto());
	        model.addAttribute("active3", "active nav-link");
	        return "studentlogin"; // This should map to studentlogin.html or similar
	    }

	    // Handle Student Login Submission (from modal or form)
	    @PostMapping("/studentlogin")
	    public String studentLogin(@ModelAttribute("dto") StudentInfoDto dto, 
	                               RedirectAttributes attributes,
	                               HttpSession session) {
	        try {
	            StudentInfo stdinfo = stdrepo.getByEmail(dto.getEmail());

	            if (stdinfo != null && stdinfo.getPassword().equals(dto.getPassword())) {
	                session.setAttribute("student", dto.getEmail());
	                return "redirect:/student/stdhome";
	            } else {
	                attributes.addFlashAttribute("msg", "Invalid email or password!");
	                return "redirect:/studentlogin";
	            }

	        } catch (Exception e) {
	            attributes.addFlashAttribute("msg", "User does not exist!");
	            return "redirect:/studentlogin";
	        }
	    }
}
	
