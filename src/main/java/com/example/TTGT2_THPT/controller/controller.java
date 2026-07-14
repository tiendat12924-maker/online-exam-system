package com.example.TTGT2_THPT.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@GetMapping("/index")
	public String index(Model model) {
		List<Subjects> subjects = repoSub.findByStatus(true);
		model.addAttribute("subjects", subjects);
		return "index";
	}
	
	@GetMapping("/admin")
    public String admin() {
        return "admin";
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
	public String chiTietDe(@PathVariable Integer id, Model model) {
	    Test test = repoTest.findById(id)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy đề"));
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
}
