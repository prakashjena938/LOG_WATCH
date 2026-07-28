const API_BASE = 'http://localhost:8080';

function getToken() {
  return localStorage.getItem('logfetch_token');
}

async function request(path, { method = 'GET', body, auth = true } = {}) {
  const headers = { 'Content-Type': 'application/json' };
  if (auth) {
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;
  }

  const res = await fetch(`${API_BASE}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  });

  let data = null;
  const text = await res.text();
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = text;
    }
  }

  if (!res.ok) {
    const message =
      (data && typeof data === 'object' && (data.message || data)) ||
      `Request failed with status ${res.status}`;
    throw new Error(typeof message === 'string' ? message : JSON.stringify(message));
  }

  return data;
}

export const api = {
  register: (user) => request('/register', { method: 'POST', body: user, auth: false }),
  login: (user) => request('/login', { method: 'POST', body: user, auth: false }),
  analyseLog: (logRequest) => request('/api/log/analyse', { method: 'POST', body: logRequest }),
  getLastResult: () => request('/api/log/show'),
};
