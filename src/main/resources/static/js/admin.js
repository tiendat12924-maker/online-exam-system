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
    document.querySelector("." + pageClass).style.display = "block";
    document.querySelectorAll(".menu-item").forEach(function(item) {
        item.classList.remove("active");
    });
    element.classList.add("active");
}