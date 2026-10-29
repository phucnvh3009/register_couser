// Load the signed-in user from the server session; anyone not logged in goes back to /login
fetch("/api/me")
  .then(function (res) {
    return res.ok ? res.json() : Promise.reject(res.status);
  })
  .then(function (user) {
    const heroName = document.getElementById("heroName");
    const userGreeting = document.getElementById("userGreeting");
    const userEmailText = document.getElementById("userEmailText");

    if (heroName) heroName.textContent = user.name;
    if (userGreeting) userGreeting.textContent = "Hi, " + user.name;
    if (userEmailText) userEmailText.textContent = "Logged in as: " + user.email + " | Learn web development with our courses.";
  })
  .catch(function () {
    window.location.replace("/login");
  });

const logoutBtn = document.getElementById("logoutButton");
if (logoutBtn) {
  logoutBtn.addEventListener("click", async function () {
    await fetch("/api/logout", { method: "POST" }).catch(function () {});
    window.location.replace("/login");
  });
}
