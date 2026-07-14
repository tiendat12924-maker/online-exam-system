const forms = document.querySelectorAll(".email-form-mid");

forms.forEach(form => {
  const inputs = form.querySelectorAll(".input-field");
  const btn = form.querySelector(".btn-submit");

  inputs.forEach(input => {
    input.addEventListener("input", () => {
      const allFilled = Array.from(inputs).every(
        i => i.value.trim() !== ""
      );

      btn.disabled = !allFilled;
      btn.classList.toggle("active", allFilled);
    });
  });
});

function goBack() {
    window.location.href = "/register";
}
function goIndex() {
    window.location.href = "/index";
}


document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".toggle-password").forEach(btn => {
        btn.addEventListener("click", function () {

            const box = this.closest(".password-box");
            const input = box.querySelector("input");
            const icon = this.querySelector("i");

            if (!input) return; // tránh lỗi null

            if (input.type === "password") {
                input.type = "text";
                icon.classList.remove("fa-eye");
                icon.classList.add("fa-eye-slash");
            } else {
                input.type = "password";
                icon.classList.remove("fa-eye-slash");
                icon.classList.add("fa-eye");
            }

        });
    });
});

