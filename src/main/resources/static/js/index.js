const khode = document.querySelector('.khode')
const overlay = document.querySelector('.overlay')
const khodethi = document.querySelector('.khodethi')
const topBtn = document.querySelector('.khode-left-top');
const bottomBtn = document.querySelector('.khode-left-bottom');
const thpt = document.querySelector('.onthithpt');
const dgnl = document.querySelector('.onthidgnl');

function toggleKhoDe() {
  overlay.classList.toggle('active');
  khodethi.classList.toggle('active');
}

function closeKhoDe() {
  overlay.classList.remove('active');
  khodethi.classList.remove('active');
}

function showTHPT() {
    thpt.classList.add('active');
    dgnl.classList.remove('active');
}

function showDGNL() {
    dgnl.classList.add('active');
    thpt.classList.remove('active');
}
function handleLogout() {
    const ok = confirm("Bạn có chắc chắn muốn đăng xuất?");
    if (ok) {
        window.location.href = "/logout";
    }
}

topBtn.addEventListener('mouseenter', showTHPT);
bottomBtn.addEventListener('mouseenter', showDGNL);
khode.addEventListener('click', toggleKhoDe);
overlay.addEventListener('click', closeKhoDe);

function goLogin() {
    window.location.href = "/register";
}