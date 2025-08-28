package com.Ambalika.AIMT.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Ambalika.AIMT.Dto.StudentInfoDto;
import com.Ambalika.AIMT.Repository.QuestionBankRepo;
import com.Ambalika.AIMT.Repository.StudentInfoRepo;
import com.Ambalika.AIMT.Repository.TestRepo;
import com.Ambalika.AIMT.Repository.TestResultRepo;
import com.Ambalika.AIMT.model.AdminInfo;
import com.Ambalika.AIMT.model.QuestionBank;
import com.Ambalika.AIMT.model.StudentInfo;
import com.Ambalika.AIMT.model.Test;
import com.Ambalika.AIMT.model.TestResult;
import com.google.gson.Gson;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	StudentInfoRepo stdrepo;

	@Autowired
	TestRepo tstrepo;

	@Autowired
	QuestionBankRepo qbrepo;

	@Autowired
	TestResultRepo resultrepo;

	@Autowired
	private EntityManager entityManager;

	private StudentInfoDto stdinfo;

	@GetMapping("/stdhome")
	public String StudentDashboard(HttpSession session, RedirectAttributes attributes, Model model,
			HttpServletResponse response) {

		if (session.getAttribute("student") != null) {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

			StudentInfo stdinfo = stdrepo.getByEmail(session.getAttribute("student").toString());
			model.addAttribute("stdinfo", stdinfo);
			model.addAttribute("active1", "active");
			StudentInfoDto dto = new StudentInfoDto();
			model.addAttribute("dto", dto);
			return "student/studentdashboard"; // Points to the studentdashboard.html or JSP
		} else {
			attributes.addFlashAttribute("msg", "Session Expired! Please login again.");
			return "redirect:/studentlogin";
		}
	}

	@GetMapping("/profile")
	public String ShowProfilePic(HttpSession session, RedirectAttributes attributes, Model model,
			HttpServletResponse response) {
		if (session.getAttribute("student") != null) {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

			StudentInfo stdinfo = stdrepo.getByEmail(session.getAttribute("student").toString());
			model.addAttribute("stdinfo", stdinfo);
			return "student/profile";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/studentlogin";
		}
	}

	@GetMapping("/viewprofile")
	public String ShowViewProfile(HttpSession session, RedirectAttributes attributes, Model model,
			HttpServletResponse response) {
		if (session.getAttribute("student") != null) {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

			StudentInfo stdinfo = stdrepo.getByEmail(session.getAttribute("student").toString());
			model.addAttribute("stdinfo", stdinfo);
			return "student/viewprofile";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/studentlogin";
		}
	}

	@PostMapping("/profile")
	public String UploadPic(@RequestParam MultipartFile profilepic, RedirectAttributes attributes,
			HttpSession session) {

		try {
			// Get the file name of the new profile picture
			String originalFileName = profilepic.getOriginalFilename();

			// Validate file extension
			if (originalFileName == null || !isValidImageExtension(originalFileName)) {
				attributes.addFlashAttribute("msg", "Invalid file type! Please upload a .jpg, .jpeg, .webp, or .png file.");
				return "redirect:/student/profile";
			}

			// Define the storage file name
			String storageFileName = UUID.randomUUID().toString() + "_" + originalFileName;
			String uploadDir = "public/profile/";
			Path uploadPath = Paths.get(uploadDir);

			// Create directory if it doesn't exist
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// Get the student information from the database
			StudentInfo stdinfo = stdrepo.getByEmail(session.getAttribute("student").toString());
			
			// Delete the old profile picture if it exists
			String oldProfilePic = stdinfo.getProfilepic();
			if (oldProfilePic != null && !oldProfilePic.isEmpty()) {
				Path oldFilePath = Paths.get(uploadDir + oldProfilePic);
				if (Files.exists(oldFilePath)) {
					System.err.println(oldProfilePic);
					Files.delete(oldFilePath);
				}
			}

			// Save the new profile picture
			try (InputStream inputStream = profilepic.getInputStream()) {
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
			}

			// Update the profile picture in the database
			stdinfo.setProfilepic(storageFileName);
			stdrepo.save(stdinfo);

			attributes.addFlashAttribute("msg", "Profile Pic Uploaded Successfully");
			return "redirect:/student/profile";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Something went wrong: " + e.getMessage());
			return "redirect:/student/profile";
		}
	}

	// Utility method to validate file extension
	private boolean isValidImageExtension(String fileName) {
		String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		return fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("webp")
				|| fileExtension.equals("png");
	}

	@GetMapping("/givetest")
	public String ShowGiveTest(HttpSession session, RedirectAttributes attributes, Model model,
	                           HttpServletResponse response,
	                           @ModelAttribute("message") String message) {

	    if (session.getAttribute("student") != null) {
	        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

	        StudentInfo stdinfo = stdrepo.getByEmail(session.getAttribute("student").toString());
	        model.addAttribute("stdinfo", stdinfo);
	        model.addAttribute("active2", "active");

	        // ✅ Add this to pass the message to Thymeleaf
	        model.addAttribute("message", message);

	        return "student/givetest";
	    } else {
	        attributes.addFlashAttribute("msg", "Session Expired! Please login again.");
	        return "redirect:/studentlogin";
	    }
	}
	@GetMapping("/starttest")
	public String StartTest(HttpSession session, RedirectAttributes attributes, Model model,
	        HttpServletResponse response) {

	    if (session.getAttribute("student") != null) {
	        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

	        StudentInfo stdinfo = stdrepo.getByEmail(session.getAttribute("student").toString());
	        model.addAttribute("stdinfo", stdinfo);

	        String stdinresult = resultrepo.getSttudentOfStdResult(stdinfo.getEmail(), stdinfo.getTestname());
	        Test tst = tstrepo.findByTestname(stdinfo.getTestname());

	        try {
	            if (stdinresult != null) {
	                attributes.addFlashAttribute("msg", "You have already given the test.");
	                return "redirect:/student/givetest";
	            } else {
	                if (tst.getTeststatus().equals("Active")) {
	                    if (qbrepo.existsByTestname(stdinfo.getTestname())) {
	                        int numberOfQuestion = tst.getNumberofquestions();
	                        List<QuestionBank> qblist = qbrepo.findQuestionByTestname(stdinfo.getTestname(),
	                                numberOfQuestion, entityManager);

	                        if (qblist.isEmpty()) {
	                            attributes.addFlashAttribute("message", "false");
	                            attributes.addFlashAttribute("msg",
	                                    "There is no Question found!, please contact our team.");
	                            return "redirect:/student/givetest";
	                        }

	                        Gson gson = new Gson();
	                        String json = gson.toJson(qblist);
	                        model.addAttribute("json", json);
	                        model.addAttribute("tt", qblist.size() / 2);
	                        model.addAttribute("tq", qblist.size());
	                        model.addAttribute("testname", tst.getTestname());
	                        model.addAttribute("message", "true"); // ✅ Add this to enable Start Test
	                        return "student/starttest";
	                    } else {
	                        attributes.addFlashAttribute("message", "false");
	                        attributes.addFlashAttribute("msg",
	                                "There is no Question found!, please contact our team.");
	                        return "redirect:/student/givetest";
	                    }
	                } else if (tst.getTeststatus().equals("Test Over")) {
	                    attributes.addFlashAttribute("msg", "The Test has been ended now.");
	                    return "redirect:/student/givetest";
	                } else {
	                    attributes.addFlashAttribute("msg", "The test has not started yet, please wait!");
	                    return "redirect:/student/givetest";
	                }
	            }
	        } catch (Exception e) {
	            System.err.println("Error processing test: " + e.getMessage());
	            return "redirect:/student/givetest";
	        }
	    } else {
	        attributes.addFlashAttribute("msg", "Session Expired! Please login again.");
	        return "redirect:/studentlogin";
	    }
	}

	
	
	@GetMapping("/testover")
	public String ShowTestOver(HttpSession session, RedirectAttributes attributes, Model model,
			HttpServletResponse response) {

		if (session.getAttribute("student") != null) {
			StudentInfo stdinfo = stdrepo.getByEmail(session.getAttribute("student").toString());
			model.addAttribute("stdinfo", stdinfo);
			return "student/testover"; // Points to the studentdashboard.html or JSP
		} else {
			attributes.addFlashAttribute("msg", "Session Expired! Please login again.");
			return "redirect:/studentlogin";
		}
	}

	@PostMapping("/testover")
	public String TestOver(HttpSession session, Model model, HttpServletResponse response, @RequestParam("s") int s,
			@RequestParam("t") int t, RedirectAttributes attributes) {
		if (session.getAttribute("student") != null) {
			try {

				StudentInfo si = stdrepo.getByEmail(session.getAttribute("student").toString());

				TestResult result = new TestResult();
				result.setEmail(si.getEmail());
				result.setName(si.getName());
				result.setCourse(si.getCourse());
				result.setBranch(si.getBranch());
				result.setContactno(si.getContactno());
				result.setYear(si.getYear());
				result.setTotalmarks(t);
				result.setGetmarks(s);
				//
				Test tstinfo = tstrepo.findByTestname(si.getTestname());
				result.setTestname(tstinfo.getTestname());
				result.setStatus("true");

				resultrepo.save(result);
				return "redirect:/student/testover";
			} catch (Exception e) {
				attributes.addFlashAttribute("msg", "Something went wrong test not submitted!");
				return "redirect:/student/givetest";
			}
		} else {
			return "redirect:/studentlogin";
		}
	}

	// Method for course Catalogue
	@GetMapping("/courses")
	public String ShowCourseCatalogue(HttpSession session, RedirectAttributes attributes, Model model,
			HttpServletResponse response) {

		if (session.getAttribute("student") != null) {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

			StudentInfo stdinfo = stdrepo.getByEmail(session.getAttribute("student").toString());
			model.addAttribute("stdinfo", stdinfo);
			model.addAttribute("active3", "active");
			return "student/coursecatalogue"; // Points to the studentdashboard.html or JSP
		} else {
			attributes.addFlashAttribute("msg", "Session Expired! Please login again.");
			return "redirect:/studentlogin";
		}
	}

	// method for training videos
	

	// method for see result
	
	@GetMapping("/result")
	public String showResult(HttpSession session,
	                         RedirectAttributes attributes,
	                         Model model,
	                         HttpServletResponse response) {

	    Object studentIdObj = session.getAttribute("student");

	    if (studentIdObj != null) {
	        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

	        String studentId = studentIdObj.toString();

	        // Use actual studentId
	        StudentInfo stdinfo = stdrepo.getByEmail(studentId);
	        model.addAttribute("stdinfo", stdinfo);
	        model.addAttribute("active5", "active");

	        Optional<TestResult> optionalResult = resultrepo.findByEmail(studentId);
	        model.addAttribute("result", optionalResult.orElse(null));

	        return "student/result";
	    } else {
	        attributes.addFlashAttribute("msg", "Session Expired! Please login again.");
	        return "redirect:/studentlogin";
	    }
	}

	@GetMapping("/changepassword")
	public String ShowChangePassword(HttpSession session, RedirectAttributes attributes, Model model,
			HttpServletResponse response) {

		if (session.getAttribute("student") != null) {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

			StudentInfo stdinfo = stdrepo.getByEmail(session.getAttribute("student").toString());
			model.addAttribute("stdinfo", stdinfo);

			return "student/changepassword";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired! Please login again.");
			return "redirect:/studentlogin";
		}
	}

	@PostMapping("/changepassword")
	public String ChangePassword(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {
		try {
			StudentInfo stdinfo = stdrepo.getByEmail(session.getAttribute("student").toString());
			String oldpass = request.getParameter("oldpass");
			String newpass = request.getParameter("newpass");
			String confirmpass = request.getParameter("confirmpass");

			if (newpass.equals(confirmpass)) {
				if (oldpass.equals(stdinfo.getPassword())) {
					stdinfo.setPassword(confirmpass);
					stdrepo.save(stdinfo);
					session.invalidate();
					return "redirect:/";
				} else {
					attributes.addFlashAttribute("msg", "Invalid Old Password ⚠️");
				}
			} else {
				attributes.addFlashAttribute("msg", "New Password and Confirm Password Does not Matched!");
			}
			return "redirect:/student/changepassword";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/student/changepassword";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes attributes) {
		session.invalidate(); // Clear the session
		attributes.addFlashAttribute("msg", "You have been logged out successfully.");
		return "redirect:/studentlogin";
	}

}
