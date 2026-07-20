package com.example.TTGT2_THPT.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.TTGT2_THPT.entity.Answers;
import com.example.TTGT2_THPT.entity.Questions;
import com.example.TTGT2_THPT.entity.QuestionsGroup;
import com.example.TTGT2_THPT.entity.QuestionsType;
import com.example.TTGT2_THPT.entity.Role;
import com.example.TTGT2_THPT.entity.Subjects;
import com.example.TTGT2_THPT.entity.Test;
import com.example.TTGT2_THPT.entity.User;
import com.example.TTGT2_THPT.repository.RepositoryAnswer;
import com.example.TTGT2_THPT.repository.RepositoryQuestion;
import com.example.TTGT2_THPT.repository.RepositoryQuestionGroup;
import com.example.TTGT2_THPT.repository.RepositorySubject;
import com.example.TTGT2_THPT.repository.RepositoryTest;
import com.example.TTGT2_THPT.repository.RepositoryUser;
import com.example.TTGT2_THPT.service.ServiceSubject;
import com.example.TTGT2_THPT.service.ServiceTest;
import com.example.TTGT2_THPT.entity.TestAttempts;
import com.example.TTGT2_THPT.entity.UserAnswers;
import com.example.TTGT2_THPT.entity.AttemptStatus;
import com.example.TTGT2_THPT.repository.RepositoryTestAttempt;
import com.example.TTGT2_THPT.repository.RepositoryUserAnswer;
import java.time.Duration;
import com.example.TTGT2_THPT.service.ServiceUser;

import jakarta.servlet.http.HttpSession;

