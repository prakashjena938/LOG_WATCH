import { useState } from 'react';

const initialState = {
  ip: '',
  port: 22,
  filePath: '',
  userName: '',
  password: '',
};

export default function LogForm({ onSubmit, submitting }) {
  const [form, setForm] = useState(initialState);

  function update(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }));
  }

  function handleSubmit(e) {
    e.preventDefault();
    onSubmit({ ...form, port: Number(form.port) });
  }

  return (
    <form className="card log-form" onSubmit={handleSubmit}>
      <h2>Fetch &amp; analyse a log file</h2>
      <p className="muted">Connects over SFTP, pulls the file, and scans it for errors, warnings, and info lines.</p>

      <div className="form-grid">
        <label>
          Host / IP
          <input
            type="text"
            placeholder="192.168.1.10"
            value={form.ip}
            onChange={(e) => update('ip', e.target.value)}
            required
          />
        </label>

        <label>
          Port
          <input
            type="number"
            value={form.port}
            onChange={(e) => update('port', e.target.value)}
            required
          />
        </label>

        <label className="span-2">
          Remote file path
          <input
            type="text"
            placeholder="/var/log/app/application.log"
            value={form.filePath}
            onChange={(e) => update('filePath', e.target.value)}
            required
          />
        </label>

        <label>
          SSH username
          <input
            type="text"
            value={form.userName}
            onChange={(e) => update('userName', e.target.value)}
            required
          />
        </label>

        <label>
          SSH password
          <input
            type="password"
            value={form.password}
            onChange={(e) => update('password', e.target.value)}
            required
          />
        </label>
      </div>

      <button type="submit" className="btn-primary" disabled={submitting}>
        {submitting ? 'Fetching…' : 'Fetch & Analyse'}
      </button>
    </form>
  );
}
