package com.Ambalika.AIMT.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Ambalika.AIMT.API.SendEmailService;
import com.Ambalika.AIMT.Dto.QuestionBankDto;
import com.Ambalika.AIMT.Dto.StudentInfoDto;
import com.Ambalika.AIMT.Dto.TestDto;
import com.Ambalika.AIMT.Repository.AdminInfoRepo;
import com.Ambalika.AIMT.Repository.BranchRepo;
import com.Ambalika.AIMT.Repository.CourseRepo;
import com.Ambalika.AIMT.Repository.QuestionBankRepo;
import com.Ambalika.AIMT.Repository.StudentInfoRepo;
import com.Ambalika.AIMT.Repository.TestRepo;
import com.Ambalika.AIMT.Repository.TestResultRepo;
import com.Ambalika.AIMT.model.AdminInfo;
import com.Ambalika.AIMT.model.Branches;
import com.Ambalika.AIMT.model.Courses;
import com.Ambalika.AIMT.model.QuestionBank;
import com.Ambalika.AIMT.model.StudentInfo;
import com.Ambalika.AIMT.model.Test;
import com.Ambalika.AIMT.model.TestResult;
import com.opencsv.CSVReader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminInfoRepo adrepo;

	@Autowired
	StudentInfoRepo stdrepo;

	@Autowired
	private SendEmailService emailService;

	@Autowired
	QuestionBankRepo qbrepo;

	@Autowired
	TestRepo tstrepo;

	@Autowired
	TestResultRepo resultrepo;

	
	@Autowired
	CourseRepo courseRepo;

	@GetMapping("/adhome")
	public String ShowAdminDashboard(HttpSession session, RedirectAttributes attributes, Model model) {
		if (session.getAttribute("admin") != null) {
			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);
			model.addAttribute("active1", "active");

			// counters
			List<StudentInfo> astdlist = stdrepo.findStudentsByStatus("true");
			model.addAttribute("ascount", astdlist.size());
			List<StudentInfo> dstdlist = stdrepo.findStudentsByStatus("false");
			model.addAttribute("dscount", dstdlist.size());
			long qcount = qbrepo.count();
			model.addAttribute("qcount", qcount);
			long tstcount = tstrepo.count();
			model.addAttribute("tstcount", tstcount);
			long rescount = resultrepo.count();
			model.addAttribute("rescount", rescount);

			return "admin/admindashboard";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@GetMapping("/profile")
	public String ShowAdminProfile(HttpSession session, RedirectAttributes attributes, Model model) {
		if (session.getAttribute("admin") != null) {

			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);
			return "admin/profile";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@PostMapping("/profile")
	public String UploadPic(@RequestParam MultipartFile profilepic, RedirectAttributes attributes,
			HttpSession session) {

		try {
			// Get the file name
			String originalFileName = profilepic.getOriginalFilename();
			// Validate file extension
			if (originalFileName == null || !isValidImageExtension(originalFileName)) {
				attributes.addFlashAttribute("msg",
						"Invalid file type! Please upload a .jpg, .jpeg, .webp, or .png file.");
				return "redirect:/admin/profile";
			}
			// Define the storage file name
			String storageFileName = originalFileName;
			String uploadDir = "public/profile/";
			Path uploadPath = Paths.get(uploadDir);
			// Create directory if it doesn't exist
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			// Save the file
			try (InputStream inputStream = profilepic.getInputStream()) {
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
			}
			// Save the file path in the database
			AdminInfo adinfo = adrepo.findById(session.getAttribute("admin").toString()).get();
			adinfo.setProfilepic(storageFileName);
			adrepo.save(adinfo);

			attributes.addFlashAttribute("msg", "Profile Pic Uploaded Successfully");
			return "redirect:/admin/profile";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Something went wrong: " + e.getMessage());
			return "redirect:/admin/profile";
		}
	}

	// Utility method to validate file extension
	private boolean isValidImageExtension(String fileName) {
		String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		return fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("webp")
				|| fileExtension.equals("png");
	}

	@GetMapping("/scheduletest")
	public String ShowScheduleTest(HttpSession session, RedirectAttributes attributes, Model model) {
		if (session.getAttribute("admin") != null) {
			try {

				AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
				model.addAttribute("adinfo", adinfo);
				model.addAttribute("active2", "active");

				TestDto dto = new TestDto();
				model.addAttribute("dto", dto);

				List<Test> testlist = tstrepo.findAll();
				model.addAttribute("testlist", testlist);

				List<Courses> courseList = courseRepo.findAll();
				model.addAttribute("courseList", courseList);

				List<Branches> branchList = branchRepo.findAll();
				model.addAttribute("branchList", branchList);

				return "admin/scheduletest";
			} catch (Exception e) {
				attributes.addFlashAttribute("msg", e.getMessage());
				return "redirect:/admin/adhome";
			}

		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@PostMapping("/scheduletest")
	public String ScheduleTest(@ModelAttribute TestDto dto, RedirectAttributes attributes) {
		try {

			
			 List<QuestionBank> qblist = qbrepo.findByYear(dto.getYear());
			  if(qblist.size()<dto.getNumberofquestions()) {
			   attributes.addFlashAttribute("msg", "There is no Enough Question for "+dto.getYear() +", so you can't schedule the test.");
			   return "redirect:/admin/scheduletest";
			 }
			

			if (tstrepo.existsByTestname(dto.getTestname())) {
				attributes.addFlashAttribute("msg", "This Test Already Exists");
				return "redirect:/admin/scheduletest";
			}

			Test tst = new Test();
			tst.setTestname(dto.getTestname());
			tst.setCourse(dto.getCourse());
			tst.setBranch(dto.getBranch());
			 tst.setYear(dto.getYear());
			tst.setNumberofquestions(dto.getNumberofquestions());

			String starttime = dto.getStarttime();
			DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
			LocalDateTime startDateTime = LocalDateTime.parse(starttime, formatter);

			tst.setStarttime(startDateTime);
			tst.setEndtime(dto.getEndtime());
			tst.setTeststatus("Scheduled");
			tstrepo.save(tst);

			attributes.addFlashAttribute("msg", "Test Successfully Scheduled at " + startDateTime);
			return "redirect:/admin/scheduletest";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/admin/scheduletest";
		}
	}

	@GetMapping("/updatetime")
	public String ShowUpdateTestDuration(HttpSession session, RedirectAttributes attributes, Model model,
			@RequestParam("testid") long testid) {
		if (session.getAttribute("admin") != null) {

			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);

			Test test = tstrepo.findById(testid).get();
			model.addAttribute("test", test);
			return "admin/updatetime";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@PostMapping("/updatetime")
	public String UpdateDuration(@RequestParam("testid") long testid, @RequestParam("timeduration") int timeduration,
			RedirectAttributes attributes, Model model) {
		try {
			Test test = tstrepo.findById(testid).get();
			test.setEndtime(timeduration);
			tstrepo.save(test);
			attributes.addFlashAttribute("msg", "Test Duration Updated Successfully");
			return "redirect:/admin/scheduletest";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/admin/updatetime";
		}
	}

	@GetMapping("/deletetest")
	public String DeleteTest(@RequestParam long testid, RedirectAttributes attributes) {
		try {
			Test tst = tstrepo.findById(testid).get();
			tstrepo.delete(tst);
			// attributes.addFlashAttribute("msg", tst.getTestname() + " is deleted
			// successfully");
			return "redirect:/admin/scheduletest";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/admin/scheduletest";
		}

	}

	@GetMapping("/addstudents")
	public String ShowAddStudents(HttpSession session, RedirectAttributes attributes, Model model) {
		if (session.getAttribute("admin") != null) {
			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);
			model.addAttribute("active4", "active");

			StudentInfoDto dto = new StudentInfoDto();
			model.addAttribute("dto", dto);
			
			List<Courses> courseList = courseRepo.findAll();
			model.addAttribute("courseList", courseList);

			List<Branches> branchList = branchRepo.findAll();
			model.addAttribute("branchList", branchList);

			List<Test> testList = tstrepo.findByTeststatus();
			model.addAttribute("testList", testList);

			model.addAttribute("studentbtn", "student-btn");
			return "admin/addstudents";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@PostMapping("/addstudents")
	public String AddStudents(@ModelAttribute StudentInfoDto dto, HttpSession session, RedirectAttributes attributes,
			Model model) {
		System.err.println("Method ok");
		if (session.getAttribute("admin") != null) {
			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adname", adinfo.getName());

			// Check is user already exists
			StudentInfo std = stdrepo.findByEmail(dto.getEmail());

			if (std != null) {
				attributes.addFlashAttribute("msg", "User Already Exists");
				return "redirect:/admin/addstudents";
			} else {
				try {
					StudentInfo stdinfo = new StudentInfo();
					stdinfo.setTestname(dto.getTestname());
					stdinfo.setEnrollmentno(dto.getEnrollmentno());
					stdinfo.setName(dto.getName());
					stdinfo.setContactno(dto.getContactno());
					stdinfo.setEmail(dto.getEmail());
					stdinfo.setPassword(dto.getPassword());
					stdinfo.setCourse(dto.getCourse());
					stdinfo.setBranch(dto.getBranch());
					stdinfo.setYear(dto.getYear());
					stdinfo.setStatus("true");
					Date dt = new Date();
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String regdate = df.format(dt);
					stdinfo.setRegdate(regdate);
					stdrepo.save(stdinfo);

					Test test = tstrepo.findByTestname(stdinfo.getTestname());
					emailService.ConfirmUserMail(stdinfo.getName(), dto.getPassword(), stdinfo.getEmail(), test.getTestname(), test.getStarttime(), test.getEndtime());
					
					attributes.addFlashAttribute("msg", "Registration Successfull");
					return "redirect:/admin/addstudents";
				} catch (Exception e) {
					attributes.addFlashAttribute("msg", "Something went wrong " + e.getMessage());
					return "redirect:/admin/addstudents";
				}
			}
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	// CSV File Upload
	@PostMapping("/uploadstudent")
	@ResponseBody
	public Map<String, Object> handleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam String testname) {
		Map<String, Object> response = new HashMap<>();

		if (file.isEmpty()) {
			response.put("status", "error");
			response.put("message", "No file uploaded!");
			return response;
		}

		try {
			List<StudentInfo> students = new ArrayList<>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String line;
			boolean isFirstRow = true;

			while ((line = reader.readLine()) != null) {
				if (isFirstRow) {
					isFirstRow = false;
					continue; // Skip the header row
				}
				String[] data = line.split(",");

				// Parse CSV data into StudentInfo object
				StudentInfo student = new StudentInfo();

				student.setTestname(testname);
				student.setEnrollmentno(data[0]);
				student.setName(data[1]);
				student.setEmail(data[2]); // Email is at index 2
				student.setPassword("Password@123"); //

				// Parse other fields
				student.setContactno(data[3]);;
				student.setCourse(data[6]);
				student.setBranch(data[7]);
				student.setYear(data[8]);

				student.setStatus("true");

				// Handle the profilepic (optional)
				if (data.length > 16) {
					student.setProfilepic(data[16]);
				}

				Date dt = new Date();
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				String regdate = df.format(dt);
				student.setRegdate(regdate);
				students.add(student);
				// emailService.ConfirmUserMail(data[1], "Password@123", data[2]);
			}

			Test test = tstrepo.findByTestname(testname);
			// Save students to the database
			for (StudentInfo student : students) {
				// Check if student already exists by email
				if (stdrepo.findByEmail(student.getEmail()) == null) {
					stdrepo.save(student); // Save the student to the database
					emailService.ConfirmUserMail(student.getName(), "Password@123", student.getEmail(), testname, test.getStarttime(), test.getEndtime());
				}
			}

			response.put("status", "success");
			response.put("message", "File uploaded and processed successfully!");

		} catch (IOException e) {
			response.put("status", "error");
			response.put("message", "Error reading the file: " + e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/allowedstd", method = { RequestMethod.GET, RequestMethod.POST })
	public String showAllowedStudent(HttpSession session, RedirectAttributes attributes, Model model,
	        HttpServletRequest request) {
	    if (session.getAttribute("admin") != null) {
	        // Fetch Admin Information
	        String adminId = session.getAttribute("admin").toString();
	        AdminInfo adinfo = adrepo.getById(adminId);
	        model.addAttribute("adinfo", adinfo);
	        model.addAttribute("active21", "pcoded-hasmenu active pcoded-trigger");
	        model.addAttribute("active5", "active");

	        // Fetch All Students for Filters
	        List<StudentInfo> allstdList = stdrepo.findAll();

	        // Populate Filters

	        List<String> Courses = allstdList.stream().map(StudentInfo::getCourse).distinct()
	                .collect(Collectors.toList());
	        model.addAttribute("Courses", Courses);

	        List<String> Branches = allstdList.stream().map(StudentInfo::getBranch).distinct()
	                .collect(Collectors.toList());
	        model.addAttribute("Branches", Branches);

	        List<Test> testList = tstrepo.findAll();
	        List<String> testNames = testList.stream().map(Test::getTestname).distinct().collect(Collectors.toList());
	        model.addAttribute("testNames", testNames);

	        // Check HTTP Method
	        if ("POST".equalsIgnoreCase(request.getMethod())) {
	            // Fetch Request Parameters
	            String testname = request.getParameter("testname");
	            String course = request.getParameter("course");
	            String branch = request.getParameter("branch");

	            // Convert Empty Strings to Null
	            testname = (testname == null || testname.trim().isEmpty()) ? null : testname;
	            course = (course == null || course.trim().isEmpty()) ? null : course;
	            branch = (branch == null || branch.trim().isEmpty()) ? null : branch;

	            // Fetch Filtered Data
	            List<StudentInfo> stdlist = stdrepo.findByDropDownSelect(testname, course, branch, "true");

	            // Store Results in RedirectAttributes
	            attributes.addFlashAttribute("stdlist", stdlist);

	            // Redirect to the GET method
	            return "redirect:/admin/allowedstd";
	        } else {
	            // Retrieve Filtered Data for GET Request
	            List<StudentInfo> stdlist = (List<StudentInfo>) model.getAttribute("stdlist");
	            model.addAttribute("stdlist", stdlist);
	        }

	        return "admin/allowedstudent";
	    } else {
	        // Handle Expired Session
	        attributes.addFlashAttribute("msg", "Session Expired!");
	        return "redirect:/adminlogin";
	    }
	}

	

	@GetMapping("/deletestudent")
	public String DeleteStudent(@RequestParam String email, RedirectAttributes attributes) {
		StudentInfo stdinfo = stdrepo.findByEmail(email);
		stdrepo.delete(stdinfo);
		return "redirect:/admin/allowedstd";
	}

	@GetMapping("/editstudent")
	public String ShowEditStudent(@RequestParam String email, HttpSession session, RedirectAttributes attributes,
			Model model) {
		if (session.getAttribute("admin") != null) {
			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);

			StudentInfo stdinfo = stdrepo.findByEmail(email);
			model.addAttribute("stdinfo", stdinfo);
			

			List<Courses> courseList = courseRepo.findAll();
			model.addAttribute("courseList", courseList);

			List<Branches> branchList = branchRepo.findAll();
			model.addAttribute("branchList", branchList);

			List<Test> testList = tstrepo.findByTeststatus();
			model.addAttribute("testList", testList);

			
			return "admin/editstddetails";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@PostMapping("/updatestudent")
	public String EditStudent(@ModelAttribute StudentInfo oldstd, RedirectAttributes attributes) {
		System.err.println(oldstd.getEmail());
		System.err.println(oldstd.getEmail());
		try {
			StudentInfo stdinfo = stdrepo.findByEmail(oldstd.getEmail().toString());
			if (stdinfo != null) {
				stdinfo.setEnrollmentno(oldstd.getEnrollmentno());
				stdinfo.setName(oldstd.getName());
				stdinfo.setEmail(oldstd.getEmail());
				stdinfo.setContactno(oldstd.getContactno());
				stdinfo.setCourse(oldstd.getCourse());
				stdinfo.setBranch(oldstd.getBranch());
				stdinfo.setYear(oldstd.getYear());
				stdrepo.save(stdinfo);
				attributes.addFlashAttribute("msg", oldstd.getName() + " Details Upadated Successfully.");
			}

			return "redirect:/admin/allowedstd";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/admin/allowedstd";
		}
	}

	@GetMapping("/checkstatus")
	public String CheckStatus(@RequestParam String email, RedirectAttributes redirectAttributes) {
		StudentInfo stdinfo = stdrepo.findByEmail(email);

		if (stdinfo.getStatus().equals("false")) {
			stdinfo.setStatus("true");
			stdrepo.save(stdinfo);
			 String name = stdinfo.getName();
			 String pass = stdinfo.getPassword();
			emailService.ConfirmUserMail(name, pass, email, pass, null, 0);
			return "redirect:/admin/disabledstd";
		} else {
			stdinfo.setStatus("false");
			stdrepo.save(stdinfo);
			return "redirect:/admin/allowedstd";
		}
	}

	@GetMapping("/viewmore")
	public String ShowViewMore(@RequestParam String email, Model model, HttpSession session,
			RedirectAttributes attributes) {
		if (session.getAttribute("admin") != null) {
			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);
			try {
				StudentInfo stdinfo = stdrepo.getByEmail(email);
				model.addAttribute("stdinfo", stdinfo);
				return "admin/viewmorestd";
			} catch (Exception e) {
				attributes.addFlashAttribute("msg", e.getMessage());
				return "redirect:/admin/manageresult";
			}
			
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	// this is for view all (question, testname, testresult)
	@GetMapping("/viewall")
	public String ShowViewAll(Model model, HttpSession session, RedirectAttributes attributes) {
		if (session.getAttribute("admin") != null) {
			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);
			model.addAttribute("active7", "active");
			List<Test> testList = tstrepo.findAll();
			model.addAttribute("testList", testList);

			return "admin/viewall";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@GetMapping("/seeresult")
	public String ShowSeeResult(HttpSession session, RedirectAttributes attributes, Model model,
			HttpServletRequest request) {
		if (session.getAttribute("admin") != null) {
			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);

			List<Test> testNames = tstrepo.findTestname();
			model.addAttribute("testNames", testNames);

			String testname = request.getParameter("testname");
			if (testname != null) {
				List<TestResult> trlist = resultrepo.findResultByTestname(testname);
				model.addAttribute("trlist", trlist);
				model.addAttribute("stdcount", trlist.size());
				return "admin/seeresult";

			} else {
				attributes.addFlashAttribute("msg", "Something went wrong!");
				return "redirect:/admin/viewall";
			}

		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@GetMapping("/manageresult")
	public String ShowAllResult(HttpSession session, RedirectAttributes attributes, Model model,
			HttpServletRequest request) {
		if (session.getAttribute("admin") != null) {

			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);
			model.addAttribute("active14", "active");

			List<Test> testList = tstrepo.findAll();
			model.addAttribute("testList", testList);

			String testname = request.getParameter("testname");
			if (testname != null) {
				List<TestResult> trlist = resultrepo.findResultByTestname(testname);
				model.addAttribute("trlist", trlist);
				return "admin/manageresult";
			} else {
				List<TestResult> trlist = resultrepo.findAll();
				model.addAttribute("trlist", trlist);
				
				return "admin/manageresult";
			}

		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@GetMapping("/deleteresult")
	public String DeleteStudentResult(@RequestParam long id, RedirectAttributes attributes) {
		try {
			TestResult result = resultrepo.findById(id).get();
			resultrepo.delete(result);
			return "redirect:/admin/manageresult";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Something went wrong " + e.getMessage());
			return "redirect:/admin/manageresult";
		}
	}

	
	// Upload Question Through CSV File
	@PostMapping("/uploadquestion")
	public ResponseEntity<String> uploadQuestion(@RequestParam("qbfile") MultipartFile file,
			@RequestParam("testname") String testname) {
		System.err.println("Question CSV File");
		try {
			Reader reader = new InputStreamReader(file.getInputStream());
			CSVReader csvReader = new CSVReader(reader);
			List<String[]> rows = csvReader.readAll();
			rows.remove(0);

			List<QuestionBank> questionBanks = new ArrayList<>();

			for (String[] row : rows) {
				QuestionBank qb = new QuestionBank();

				qb.setTestname(testname);
				qb.setQuestion(row[0]);
				qb.setA(row[1]);
				qb.setB(row[2]);
				qb.setC(row[3]);
				qb.setD(row[4]);
				qb.setCorrect(row[5]);
				qb.setYear(row[6]);
				qb.setBranch(row[7]);
				qb.setCourse(row[8]);
				
				questionBanks.add(qb);
			}
			qbrepo.saveAll(questionBanks);
			return ResponseEntity.ok(questionBanks.size() + " Question Uploaded SuccessFully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Failed to Upload Question " + e.getMessage());
		}
	}

	@GetMapping("/addquestion")
	public String ShowAddQuestion(HttpSession session, RedirectAttributes attributes, Model model) {
		if (session.getAttribute("admin") != null) {
			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);
			model.addAttribute("active22", "pcoded-hasmenu active pcoded-trigger");
			model.addAttribute("active8", "active");

			QuestionBankDto qbdto = new QuestionBankDto();
			model.addAttribute("qbdto", qbdto);

			model.addAttribute("questionbtn", "question-btn");

			List<Test> testList = tstrepo.findByTeststatus();
			model.addAttribute("testList", testList);

			return "admin/addquestion";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@PostMapping("/addquestion")
	public String AddQuestion(@ModelAttribute QuestionBankDto bankDto, RedirectAttributes attributes) {
		try {
			QuestionBank qb = new QuestionBank();
			qb.setTestname(bankDto.getTestname());
			qb.setYear(bankDto.getYear());
			qb.setCourse(bankDto.getCourse());
			qb.setBranch(bankDto.getBranch());
			qb.setQuestion(bankDto.getQuestion());
			qb.setA(bankDto.getA());
			qb.setB(bankDto.getB());
			qb.setC(bankDto.getC());
			qb.setD(bankDto.getD());
			qb.setCorrect(bankDto.getCorrect());
			qbrepo.save(qb);
			attributes.addFlashAttribute("msg", "Question Uploaded Successfully");
			return "redirect:/admin/addquestion";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Something went wrong " + e.getMessage());
			return "redirect:/admin/addquestion";
		}
	}

	@GetMapping("/managequestionbank")
	public String ShowAllQuestion(HttpSession session, RedirectAttributes attributes, Model model,
			HttpServletRequest request) {
		if (session.getAttribute("admin") != null) {

			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);
			model.addAttribute("active13", "active");

			List<Test> testList = tstrepo.findAll();
			model.addAttribute("testList", testList);

			String testname = request.getParameter("testname");
			if (testname != null) {
				List<QuestionBank> qblist = qbrepo.findQuestionByTestname(testname);
				model.addAttribute("qblist", qblist);
				return "admin/managequestionbank";
			} else {
				List<QuestionBank> qblist = qbrepo.findAll();
				model.addAttribute("qblist", qblist);
				return "admin/managequestionbank";
			}

		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@GetMapping("/questionbank")
	public String ShowManageQuestionBank(HttpSession session, RedirectAttributes attributes, Model model,
			HttpServletRequest request) {
		if (session.getAttribute("admin") != null) {
			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);
			model.addAttribute("active22", "pcoded-hasmenu active pcoded-trigger");
			model.addAttribute("active9", "active");

			List<Test> testNames = tstrepo.findTestname();
			model.addAttribute("testNames", testNames);

			String testname = request.getParameter("testname");
			if (testname != null) {

				List<QuestionBank> qblist = qbrepo.findQuestionByTestname(testname);
				model.addAttribute("qblist", qblist);
				model.addAttribute("questioncount", qblist.size());
				return "admin/seequestion";

			} else {
				attributes.addFlashAttribute("msg", "Something went wrong!");
				return "redirect:/admin/viewall";
			}

		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@GetMapping("/deleteallquestion")
	public String DeleteQuestion(@RequestParam("testname") String testname, RedirectAttributes attributes) {
		try {

			List<QuestionBank> qbList = qbrepo.findQuestionByTestname(testname);
			qbrepo.deleteAll(qbList);
			attributes.addFlashAttribute("msg", testname+" Questions Deleted Successfully");
			return "redirect:/admin/viewall";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Something went wrong " + e.getMessage());
			return "redirect:/admin/viewall";
		}
	}

	@GetMapping("/changepassword")
	public String ShowChangePassword(HttpSession session, RedirectAttributes attributes, Model model) {
		if (session.getAttribute("admin") != null) {
			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);
			return "admin/changepassword";

		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@PostMapping("/changepassword")
	public String ChangePassword(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {
		try {
			AdminInfo adinfo = adrepo.findById(session.getAttribute("admin").toString()).get();
			String oldpass = request.getParameter("oldpass");
			String newpass = request.getParameter("newpass");
			String confirmpass = request.getParameter("confirmpass");

			if (newpass.equals(confirmpass)) {
				if (oldpass.equals(adinfo.getPassword())) {
					adinfo.setPassword(confirmpass);
					adrepo.save(adinfo);
					session.invalidate();
					return "redirect:/adminlogin";
				} else {
					attributes.addFlashAttribute("msg", "Invalid Old Password ⚠️");
				}
			} else {
				attributes.addFlashAttribute("msg", "New Password and Confirm Password Does not Matched!");
			}
			return "redirect:/admin/changepassword";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/admin/changepassword";
		}
	}



	@GetMapping("/managecourse")
	public String ShowManageCourses(HttpSession session, RedirectAttributes attributes, Model model) {
		if (session.getAttribute("admin") != null) {
			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);

			model.addAttribute("active11", "active");
			List<Courses> courseList = courseRepo.findAll();
			model.addAttribute("courseList", courseList);
			return "admin/managecourse";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@PostMapping("/addcourses")
	public String AddCourse(@RequestParam("coursename") String course, RedirectAttributes attributes) {
		try {

			if (courseRepo.existsByCoursename(course)) { // Note: Method matches repository definition
				attributes.addFlashAttribute("msg", "This Course already exists!");
				return "redirect:/admin/managecourse";
			}

			Courses obj = new Courses();
			obj.setCoursename(course);
			courseRepo.save(obj);
			attributes.addFlashAttribute("msg", "Course added Successfully");
			return "redirect:/admin/managecourse";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/admin/managecourse";
		}
	}

	@GetMapping("/deletecourse")
	public String DeleteCourse(@RequestParam long courseid, RedirectAttributes attributes) {
		try {
			Courses course = courseRepo.findById(courseid).get();
			courseRepo.delete(course);
			return "redirect:/admin/managecourse";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Something went wrong " + e.getMessage());
			return "redirect:/admin/managecourse";
		}
	}

	@GetMapping("/managebranches")
	public String ShowManageBranches(HttpSession session, RedirectAttributes attributes, Model model) {
		if (session.getAttribute("admin") != null) {
			AdminInfo adinfo = adrepo.getById(session.getAttribute("admin").toString());
			model.addAttribute("adinfo", adinfo);

			model.addAttribute("active12", "active");
			List<Branches> branchList = branchRepo.findAll();
			model.addAttribute("branchList", branchList);
			return "admin/managebranches";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

	@Autowired
	private BranchRepo branchRepo;

	@PostMapping("/addbranches")
	public String AddBranch(@RequestParam("branchname") String branch, RedirectAttributes attributes) {
		try {
			if (branchRepo.existsByBranchname(branch)) {
				attributes.addFlashAttribute("msg", "This branch already exists");
				return "redirect:/admin/managebranches";
			}

			Branches obj = new Branches();
			obj.setBranchname(branch);
			branchRepo.save(obj);
			attributes.addFlashAttribute("msg", "Branch added successfully");
			return "redirect:/admin/managebranches";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/admin/managebranches";
		}
	}

	@GetMapping("/deletebranch")
	public String DeleteBranch(@RequestParam long branchid, RedirectAttributes attributes) {
		try {
			Branches branch = branchRepo.findById(branchid).get();
			branchRepo.delete(branch);
			return "redirect:/admin/managebranches";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Something went wrong " + e.getMessage());
			return "redirect:/admin/managebranches";
		}
	}

	@GetMapping("/adlogout")
	public String Logout(HttpSession session, RedirectAttributes attributes, HttpServletResponse response) {
		if (session.getAttribute("admin") != null) {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			session.invalidate();
			attributes.addFlashAttribute("logoutmsg", "Successfully Logout!");
			return "redirect:/adminlogin";
		} else {
			attributes.addFlashAttribute("msg", "Session Expired!");
			return "redirect:/adminlogin";
		}
	}

}
