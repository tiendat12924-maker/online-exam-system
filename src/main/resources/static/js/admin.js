function handleLogout() {
    const ok = confirm("Bạn có chắc chắn muốn đăng xuất?");
    if (ok) {
        window.location.href = "/logout";
    }
}

function showPage(pageClass, element) {
    document.querySelectorAll(".page").forEach(function(page) {
        page.style.display = "none";
    });
    const targetPage = document.querySelector("." + pageClass);
    if (targetPage) {
        targetPage.style.display = "block";
    }
    document.querySelectorAll(".menu-item").forEach(function(item) {
        item.classList.remove("active");
    });
    if (element) {
        element.classList.add("active");
    }
    localStorage.setItem('activeAdminTab', pageClass);
}

function openSubjectModal() {
    const modal = document.getElementById('add-subject-modal');
    if (modal) modal.style.display = 'flex';
}

function closeSubjectModal() {
    const modal = document.getElementById('add-subject-modal');
    if (modal) modal.style.display = 'none';
    const form = document.getElementById('add-subject-form');
    if (form) form.reset();
}

document.addEventListener('DOMContentLoaded', function() {
    const examPage = document.querySelector('.exam-page');
    if (examPage) {
        const leftPanel = examPage.querySelector('.manage-user-test-left');
        const rightPanel = examPage.querySelector('.manage-user-right');
        if (leftPanel && rightPanel) {
            leftPanel.style.width = '100%';
            rightPanel.style.display = 'none';
        }
    }

    document.querySelectorAll('.btn-view-detail').forEach(button => {
        button.addEventListener('click', function() {
            const id = this.getAttribute('data-id');
            const title = this.getAttribute('data-title');
            const subject = this.getAttribute('data-subject');
            const questions = this.getAttribute('data-questions');
            const duration = this.getAttribute('data-duration');
            const created = this.getAttribute('data-created');
            const updated = this.getAttribute('data-updated');
            const difficulty = this.getAttribute('data-difficulty');
            const examPage = document.querySelector('.exam-page');
            const detailPanel = examPage.querySelector('.exam-detail');
            const leftPanel = examPage.querySelector('.manage-user-test-left');
            const rightPanel = examPage.querySelector('.manage-user-right');
            if (detailPanel && leftPanel && rightPanel) {           
                detailPanel.querySelector('.title').textContent = title;
                detailPanel.querySelector('.exam-id').textContent = 'ID: #' + id;

                const infoRows = detailPanel.querySelectorAll('.info-row p');
                if (infoRows.length >= 6) {
                    infoRows[0].textContent = subject;
                    infoRows[1].textContent = questions + ' câu';
                    infoRows[2].textContent = duration + ' phút';
                    infoRows[3].textContent = created;
                    infoRows[4].textContent = updated ? updated : created;
                    infoRows[5].textContent = 'Đề thi môn ' + subject + ' độ khó ' + difficulty + '.';
                }

                const statsValues = detailPanel.querySelectorAll('.stat-card .value');
                if (statsValues.length >= 4) {
                    statsValues[0].textContent = '0';
                    statsValues[1].textContent = '0';
                    statsValues[2].textContent = '0.0';
                    statsValues[3].textContent = '0%';
                }

                leftPanel.style.width = '73%';
                rightPanel.style.display = 'block';
            }
        });
    });

    const closeExamDetail = document.querySelector('.exam-detail .close');
    if (closeExamDetail) {
        closeExamDetail.addEventListener('click', function() {
            const examPage = document.querySelector('.exam-page');
            const leftPanel = examPage.querySelector('.manage-user-test-left');
            const rightPanel = examPage.querySelector('.manage-user-right');
            if (leftPanel && rightPanel) {
                rightPanel.style.display = 'none';
                leftPanel.style.width = '100%';
            }
        });
    }

    const examRows = document.querySelectorAll('.exam-page tbody tr');
    const paginationContainer = document.getElementById('exam-pagination');
    const rowsPerPage = 5;

    function renderExamPage(page) {
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        examRows.forEach((row, index) => {
            if (index >= start && index < end) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });

        if (paginationContainer) {
            paginationContainer.innerHTML = '';
            const totalPages = Math.ceil(examRows.length / rowsPerPage);

            if (totalPages <= 1) {
                paginationContainer.style.display = 'none';
                return;
            } else {
                paginationContainer.style.display = 'flex';
            }

            const prevButton = document.createElement('button');
            prevButton.innerHTML = '‹';
            if (page === 1) {
                prevButton.disabled = true;
                prevButton.style.cursor = 'not-allowed';
                prevButton.style.opacity = '0.5';
            } else {
                prevButton.addEventListener('click', () => renderExamPage(page - 1));
            }
            paginationContainer.appendChild(prevButton);

            for (let i = 1; i <= totalPages; i++) {
                const pageButton = document.createElement('button');
                pageButton.innerText = i;
                if (i === page) {
                    pageButton.classList.add('active');
                }
                pageButton.addEventListener('click', () => renderExamPage(i));
                paginationContainer.appendChild(pageButton);
            }

            const nextButton = document.createElement('button');
            nextButton.innerHTML = '›';
            if (page === totalPages) {
                nextButton.disabled = true;
                nextButton.style.cursor = 'not-allowed';
                nextButton.style.opacity = '0.5';
            } else {
                nextButton.addEventListener('click', () => renderExamPage(page + 1));
            }
            paginationContainer.appendChild(nextButton);
        }
    }

    if (examRows.length > 0) {
        renderExamPage(1);
    }

    const addSubjectForm = document.getElementById('add-subject-form');
    if (addSubjectForm) {
        addSubjectForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const name = document.getElementById('modal-sub-name').value.trim();
            const code = document.getElementById('modal-sub-code').value.trim().toUpperCase();
            const status = document.getElementById('modal-sub-status').value === 'true';

            fetch('/admin/api/add-subject', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name, code, status })
            })
            .then(res => {
                if (res.ok) {
                    alert('Thêm môn học mới thành công!');
                    closeSubjectModal();

                    localStorage.setItem('activeAdminTab', 'subject-page');
                    window.location.reload();
                } else {
                    return res.text().then(text => { throw new Error(text) });
                }
            })
            .catch(err => {
                alert('Có lỗi xảy ra: ' + err.message);
            });
        });
    }

    const savedActiveTab = localStorage.getItem('activeAdminTab');
    if (savedActiveTab) {
        localStorage.removeItem('activeAdminTab');
        const menuItem = document.querySelector(`.menu-item[onclick*="${savedActiveTab}"]`);
        if (menuItem) {
            showPage(savedActiveTab, menuItem);
        }
    }

});