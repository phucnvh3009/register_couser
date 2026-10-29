
async function postJson(url, body) {
    try {
        const res = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });
        const data = await res.json().catch(() => ({}));
        return { ok: res.ok, data };
    } catch {
        return { ok: false, data: { message: "Cannot reach the server. Please try again." } };
    }
}

function showAlert(alertBox, message) {
    alertBox.textContent = message;
    alertBox.hidden = !message;
}
