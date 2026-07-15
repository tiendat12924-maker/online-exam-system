document.addEventListener("DOMContentLoaded", () => {
    const answers = document.querySelectorAll('.answer');
    const answeredText = document.getElementById('answered-count');
    const btnFinish = document.getElementById('btn-finish');
    const questions = document.querySelectorAll(".question-item");
    const nums = document.querySelectorAll(".num");
    const btnPrev = document.getElementById('btn-prev');
    const btnNext = document.getElementById('btn-next');

    let currentQuestionIndex = 0; // 0-indexed

    // Hàm chuyển đến câu hỏi cụ thể (1-indexed)
    window.goToQuestion = function(index) {
        if (index < 1 || index > questions.length) return;
        
        currentQuestionIndex = index - 1;

        // Cập nhật giao diện câu hỏi
        questions.forEach(q => q.classList.remove("active"));
        questions[currentQuestionIndex].classList.add("active");

        // Cập nhật thanh số câu ở Footer
        nums.forEach(n => n.classList.remove("active"));
        const activeNum = document.querySelector(`.num[data-index="${currentQuestionIndex}"]`);
        if (activeNum) {
            activeNum.classList.add("active");
            // Cuộn mượt thanh số câu đến vị trí câu hiện tại
            activeNum.scrollIntoView({ behavior: 'smooth', block: 'nearest', inline: 'center' });
        }

        // Cập nhật trạng thái các nút Câu trước / Câu sau
        if (currentQuestionIndex === 0) {
            btnPrev.style.opacity = '0.5';
            btnPrev.style.pointerEvents = 'none';
        } else {
            btnPrev.style.opacity = '1';
            btnPrev.style.pointerEvents = 'auto';
        }

        if (currentQuestionIndex === questions.length - 1) {
            btnNext.style.opacity = '0.5';
            btnNext.style.pointerEvents = 'none';
        } else {
            btnNext.style.opacity = '1';
            btnNext.style.pointerEvents = 'auto';
        }

        // Cập nhật active trong popup bảng câu hỏi
        const popupButtons = document.querySelectorAll('.popup .grid button');
        popupButtons.forEach(btn => btn.classList.remove('active'));
        const activePopupBtn = popupButtons[currentQuestionIndex];
        if (activePopupBtn) {
            activePopupBtn.classList.add('active');
        }
    };

    // Nút Câu trước
    window.prevQuestion = function() {
        if (currentQuestionIndex > 0) {
            goToQuestion(currentQuestionIndex);
        }
    };

    // Nút Câu sau
    window.nextQuestion = function() {
        if (currentQuestionIndex < questions.length - 1) {
            goToQuestion(currentQuestionIndex + 2);
        }
    };

    // Đăng ký sự kiện click cho các số câu hỏi ở Footer
    nums.forEach(num => {
        num.onclick = function() {
            const idx = parseInt(this.dataset.index);
            goToQuestion(idx + 1);
        };
    });

    // Xử lý khi click vào một phương án đáp án
    answers.forEach(ans => {
        ans.addEventListener('click', (e) => {
            // Chặn sự kiện nổi bọt nếu click trúng input radio
            if (e.target.tagName === 'INPUT') return;

            const input = ans.querySelector('input[type="radio"]');
            if (!input) return;

            const questionId = input.dataset.qid;
            const questionName = input.name;

            // Bỏ class selected trên toàn bộ đáp án của câu hỏi này
            document.querySelectorAll(`input[name="${questionName}"]`).forEach(inp => {
                inp.parentElement.classList.remove('selected');
            });

            // Chọn phương án hiện tại
            ans.classList.add('selected');
            input.checked = true;

            // Đánh dấu trạng thái Đã làm trên thanh số câu ở chân trang
            const navNum = document.getElementById(`num-nav-${questionId}`);
            if (navNum) {
                navNum.classList.add('answered');
            }

            // Đánh dấu trạng thái Đã làm trong bảng câu hỏi popup
            const popupBtn = document.getElementById(`popup-btn-${questionId}`);
            if (popupBtn) {
                popupBtn.classList.add('answered');
            }

            // Cập nhật tiến độ làm bài
            updateProgress();
        });
    });

    // Cập nhật số câu đã làm và mở khóa nút Hoàn thành
    function updateProgress() {
        const checkedCount = document.querySelectorAll('input[type="radio"]:checked').length;
        if (answeredText) {
            answeredText.innerText = checkedCount;
        }

        // Nếu đã làm hết 100% số câu hỏi
        if (checkedCount === TOTAL_QUESTIONS) {
            btnFinish.classList.remove('disabled');
        } else {
            btnFinish.classList.add('disabled');
        }
    }

    // Modal và Popup điều hướng
    window.openModal = function() {
        document.getElementById("modal").classList.add("show");
    };

    window.closeModal = function() {
        document.getElementById("modal").classList.remove("show");
    };

    window.togglePopup = function() {
        const popup = document.getElementById("popup");
        popup.style.display = popup.style.display === "block" ? "none" : "block";
    };

    // Hàm Hoàn thành (Chuyển sang trang xem lại Review)
    window.finish = function() {
        const checkedCount = document.querySelectorAll('input[type="radio"]:checked').length;
        if (checkedCount < TOTAL_QUESTIONS) {
            alert(`Bạn mới hoàn thành được ${checkedCount}/${TOTAL_QUESTIONS} câu hỏi.\nVui lòng trả lời đầy đủ tất cả các câu hỏi trước khi nhấn Hoàn thành!`);
            return;
        }
        
        clearInterval(timer); // Dừng timer đếm ngược
        
        // Submit form
        document.getElementById("examForm").submit();
    };

    // Logic Timer đếm ngược
    const timeElement = document.getElementById("time");
    let duration = parseInt(timeElement.dataset.duration);
    let totalSeconds = duration * 60;

    function updateTimer() {
        const minutes = Math.floor(totalSeconds / 60);
        const seconds = totalSeconds % 60;
        timeElement.textContent =
            String(minutes).padStart(2, "0") + ":" +
            String(seconds).padStart(2, "0");
        
        if (totalSeconds <= 0) {
            clearInterval(timer);
            alert("Đã hết thời gian làm bài! Hệ thống sẽ tự động nộp bài.");
            // Tự động submit lên review
            document.getElementById("examForm").submit();
            return;
        }
        totalSeconds--;
    }

    updateTimer();
    let timer = setInterval(updateTimer, 1000);

    // Khởi tạo hiển thị câu hỏi đầu tiên
    goToQuestion(1);
    updateProgress();
});