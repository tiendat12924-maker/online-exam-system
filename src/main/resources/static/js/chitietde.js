document.addEventListener("DOMContentLoaded", () => {
    const answers = document.querySelectorAll('.answer');
    const answeredText = document.getElementById('answered-count');
    const btnFinish = document.getElementById('btn-finish');
    const questions = document.querySelectorAll(".question-item");
    const nums = document.querySelectorAll(".num");
    const btnPrev = document.getElementById('btn-prev');
    const btnNext = document.getElementById('btn-next');

    let currentQuestionIndex = 0;
    window.goToQuestion = function(index) {
        if (index < 1 || index > questions.length) return;     
        currentQuestionIndex = index - 1;
        questions.forEach(q => q.classList.remove("active"));
        questions[currentQuestionIndex].classList.add("active");
        nums.forEach(n => n.classList.remove("active"));
        const activeNum = document.querySelector(`.num[data-index="${currentQuestionIndex}"]`);
        if (activeNum) {
            activeNum.classList.add("active");
            activeNum.scrollIntoView({ behavior: 'smooth', block: 'nearest', inline: 'center' });
        }
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
        const popupButtons = document.querySelectorAll('.popup .grid button');
        popupButtons.forEach(btn => btn.classList.remove('active'));
        const activePopupBtn = popupButtons[currentQuestionIndex];
        if (activePopupBtn) {
            activePopupBtn.classList.add('active');
        }
    };

    window.prevQuestion = function() {
        if (currentQuestionIndex > 0) {
            goToQuestion(currentQuestionIndex);
        }
    };

    window.nextQuestion = function() {
        if (currentQuestionIndex < questions.length - 1) {
            goToQuestion(currentQuestionIndex + 2);
        }
    };

    nums.forEach(num => {
        num.onclick = function() {
            const idx = parseInt(this.dataset.index);
            goToQuestion(idx + 1);
        };
    });

    answers.forEach(ans => {
        ans.addEventListener('click', (e) => {
            if (e.target.tagName === 'INPUT') return;
            const input = ans.querySelector('input[type="radio"]');
            if (!input) return;
            const questionId = input.dataset.qid;
            const questionName = input.name;
            document.querySelectorAll(`input[name="${questionName}"]`).forEach(inp => {
                inp.parentElement.classList.remove('selected');
            });
            ans.classList.add('selected');
            input.checked = true;
            const navNum = document.getElementById(`num-nav-${questionId}`);
            if (navNum) {
                navNum.classList.add('answered');
            }
            const popupBtn = document.getElementById(`popup-btn-${questionId}`);
            if (popupBtn) {
                popupBtn.classList.add('answered');
            }
            updateProgress();
        });
    });

    function updateProgress() {
        const checkedCount = document.querySelectorAll('input[type="radio"]:checked').length;
        if (answeredText) {
            answeredText.innerText = checkedCount;
        }
        if (checkedCount === TOTAL_QUESTIONS) {
            btnFinish.classList.remove('disabled');
        } else {
            btnFinish.classList.add('disabled');
        }
    }

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

    window.finish = function() {
        const checkedCount = document.querySelectorAll('input[type="radio"]:checked').length;
        if (checkedCount < TOTAL_QUESTIONS) {
            alert(`Bạn mới hoàn thành được ${checkedCount}/${TOTAL_QUESTIONS} câu hỏi.\nVui lòng trả lời đầy đủ tất cả các câu hỏi trước khi nhấn Hoàn thành!`);
            return;
        }
        
        clearInterval(timer);
        
        document.getElementById("examForm").submit();
    };

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
            document.getElementById("examForm").submit();
            return;
        }
        totalSeconds--;
    }

    updateTimer();
    let timer = setInterval(updateTimer, 1000);


    goToQuestion(1);
    updateProgress();
});