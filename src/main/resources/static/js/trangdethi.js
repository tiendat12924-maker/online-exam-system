const khode = document.querySelector('.khode')
const overlay = document.querySelector('.overlay')
const khodethi = document.querySelector('.khodethi')
const topBtn = document.querySelector('.khode-left-top');
const bottomBtn = document.querySelector('.khode-left-bottom');
const thpt = document.querySelector('.onthithpt');
const dgnl = document.querySelector('.onthidgnl');

function toggleMenu() {
    document.getElementById("menu").classList.toggle("show");
}
function selectOption(img, el) {
    document.getElementById("logoImg").src = img;
    document.querySelectorAll(".item").forEach(i => i.classList.remove("active"));
    el.closest(".item").classList.add("active");
    document.getElementById("menu").classList.remove("show");
}
document.addEventListener("click", function(e) {
    if (!e.target.closest(".dropdown")) {
        document.getElementById("menu").classList.remove("show");
    }
});
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
function goIndex() {
    window.location.href = "/index";
}
khode.addEventListener('click', toggleKhoDe);
overlay.addEventListener('click', closeKhoDe);
topBtn.addEventListener('mouseenter', showTHPT);
bottomBtn.addEventListener('mouseenter', showDGNL);