# LogFetch

SSH/SFTP log fetching + analysis tool with JWT-secured Spring Boot backend and a React frontend.

## Structure

```
LogFetch/            Spring Boot backend (Java 21, Maven)
logfetch-frontend/   React + Vite frontend
```

## Backend setup

1. Create a MySQL database (or let it auto-create — `application.properties` already has
   `createDatabaseIfNotExist=true` for a database named `logs`).
2. Update `LogFetch/src/main/resources/application.properties` with your MySQL username/password
   if different from the defaults (`root` / `8080`).
3. `jwt.secret` in the same file is a placeholder Base64 256-bit key — swap it for your own
   before this goes anywhere beyond your laptop.
4. Run it:
   ```bash
   cd LogFetch
   ./mvnw spring-boot:run
   ```
   The API starts on `http://localhost:8080`.

### API overview

| Method | Path              | Auth required | Purpose                                  |
|--------|-------------------|----------------|-------------------------------------------|
| POST   | `/register`       | No             | Create an account                        |
| POST   | `/login`          | No             | Returns `{ token, username }`            |
| POST   | `/api/log/analyse`| Yes (Bearer)   | SFTP-fetch a log file and analyse it     |
| GET    | `/api/log/show`   | Yes (Bearer)   | Get your most recent analysis result     |

Send the JWT on protected routes as `Authorization: Bearer <token>`.

## Frontend setup

```bash
cd logfetch-frontend
npm install
npm run dev
```

Runs on `http://localhost:5173` and talks to the backend at `http://localhost:8080`
(hardcoded in `src/api.js` — change `API_BASE` there if you deploy elsewhere).

Flow: Register → Login → Dashboard, where you submit SSH/SFTP connection details plus
a remote file path, then browse the parsed errors/warnings/info/all-lines.

## What changed from the plain version

- Full JWT auth: `JwtUtil` (issue/verify tokens), `JwtFilter` (reads the `Authorization`
  header on every request), `SecurityConfig` (stateless sessions, CORS for the Vite dev
  server, public `/login` & `/register`, everything else authenticated).
- Passwords are BCrypt-hashed (`UserService.register`) and never serialized back out
  (`@JsonIgnore` on `UserRegistry.password`).
- `AnalysisResult` is a JPA entity again, persisted via `LogRepo`, and tagged with the
  requesting user (`requestedBy`) so `/api/log/show` returns *your* last result, not
  just whatever the server last computed for anyone.
- `UserService.login` previously didn't compile (a broken `if` with no body) — it now
  actually authenticates via Spring's `AuthenticationManager` and returns a signed JWT.
