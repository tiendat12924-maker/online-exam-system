package com.example.TTGT2_THPT.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.TTGT2_THPT.dto.ExcelMappingDTO;
import com.example.TTGT2_THPT.entity.Difficulty;
import com.example.TTGT2_THPT.entity.Subjects;
import com.example.TTGT2_THPT.entity.Test;
import com.example.TTGT2_THPT.repository.RepositorySubject;
import com.example.TTGT2_THPT.repository.RepositoryTest;
import com.example.TTGT2_THPT.service.ExcelImportService;

@RestController
@RequestMapping("/api/excel")
public class ExcelImportController {

    @Autowired
    private ExcelImportService excelImportService;

    @Autowired
    private RepositoryTest repoTest;

    @Autowired
    private RepositorySubject repoSub;

    /**
     * API tạo nhanh đề thi phục vụ luồng import Excel
     */
    @PostMapping("/create-test")
    public ResponseEntity<?> createTest(
            @RequestParam("title") String title,
            @RequestParam("subjectId") Long subjectId,
            @RequestParam(value = "duration", required = false) Integer duration,
            @RequestParam("difficulty") String difficulty) {
        try {
            Subjects subject = repoSub.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học với ID: " + subjectId));

            Test test = new Test();
            test.setTitle(title);
            test.setSubject(subject);
            test.setDuration(duration != null ? duration : 45);
            test.setDifficulty(Difficulty.valueOf(difficulty));
            test.setYear(java.time.LocalDate.now().getYear());
            test.setType("Đề Excel Import");
            test.setImage("");
            test.setCreatedBy(1); // Mặc định là admin

            Test savedTest = repoTest.save(test);
            return ResponseEntity.ok(Map.of("success", true, "testId", savedTest.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tạo đề thi: " + e.getMessage());
        }
    }

    /**
     * API đọc danh sách các cột tiêu đề của file Excel
     */
    @PostMapping("/read-headers")
    public ResponseEntity<?> readHeaders(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File không được trống.");
        }
        try {
            List<String> headers = excelImportService.getExcelHeaders(file);
            return ResponseEntity.ok(headers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi đọc file Excel: " + e.getMessage());
        }
    }

    /**
     * API thực hiện Import câu hỏi
     */
    @PostMapping("/import")
    public ResponseEntity<?> importQuestions(
            @RequestParam("file") MultipartFile file,
            @RequestParam("testId") Integer testId,
            @RequestParam("questionContentCol") String questionContentCol,
            @RequestParam(value = "passageCol", required = false) String passageCol,
            @RequestParam(value = "instructionCol", required = false) String instructionCol,
            @RequestParam("answerACol") String answerACol,
            @RequestParam("answerBCol") String answerBCol,
            @RequestParam("answerCCol") String answerCCol,
            @RequestParam("answerDCol") String answerDCol,
            @RequestParam("correctAnswerCol") String correctAnswerCol) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File không được trống.");
        }

        try {
            ExcelMappingDTO mapping = new ExcelMappingDTO();
            mapping.setQuestionContentCol(questionContentCol);
            mapping.setPassageCol(passageCol);
            mapping.setInstructionCol(instructionCol);
            mapping.setAnswerACol(answerACol);
            mapping.setAnswerBCol(answerBCol);
            mapping.setAnswerCCol(answerCCol);
            mapping.setAnswerDCol(answerDCol);
            mapping.setCorrectAnswerCol(correctAnswerCol);

            excelImportService.importExcel(file, testId, mapping);
            return ResponseEntity.ok(Map.of("success", true, "message", "Import câu hỏi thành công!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi import dữ liệu: " + e.getMessage());
        }
    }
}
