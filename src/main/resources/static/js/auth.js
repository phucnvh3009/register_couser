async function csrfFetch(url, options = {}) {
    const tokenResponse = await fetch("/api/csrf", {
        cache: "no-store"
    });

    if (!tokenResponse.ok) {
        throw new Error("Cannot obtain CSRF token");
    }

    const csrf = await tokenResponse.json();

    const headers = new Headers(options.headers);
    headers.set(csrf.headerName, csrf.token);

    return fetch(url, {
        ...options,
        headers
    });
}

async function postJson(url, body) {
    try {
        const res = await csrfFetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        });

        const data = await res.json().catch(() => ({}));

        return {
            ok: res.ok,
            data
        };
    } catch {
        return {
            ok: false,
            data: {
                message: "Cannot reach the server. Please try again."
            }
        };
    }
}

function showAlert(alertBox, message) {
    alertBox.textContent = message;
    alertBox.hidden = !message;
}