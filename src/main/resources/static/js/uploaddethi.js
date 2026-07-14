const buttons = document.querySelectorAll(".type");
const forms = document.querySelectorAll(".question-form");
const rightform = document.querySelectorAll(".right-question-form")

buttons.forEach(button => {
    button.addEventListener("click", function () {
        buttons.forEach(btn => btn.classList.remove("active"));
        this.classList.add("active");
        forms.forEach(form => form.classList.remove("active"));
        rightform.forEach(form => form.classList.remove("active"));
        const target = this.dataset.target;
        document.querySelector(".question-form." + target)
                .classList.add("active");
        document.querySelector(".right-question-form." + target)
                .classList.add("active");
    });
});

let childQuestions = [];
const addBtn = document.getElementById("addQuestionBtn");
const groupForm = document.getElementById("groupQuestionForm");
addBtn.addEventListener("click", function () {
    addChildQuestion();
});
function addChildQuestion() {
	const form = document.getElementById("groupQuestionForm");
    const content = document.getElementById("questionContent").value.trim();
	const answerA = form.querySelector("[name='answerA']").value.trim();
	const answerB = form.querySelector("[name='answerB']").value.trim();
	const answerC = form.querySelector("[name='answerC']").value.trim();
	const answerD = form.querySelector("[name='answerD']").value.trim();
    const correct = document.querySelector("input[name='correctAnswer']:checked");
    if (content === "") {
        alert("Nhập nội dung câu hỏi");
        return;
    }
    if (
        answerA === "" ||
        answerB === "" ||
        answerC === "" ||
        answerD === ""
    ) {
        alert("Nhập đầy đủ đáp án");
        return;
    }
    if (correct == null) {
        alert("Chọn đáp án đúng");
        return;
    }
    const question = {
        content: content,
        answerA: answerA,
        answerB: answerB,
        answerC: answerC,
        answerD: answerD,
        correctAnswer: correct.value,
        score: 1.0
    };
    childQuestions.push(question);
    renderChildTable();
    createHiddenInputs();
    clearChildForm();
}
function clearChildForm() {
    document.getElementById("questionContent").value = "";
    document.querySelector("[name='answerA']").value = "";
    document.querySelector("[name='answerB']").value = "";
    document.querySelector("[name='answerC']").value = "";
    document.querySelector("[name='answerD']").value = "";
    document
        .querySelectorAll("input[name='correctAnswer']")
        .forEach(r => r.checked = false);
}
function createHiddenInputs() {
    const hiddenInputs = document.getElementById("hiddenInputs");
    hiddenInputs.innerHTML = "";
    childQuestions.forEach(q => {
        hiddenInputs.innerHTML += `
            <input type="hidden" name="questionContent" value="${q.content}">
            <input type="hidden" name="groupAnswerA" value="${q.answerA}">
            <input type="hidden" name="groupAnswerB" value="${q.answerB}">
            <input type="hidden" name="groupAnswerC" value="${q.answerC}">
            <input type="hidden" name="groupAnswerD" value="${q.answerD}">
            <input type="hidden" name="groupCorrectAnswer" value="${q.correctAnswer}">
        `;
    });
}
function renderChildTable() {
    const tbody = document.getElementById("childQuestionTable");
    tbody.innerHTML = "";
    childQuestions.forEach((q,index)=>{
        tbody.innerHTML += `
        <tr>
            <td>${index+1}</td>
            <td>${q.content}</td>
            <td>1.0</td>
            <td>
                <div class="table-action">
                    <button type="button" class="edit" onclick="editQuestion(${index})">
                        <i class="fa-solid fa-pen"></i>
                    </button>
                    <button type="button"  class="delete"  onclick="deleteQuestion(${index})">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
        `;
    });
}
function deleteQuestion(index){
    childQuestions.splice(index,1);
    renderChildTable();
    createHiddenInputs();
}
function editQuestion(index){
    const q = childQuestions[index];
    document.getElementById("questionContent").value=q.content;
    document.querySelector("[name='answerA']").value=q.answerA;
    document.querySelector("[name='answerB']").value=q.answerB;
    document.querySelector("[name='answerC']").value=q.answerC;
    document.querySelector("[name='answerD']").value=q.answerD;
    document.querySelector(
        `input[name='correctAnswer'][value='${q.correctAnswer}']`
    ).checked=true;
    childQuestions.splice(index,1);
    renderChildTable();
    createHiddenInputs();
}
function clearChildForm(){
	const form = document.getElementById("groupQuestionForm");
	    form.querySelector("#questionContent").value = "";
	    form.querySelector("[name='answerA']").value = "";
	    form.querySelector("[name='answerB']").value = "";
	    form.querySelector("[name='answerC']").value = "";
	    form.querySelector("[name='answerD']").value = "";
	    form.querySelectorAll("input[name='correctAnswer']")
	        .forEach(radio => radio.checked = false);
}
function scrollToQuestionForm() {
    document.querySelector(".add-question-card")
            .scrollIntoView({
                behavior: "smooth"
            });

    document.getElementById("questionContent").focus();
}