@Controller
public class controller {
	@Autowired
	ServiceUser svUser;
	@Autowired
	RepositoryUser repoUser;
	@Autowired
	RepositorySubject repoSub;
	@Autowired
	ServiceSubject svSubject;
	@Autowired
	RepositoryTest repoTest;
	@Autowired
	RepositoryAnswer repoAnswer;
	@Autowired
	RepositoryQuestion repoQuestion;
	@Autowired
	RepositoryQuestionGroup repoGroup;
	@Autowired
	ServiceTest svTest;
	@Autowired
	RepositoryTestAttempt repoAttempt;
	@Autowired
	RepositoryUserAnswer repoUserAnswer;
	@GetMapping("/")
	public String home() {
		return "redirect:/index";
	}
	@GetMapping("/index")
	public String index(Model model) {
		List<Subjects> subjects = repoSub.findByStatus(true);
		model.addAttribute("subjects", subjects);
		return "index";
	}	
	@GetMapping("/admin")
    public String admin(Model model) {
        List<Test> tests = repoTest.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
        model.addAttribute("tests", tests);      
        long totalTests = repoTest.count();
        model.addAttribute("totalTests", totalTests);        
        long totalUsers = repoUser.count();
        model.addAttribute("totalUsers", totalUsers);        
        List<Subjects> subjects = repoSub.findAll();
        model.addAttribute("subjects", subjects);       
        List<User> users = repoUser.findAll();
        model.addAttribute("users", users);     
        long totalQuestions = repoQuestion.count();
        model.addAttribute("totalQuestions", totalQuestions);     
        Map<Long, Long> testCounts = tests.stream()
            .filter(t -> t.getSubject() != null)
            .collect(Collectors.groupingBy(t -> t.getSubject().getId(), Collectors.counting()));
        model.addAttribute("testCounts", testCounts);      
        return "admin";
    }
	@PostMapping("/admin/api/add-subject")
    @ResponseBody
    public ResponseEntity<?> addSubject(@RequestBody Subjects subject) {
        if (subject.getName() == null || subject.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên môn học không được để trống");
        }
        if (subject.getCode() == null || subject.getCode().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mã môn học không được để trống");
        }
        if (subject.getDescription() == null || subject.getDescription().trim().isEmpty()) {
            subject.setDescription("Môn học " + subject.getName());
        }
        if (repoSub.findByCode(subject.getCode()).isPresent()) {
            return ResponseEntity.badRequest().body("Mã môn học đã tồn tại");
        }       
        boolean existsByName = repoSub.findAll().stream().anyMatch(s -> s.getName().equalsIgnoreCase(subject.getName()));
        if (existsByName) {
            return ResponseEntity.badRequest().body("Tên môn học đã tồn tại");
        }
        repoSub.save(subject);
        return ResponseEntity.ok("Thêm môn học thành công");
    }
	@PostMapping("/reviewdethi/{id}")
	public String reviewDeThi(@PathVariable Integer id, @RequestParam Map<String, String> allParams, Model model) {
	    Test test = repoTest.findById(id)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy đề"));	    
	    Map<String, Long> selectedAnswers = new java.util.HashMap<>();
	    for (Map.Entry<String, String> entry : allParams.entrySet()) {
	        if (entry.getKey().startsWith("question_")) {
	            try {
	                String questionIdStr = entry.getKey().replace("question_", "");
	                Long answerId = Long.parseLong(entry.getValue());
	                selectedAnswers.put(questionIdStr, answerId);
	            } catch (NumberFormatException e) {
	            }
	        }
	    }
	    model.addAttribute("test", test);
	    model.addAttribute("selectedAnswers", selectedAnswers);
	    return "review";
	}
	@PostMapping("/submitdethi/{id}")
	public String submitDeThi(@PathVariable Integer id, @RequestParam Map<String, String> allParams, Model model, HttpSession session) {
	    User user = (User) session.getAttribute("user");
	    if (user == null) {
	        return "redirect:/login";
	    }
	    Test test = repoTest.findById(id)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy đề"));
	    int correctCount = 0;
	    int wrongCount = 0;
	    Map<String, Boolean> questionResults = new java.util.HashMap<>();
	    Map<String, Long> selectedAnswers = new java.util.HashMap<>();	    
	    for (Questions question : test.getQuestions()) {
	        String paramValue = allParams.get("question_" + question.getId());
	        String qIdStr = String.valueOf(question.getId());
	        if (paramValue != null && !paramValue.trim().isEmpty()) {
	            try {
	                Long selectedAnsId = Long.parseLong(paramValue);
	                selectedAnswers.put(qIdStr, selectedAnsId);               
	                boolean isCorrect = false;
	                for (Answers ans : question.getAnswers()) {
	                    if (ans.getId().equals(selectedAnsId) && ans.getIsCorrect()) {
	                        isCorrect = true;
	                        break;
	                    }
	                }	             
	                if (isCorrect) {
	                    correctCount++;
	                    questionResults.put(qIdStr, true);
	                } else {
	                    wrongCount++;
	                    questionResults.put(qIdStr, false);
	                }
	            } catch (NumberFormatException e) {
	                wrongCount++;
	                questionResults.put(qIdStr, false);
	            }
	        } else {
	            wrongCount++;
	            questionResults.put(qIdStr, false);
	        }
	    }
	    double score = 0.0;
	    if (test.getQuestions() != null && !test.getQuestions().isEmpty()) {
	        score = (correctCount * 10.0) / test.getQuestions().size();
	        score = Math.round(score * 100.0) / 100.0;
	    }

	    LocalDateTime submittedAt = LocalDateTime.now();
	    LocalDateTime startedAt = (LocalDateTime) session.getAttribute("exam_start_time_" + id);
	    if (startedAt == null) {
	        startedAt = submittedAt.minusMinutes(test.getDuration() != null ? test.getDuration() : 45);
	    }
	    int timeSpent = (int) Duration.between(startedAt, submittedAt).toSeconds();

	    TestAttempts attempt = new TestAttempts();
	    attempt.setUser(user);
	    attempt.setTest(test);
	    attempt.setCorrectCount(correctCount);
	    attempt.setWrongCount(wrongCount);
	    attempt.setScore(score);
	    attempt.setTimeSpent(timeSpent);
	    attempt.setStartedAt(startedAt);
	    attempt.setSubmittedAt(submittedAt);
	    attempt.setStatus(AttemptStatus.SUBMITTED);
	    TestAttempts savedAttempt = repoAttempt.save(attempt);

	    for (Questions question : test.getQuestions()) {
	        String paramValue = allParams.get("question_" + question.getId());
	        if (paramValue != null && !paramValue.trim().isEmpty()) {
	            try {
	                Long selectedAnsId = Long.parseLong(paramValue);
	                UserAnswers uAns = new UserAnswers();
	                uAns.setAttempt(savedAttempt);
	                uAns.setQuestion(question);
	                Answers dbAns = repoAnswer.findById(selectedAnsId).orElse(null);
	                uAns.setAnswer(dbAns);
	                uAns.setIsCorrect(dbAns != null && Boolean.TRUE.equals(dbAns.getIsCorrect()));
	                repoUserAnswer.save(uAns);
	            } catch (NumberFormatException e) {
	            }
	        }
	    }
	    session.removeAttribute("exam_start_time_" + id);

	    model.addAttribute("test", test);
	    model.addAttribute("correctCount", correctCount);
	    model.addAttribute("wrongCount", wrongCount);
	    model.addAttribute("score", score);
	    model.addAttribute("questionResults", questionResults);
	    model.addAttribute("selectedAnswers", selectedAnswers);
	    return "ketqua";
	}
	@GetMapping("/login")
	public String showLogin(Model model) {
		model.addAttribute("mode", "email");
	    return "login";
	}
	@PostMapping("/login")
	public String doLogin(@RequestParam String password,
	                      Model model,	                      
	                      HttpSession session) {
	    String email = (String) session.getAttribute("email");
	    if (email == null) {
	        return "redirect:/login";
	    }
	    User user = repoUser.findByEmail(email);
	    if (!user.getPassword().equals(password)) {
	        model.addAttribute("email", email);
	        model.addAttribute("mode", "login");
	        model.addAttribute("error", "Sai mật khẩu");
	        return "login";
	    }
	    session.setAttribute("user", user);
	    System.out.println("Role ID = " + user.getRole().getId());
	    if (user.getRole().getId().equals(1L)) {
	        System.out.println("===> ADMIN");
	        return "redirect:/admin";
	    }
	    System.out.println("===> USER");
	    return "redirect:/index";
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate();
	    return "redirect:/index";
	}
	@GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("mode", "email");
        return "login";
    }
	@PostMapping("/check-email")
	public String checkEmail(@RequestParam String email,
	                         Model model,
	                         HttpSession session) {
	    System.out.println("EMAIL: " + email);
	    session.setAttribute("email", email);
	    User user = repoUser.findByEmail(email);
	    model.addAttribute("email", email);
	    if (user != null) {
	        model.addAttribute("mode", "login");
	    } else {
	        model.addAttribute("mode", "register");
	        model.addAttribute("user", new User());
	    }
	    return "login";
	}
	@PostMapping("/register")
	public String register(@ModelAttribute User user,
	                       @RequestParam("confirmPassword") String confirmPassword,	                     
	                       Model model) {
	    if (!user.getPassword().equals(confirmPassword)) {
	        model.addAttribute("error", "Mật khẩu nhập lại không khớp!");
	        model.addAttribute("mode", "register");
	        return "login";
	    }
	    Role role = new Role();
	    role.setId(3L);
	    user.setRole(role);
	    svUser.save(user);
	    return "redirect:/index";
	}
	@GetMapping("/trangdethi")
	public String trangDeThi(@RequestParam("subjectId") Long subjectId, Model model, Long id) {
	    List<Subjects> subjects = svSubject.findAll();
	    model.addAttribute("subjects", subjects);
	    model.addAttribute("activeSubjectId", subjectId);
	    id = subjectId;
	    Subjects subject = repoSub.findById(id)
	            .orElseThrow(() -> new RuntimeException("Subject not found"));
	    model.addAttribute("subject", subject);
	    List<Test> tests = repoTest.findBySubjectId(subjectId);
	    model.addAttribute("tests", tests);
	    return "trangdethi";
	}
	@GetMapping("/chitietde/{id}")
	public String chiTietDe(@PathVariable Integer id, Model model, HttpSession session) {
	    User user = (User) session.getAttribute("user");
	    if (user == null) {
	        return "redirect:/login";
	    }
	    Test test = repoTest.findById(id)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy đề"));
	    session.setAttribute("exam_start_time_" + id, LocalDateTime.now());
	    model.addAttribute("test", test);
	    return "chitietde";
	}
	@GetMapping("/uploadtest")
    public String uploadTest() {
        return "uploadtest";
    }
	@PostMapping("/uploadtest")
    public String saveTest(@ModelAttribute Test test) {
        LocalDateTime now = LocalDateTime.now();
        test.setYear(LocalDate.now().getYear());
        test.setType("Đề DOV soạn");
        test.setCreatedAt(now);
        test.setUpdatedAt(now);
        test.setCreatedBy(1);
        test.setImage("");
        Test savedTest = repoTest.save(test);
        return "redirect:/uploaddethi/" + savedTest.getId();
    }
	@GetMapping("/uploaddethi/{id}")
	public String uploadDeThi(
	        @PathVariable Integer id,
	        Model model) {
	    Test test = repoTest.findById(id)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy đề"));
	    List<QuestionsGroup> groups = repoGroup.findByTestId(id);
	    model.addAttribute("groups", groups);
	    model.addAttribute("test", test);
	    model.addAttribute("questionCount", repoQuestion.countByTest(test)
	    );
	    return "uploaddethi";
	}
	@PostMapping("/admin/question/save")
	public String saveQuestion(
						        @RequestParam Integer testId,
						        @RequestParam String questionType,
						        @RequestParam(required = false) String content,
						        @RequestParam(required = false) String answerA,
						        @RequestParam(required = false) String answerB,
						        @RequestParam(required = false) String answerC,
						        @RequestParam(required = false) String answerD,
						        @RequestParam(required = false) String correctAnswer,
						        @RequestParam(required = false) String passage,
						        @RequestParam(required = false) List<String> questionContent,
						        @RequestParam(required = false) List<String> groupAnswerA,
						        @RequestParam(required = false) List<String> groupAnswerB,
						        @RequestParam(required = false) List<String> groupAnswerC,
						        @RequestParam(required = false) List<String> groupAnswerD,
						        @RequestParam(required = false) List<String> groupCorrectAnswer
							  ){
	    Test test = repoTest.findById(testId)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy đề"));
	    QuestionsType type = QuestionsType.valueOf(questionType);
	    if (type == QuestionsType.SINGLE) {
	        Questions question = new Questions();
	        question.setContent(content);
	        question.setQuestionType(type);
	        question.setTest(test);
	        repoQuestion.save(question);
	        saveAnswer(question, "A", answerA, correctAnswer);
	        saveAnswer(question, "B", answerB, correctAnswer);
	        saveAnswer(question, "C", answerC, correctAnswer);
	        saveAnswer(question, "D", answerD, correctAnswer);
	    }
	    else if (type == QuestionsType.MULTIPLE) {
	        QuestionsGroup group = new QuestionsGroup();
	        group.setTitle("Nhóm câu hỏi");
	        group.setInstruction("Đọc đoạn văn và trả lời các câu hỏi sau.");
	        group.setPassage(passage);
	        group.setTestId(test.getId().longValue());
	        repoGroup.save(group);
	        for (int i = 0; i < questionContent.size(); i++) {
	            Questions question = new Questions();
	            question.setContent(questionContent.get(i));
	            question.setQuestionType(type);
	            question.setQuestionOrder(i + 1);
	            question.setGroup(group);
	            question.setTest(test);
	            repoQuestion.save(question);
	            saveAnswer(question,
	                    "A",
	                    groupAnswerA.get(i),
	                    groupCorrectAnswer.get(i));
	            saveAnswer(question,
	                    "B",
	                    groupAnswerB.get(i),
	                    groupCorrectAnswer.get(i));
	            saveAnswer(question,
	                    "C",
	                    groupAnswerC.get(i),
	                    groupCorrectAnswer.get(i));
	            saveAnswer(question,
	                    "D",
	                    groupAnswerD.get(i),
	                    groupCorrectAnswer.get(i));
	        }
	    }
	    else if (type == QuestionsType.ESSAY) {
	        Questions question = new Questions();
	        question.setContent(content);
	        question.setQuestionType(type);
	        question.setTest(test);
	        repoQuestion.save(question);
	    }
	    return "redirect:/uploaddethi/" + testId;
	}
	private void saveAnswer( Questions question,
					        String label,
					        String content,
					        String correctAnswer) {
	    Answers answer = new Answers();
	    answer.setQuestion(question);
	    answer.setLabel(label);
	    answer.setContent(content);
	    answer.setIsCorrect(label.equals(correctAnswer));
	    switch (label) {
	        case "A":
	            answer.setAnswerOrder(1);
	            break;
	        case "B":
	            answer.setAnswerOrder(2);
	            break;
	        case "C":
	            answer.setAnswerOrder(3);
	            break;
	        case "D":
	            answer.setAnswerOrder(4);
	            break;
	    }
	    repoAnswer.save(answer);
	}
	@GetMapping("/forgot-password")
	public String showForgotPassword(HttpSession session,
	                                 Model model){
	    String email = (String) session.getAttribute("email");
	    model.addAttribute("email", email);
	    model.addAttribute("mode", "ForgotPassword");
	    return "login";
	}
	
	@PostMapping("/forgot-password")
	public String forgotPassword(HttpSession session,
	                             Model model) {
	    String email = (String) session.getAttribute("email");
	    if(email == null){
	        return "redirect:/login";
	    }
	    User user = repoUser.findByEmail(email);
	    if(user == null){
	        model.addAttribute("mode","register");
	        model.addAttribute("email",email);
	        return "login";
	    }
	    String code = String.format("%06d",
	            new Random().nextInt(1000000));
	    user.setResetCode(code);
	    user.setResetExpired( LocalDateTime.now().plusMinutes(5));
	    repoUser.save(user);
	    System.out.println();
	    System.out.println("========== OTP ==========");
	    System.out.println("Email : " + email);
	    System.out.println("Code  : " + code);
	    System.out.println("=========================");
	    System.out.println();
	    model.addAttribute("mode","verify");
	    model.addAttribute("email",email);
	    return "login";
	}
	
	@PostMapping("/verify-code")
	public String verifyCode(@RequestParam String code,
	                         HttpSession session,
	                         Model model){
	    String email=(String)session.getAttribute("email");
	    User user=repoUser.findByEmail(email);
	    if(user==null){
	        return "redirect:/login";
	    }
	    if(!code.equals(user.getResetCode())){
	        model.addAttribute("mode","verify");
	        model.addAttribute("demoCode",user.getResetCode());
	        model.addAttribute("email",email);
	        model.addAttribute("error","Sai mã xác nhận");
	        return "login";
	    }
	    if(user.getResetExpired().isBefore(LocalDateTime.now())){
	        model.addAttribute("mode","verify");
	        model.addAttribute("demoCode",user.getResetCode());
	        model.addAttribute("email",email);
	        model.addAttribute("error","Mã đã hết hạn");
	        return "login";
	    }
	    model.addAttribute("mode","reset");
	    model.addAttribute("email",email);
	    return "login";
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam String password,
	                            HttpSession session){

	    String email=(String)session.getAttribute("email");

	    User user=repoUser.findByEmail(email);

	    user.setPassword(password);

	    user.setResetCode(null);

	    user.setResetExpired(null);

	    repoUser.save(user);

	    session.removeAttribute("email");

	    return "redirect:/login";
	}

	@GetMapping("/profile")
	public String showProfile(HttpSession session, Model model) {
	    User user = (User) session.getAttribute("user");
	    if (user == null) {
	        return "redirect:/login";
	    }
	    List<TestAttempts> attempts = repoAttempt.findByUserOrderBySubmittedAtDesc(user);
	    model.addAttribute("user", user);
	    model.addAttribute("attempts", attempts);
	    return "profile";
	}

	@GetMapping("/attempt/{attemptId}/review")
	public String reviewAttemptDetail(@PathVariable Long attemptId, HttpSession session, Model model) {
	    User user = (User) session.getAttribute("user");
	    if (user == null) {
	        return "redirect:/login";
	    }
	    TestAttempts attempt = repoAttempt.findById(attemptId)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy lượt thi"));
	    
	    if (!attempt.getUser().getId().equals(user.getId())) {
	        throw new RuntimeException("Bạn không có quyền truy cập kết quả này");
	    }
	    
	    List<UserAnswers> userAnswers = repoUserAnswer.findByAttempt(attempt);
	    Map<Integer, UserAnswers> userAnswersMap = userAnswers.stream()
	            .collect(Collectors.toMap(ua -> ua.getQuestion().getId(), ua -> ua));
	    
	    model.addAttribute("attempt", attempt);
	    model.addAttribute("test", attempt.getTest());
	    model.addAttribute("userAnswersMap", userAnswersMap);
	    return "attempt_review";
	}
}
