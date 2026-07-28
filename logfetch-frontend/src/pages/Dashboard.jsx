import { useEffect, useState, useCallback } from 'react';
import { api } from '../api';
import { useAuth } from '../context/AuthContext';
import LogForm from '../components/LogForm';
import ResultPanel from '../components/ResultPanel';

export default function Dashboard() {
  const { username, logout } = useAuth();
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [loadingInitial, setLoadingInitial] = useState(true);

  const loadLast = useCallback(async () => {
    try {
      const data = await api.getLastResult();
      setResult(data);
    } catch {
      // No prior result yet — that's fine on a fresh account.
    } finally {
      setLoadingInitial(false);
    }
  }, []);

  useEffect(() => {
    loadLast();
  }, [loadLast]);

  async function handleSubmit(logRequest) {
    setError('');
    setSubmitting(true);
    try {
      const data = await api.analyseLog(logRequest);
      setResult(data);
    } catch (err) {
      setError(err.message || 'Failed to fetch and analyse the log file');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="dashboard">
      <header className="topbar">
        <div className="brand">LogFetch</div>
        <div className="topbar-right">
          <span className="muted">{username}</span>
          <button className="btn-ghost" onClick={logout}>
            Log out
          </button>
        </div>
      </header>

      <main className="dashboard-content">
        {error && <div className="alert">{error}</div>}
        <LogForm onSubmit={handleSubmit} submitting={submitting} />
        {loadingInitial ? (
          <div className="card empty-state">
            <p>Loading your last analysis…</p>
          </div>
        ) : (
          <ResultPanel result={result} />
        )}
      </main>
    </div>
  );
}
