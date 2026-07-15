package com.example.TTGT2_THPT.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.TTGT2_THPT.dto.ExcelMappingDTO;
import com.example.TTGT2_THPT.entity.Answers;
import com.example.TTGT2_THPT.entity.Questions;
import com.example.TTGT2_THPT.entity.QuestionsGroup;
import com.example.TTGT2_THPT.entity.QuestionsType;
import com.example.TTGT2_THPT.entity.Test;
import com.example.TTGT2_THPT.repository.RepositoryAnswer;
import com.example.TTGT2_THPT.repository.RepositoryQuestion;
import com.example.TTGT2_THPT.repository.RepositoryQuestionGroup;
import com.example.TTGT2_THPT.repository.RepositoryTest;

@Service
public class ExcelImportService {

    @Autowired
    private RepositoryTest repoTest;

    @Autowired
    private RepositoryQuestion repoQuestion;

    @Autowired
    private RepositoryQuestionGroup repoGroup;

    @Autowired
    private RepositoryAnswer repoAnswer;

    /**
     * Đọc các tiêu đề cột (Headers) từ dòng đầu tiên của file Excel
     */
    public List<String> getExcelHeaders(MultipartFile file) throws IOException {
        List<String> headers = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            if (headerRow != null) {
                DataFormatter formatter = new DataFormatter();
                for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                    Cell cell = headerRow.getCell(i);
                    if (cell != null) {
                        String headerVal = formatter.formatCellValue(cell).trim();
                        if (!headerVal.isEmpty()) {
                            headers.add(headerVal);
                        }
                    }
                }
            }
        }
        return headers;
    }

    /**
     * Import câu hỏi và đáp án từ file Excel vào database dựa trên cấu hình mapping
     */
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(MultipartFile file, Integer testId, ExcelMappingDTO mapping) throws Exception {
        Test test = repoTest.findById(testId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đề thi với ID: " + testId));

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("File Excel trống hoặc không có dòng tiêu đề.");
            }

            // Xác định index các cột dựa trên tên cột ánh xạ
            int questionContentIdx = -1;
            int passageIdx = -1;
            int instructionIdx = -1;
            int answerAIdx = -1;
            int answerBIdx = -1;
            int answerCIdx = -1;
            int answerDIdx = -1;
            int correctAnswerIdx = -1;

            DataFormatter formatter = new DataFormatter();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell == null) continue;
                String headerVal = formatter.formatCellValue(cell).trim();

                if (headerVal.equalsIgnoreCase(mapping.getQuestionContentCol())) {
                    questionContentIdx = i;
                } else if (mapping.getPassageCol() != null && !mapping.getPassageCol().isEmpty() && headerVal.equalsIgnoreCase(mapping.getPassageCol())) {
                    passageIdx = i;
                } else if (mapping.getInstructionCol() != null && !mapping.getInstructionCol().isEmpty() && headerVal.equalsIgnoreCase(mapping.getInstructionCol())) {
                    instructionIdx = i;
                } else if (headerVal.equalsIgnoreCase(mapping.getAnswerACol())) {
                    answerAIdx = i;
                } else if (headerVal.equalsIgnoreCase(mapping.getAnswerBCol())) {
                    answerBIdx = i;
                } else if (headerVal.equalsIgnoreCase(mapping.getAnswerCCol())) {
                    answerCIdx = i;
                } else if (headerVal.equalsIgnoreCase(mapping.getAnswerDCol())) {
                    answerDIdx = i;
                } else if (headerVal.equalsIgnoreCase(mapping.getCorrectAnswerCol())) {
                    correctAnswerIdx = i;
                }
            }

            // Kiểm tra xem các cột bắt buộc đã tìm thấy chưa
            if (questionContentIdx == -1 || answerAIdx == -1 || answerBIdx == -1 || answerCIdx == -1 || answerDIdx == -1 || correctAnswerIdx == -1) {
                throw new RuntimeException("Không tìm thấy đủ các cột bắt buộc (Câu hỏi, Đáp án A-D, Đáp án đúng) theo cấu hình ánh xạ.");
            }

            QuestionsGroup currentGroup = null;
            String currentPassage = "";
            int questionOrder = 1;

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                String questionContent = getCellValue(row, questionContentIdx).trim();
                if (questionContent.isEmpty()) {
                    continue; // Bỏ qua dòng không có nội dung câu hỏi
                }

                // Xử lý gom nhóm câu hỏi đọc hiểu (QuestionsGroup)
                String passage = passageIdx != -1 ? getCellValue(row, passageIdx).trim() : "";
                String instruction = instructionIdx != -1 ? getCellValue(row, instructionIdx).trim() : "";

                QuestionsType type = QuestionsType.SINGLE;
                if (!passage.isEmpty()) {
                    type = QuestionsType.MULTIPLE;
                    // Tạo group mới nếu group hiện tại rỗng hoặc nội dung passage khác đi
                    if (currentGroup == null || !passage.equals(currentPassage)) {
                        currentGroup = new QuestionsGroup();
                        currentGroup.setPassage(passage);
                        currentGroup.setInstruction(instruction.isEmpty() ? "Đọc đoạn văn và trả lời các câu hỏi sau." : instruction);
                        currentGroup.setTitle("Nhóm câu hỏi");
                        currentGroup.setTestId(test.getId().longValue());
                        currentGroup = repoGroup.save(currentGroup);
                        currentPassage = passage;
                    }
                } else {
                    currentGroup = null;
                    currentPassage = "";
                }

                // Lưu Question
                Questions question = new Questions();
                question.setContent(questionContent);
                question.setQuestionType(type);
                question.setQuestionOrder(questionOrder++);
                question.setTest(test);
                question.setGroup(currentGroup);
                question = repoQuestion.save(question);

                // Đọc nội dung 4 đáp án và đáp án đúng
                String ansA = getCellValue(row, answerAIdx).trim();
                String ansB = getCellValue(row, answerBIdx).trim();
                String ansC = getCellValue(row, answerCIdx).trim();
                String ansD = getCellValue(row, answerDIdx).trim();
                String correctAns = getCellValue(row, correctAnswerIdx).trim();

                saveAnswer(question, "A", ansA, correctAns, 1);
                saveAnswer(question, "B", ansB, correctAns, 2);
                saveAnswer(question, "C", ansC, correctAns, 3);
                saveAnswer(question, "D", ansD, correctAns, 4);
            }
        }
    }

    private String getCellValue(Row row, int colIndex) {
        if (colIndex == -1) return "";
        Cell cell = row.getCell(colIndex);
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    private void saveAnswer(Questions question, String label, String content, String correctAnswer, int order) {
        Answers answer = new Answers();
        answer.setQuestion(question);
        answer.setLabel(label);
        answer.setContent(content);
        answer.setAnswerOrder(order);

        // Kiểm tra xem đáp án có đúng không (Hỗ trợ so sánh nhãn A, B, C, D hoặc nội dung đáp án hoặc số 1, 2, 3, 4)
        boolean isCorrect = label.equalsIgnoreCase(correctAnswer) 
                || content.equalsIgnoreCase(correctAnswer)
                || (correctAnswer.equals("1") && label.equals("A"))
                || (correctAnswer.equals("2") && label.equals("B"))
                || (correctAnswer.equals("3") && label.equals("C"))
                || (correctAnswer.equals("4") && label.equals("D"));

        answer.setIsCorrect(isCorrect);
        repoAnswer.save(answer);
    }
}
