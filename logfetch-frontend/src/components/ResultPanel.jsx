import { useState } from 'react';

const TABS = [
  { key: 'errors', label: 'Errors' },
  { key: 'warnings', label: 'Warnings' },
  { key: 'infos', label: 'Info' },
  { key: 'allLines', label: 'All lines' },
];

export default function ResultPanel({ result }) {
  const [activeTab, setActiveTab] = useState('errors');

  if (!result) {
    return (
      <div className="card empty-state">
        <p>No analysis yet. Fill in the form above and run your first fetch.</p>
      </div>
    );
  }

  const lines = result[activeTab] || [];

  return (
    <div className="card result-panel">
      <div className="result-header">
        <div>
          <h2>Analysis result</h2>
          <p className="muted">
            {result.ip} · {result.logPath} · {result.AnalyseAt}
          </p>
        </div>
      </div>

      <div className="stat-grid">
        <div className="stat">
          <span className="stat-value">{result.totalLines}</span>
          <span className="stat-label">Total lines</span>
        </div>
        <div className="stat stat-error">
          <span className="stat-value">{result.errorCount}</span>
          <span className="stat-label">Errors</span>
        </div>
        <div className="stat stat-warn">
          <span className="stat-value">{result.warningCount}</span>
          <span className="stat-label">Warnings</span>
        </div>
        <div className="stat stat-info">
          <span className="stat-value">{result.infoCount}</span>
          <span className="stat-label">Info</span>
        </div>
      </div>

      <div className="tabs">
        {TABS.map((tab) => (
          <button
            key={tab.key}
            className={`tab ${activeTab === tab.key ? 'tab-active' : ''}`}
            onClick={() => setActiveTab(tab.key)}
            type="button"
          >
            {tab.label} ({(result[tab.key] || []).length})
          </button>
        ))}
      </div>

      <div className="log-lines">
        {lines.length === 0 ? (
          <p className="muted">Nothing here.</p>
        ) : (
          lines.map((line, i) => (
            <div key={i} className={`log-line log-line-${activeTab}`}>
              {line}
            </div>
          ))
        )}
      </div>
    </div>
  );
}
