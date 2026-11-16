# CI/CD Workshop — Java + GitHub Actions

**htw saar × abat | Guest lecture | Java 21 / Maven / JUnit 5 / Docker**

## Learning goals
- Understand workflow triggers, jobs, runners, actions and shell steps.
- Run `mvn clean verify` automatically on push and pull request.
- Inspect a failing unit test and fix a red pipeline.
- (Bonus) Build and release a versioned Docker image to GitHub Container Registry (GHCR).

## 0 — Fork the workshop repository (5 min)
Your instructor publishes this starter as a **public GitHub repository** and shares the URL.
Click **Fork**, choose your account, then clone **your fork**. A ZIP file itself cannot be forked on GitHub.

Alternatively, download the starter ZIP from your instructor, extract it, and import it into a **new GitHub repository**.

## 1 — Explore the Java application (5 min)
- `src/main/java/.../GreetingService.java` — simple business logic.
- `src/test/java/.../GreetingServiceTest.java` — JUnit 5 tests.
- `src/main/java/.../DemoApplication.java` — a tiny built-in HTTP server.
- `pom.xml` — Maven build configuration, Java 21.
- `Dockerfile` — multi-stage Java Docker image.

Locally, if Java 21 and Maven are installed:
```bash
mvn clean verify
java -jar target/cicd-java-demo-1.0.0-SNAPSHOT.jar
# In another terminal:
curl http://localhost:8080/health
curl http://localhost:8080/hello
```

## 2 — Build your first CI workflow (10 min)
Open `.github/workflows/ci.yml`. It deliberately contains a placeholder step, **not** a finished CI pipeline.
1. Under `steps`, after checkout, add `actions/setup-java@v6` with distribution `temurin`, Java version `21` and `cache: maven`.
2. Add a named step running `mvn --batch-mode --no-transfer-progress clean verify`.
3. Remove the placeholder reminder.
4. Commit and push. Open **GitHub → Actions → Java CI**. Are both tests green?

Hints:
```yaml
- uses: actions/setup-java@v6
  with:
    distribution: temurin
    java-version: '21'
    cache: maven
```

## 3 — Break it, read the logs, fix it (10 min)
1. Change the expected result in `GreetingServiceTest` from `Hello, Ada!` to `Hello, Grace!` (leave production code unchanged).
2. Push and observe the failed Maven test. Identify the failing assertion in the job log.
3. Restore `Hello, Ada!`, push again and confirm green status.
4. Optionally use a feature branch and open a pull request against **your own fork**.

## 4 — Docker bonus (10 min)
Add a step after Maven verification:
```yaml
- name: Build Docker image
  run: docker build -t cicd-java-demo:${{ github.sha }} .
```
The Dockerfile builds a runnable JAR and packages it in a Java 21 runtime image.
On a machine with Docker:
```bash
docker build -t cicd-java-demo:local .
docker run --rm -p 8080:8080 cicd-java-demo:local
```

## 5 — Release bonus (optional / instructor demo)
Publish `ghcr.io/OWNER/REPO:v1.0.0` when a tag `v1.0.0` is pushed.
A **separate** `.github/workflows/release.yml` should have `packages: write`, use the built-in `GITHUB_TOKEN`, log in to GHCR and run `docker build` + `docker push`. See the instructor solution.

**Note:** A successful image build is not the same as publishing an image, and publishing is not the same as deploying to production.

## Troubleshooting
- **Workflow runs but tests never run**: you still have the placeholder step; complete the TODOs.
- **No workflow run**: check the branch and `.github/workflows/ci.yml` path; enable Actions if prompted.
- **Maven error**: make sure `setup-java` is before the Maven command and indentation is valid.
- **Red pipeline**: open the failing step and read the assertion error.
- **GHCR permission denied**: confirm the release workflow has `packages: write` and repository/package policies allow publishing. For forked repositories, GitHub Actions settings may require enabling workflows.

**Safety:** Never commit access tokens, passwords or other secrets. For release workflows, use `GITHUB_TOKEN` with the minimum required permissions.
