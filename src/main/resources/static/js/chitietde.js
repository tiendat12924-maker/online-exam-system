const answers = document.querySelectorAll('.answer');
const answeredText = document.getElementById('answered');
const progressBox = document.getElementById('progressBox');
const label = document.querySelectorAll('.label')
const questions=document.querySelectorAll(".question-item");
const nums=document.querySelectorAll(".num");

nums.forEach(num=>{

    num.onclick=function(){

        const index=this.dataset.index;

        questions.forEach(q=>q.classList.remove("active"));
        nums.forEach(n=>n.classList.remove("active"));

        questions[index].classList.add("active");
        this.classList.add("active");

    }

});

function openModal() {
    document.getElementById("modal").classList.add("show");
}

function closeModal() {
    document.getElementById("modal").classList.remove("show");
}
function togglePopup() {
    const popup = document.getElementById("popup");
    popup.style.display =
        popup.style.display === "block" ? "none" : "block";
}


const TOTAL = 24;

let answeredMap = {};

answers.forEach((ans, index) => {

    ans.addEventListener('click', () => {

        const input = ans.querySelector('input');
        const questionName = input.name;
        document.querySelectorAll(`input[name="${questionName}"]`)
            .forEach(i => i.parentElement.classList.remove('selected'));
        ans.classList.add('selected');
        input.checked = true;
        if (!answeredMap[questionName]) {
            answeredMap[questionName] = true;
        }
        const count = Object.keys(answeredMap).length;
        answeredText.innerText = count;
        if (count === TOTAL) {
            progressBox.classList.add('done');
        }
    });
});

function finish(){
    clearInterval(timer);   // Dừng đếm ngược
    if(confirm("Bạn có chắc chắn muốn nộp bài?")){
        document.getElementById("examForm").submit();
    }else{
        // Nếu bấm Hủy thì chạy lại timer
        timer = setInterval(updateTimer,1000);
    }
}
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
        alert("Đã hết thời gian làm bài!");
        document.getElementById("examForm").submit();
        return;
    }
    totalSeconds--;
}
updateTimer();
timer = setInterval(updateTimer, 1000);

function goToQuestion(index) {
    document.querySelectorAll(".question-item").forEach(item => {
        item.classList.remove("active");
    });
    document.getElementById("question-" + index).classList.add("active");
    togglePopup();
}