# Week 8 — Pipeline as Code & Server Deployment

## 1. Declarative Pipeline-as-Code
The pipeline is codified in `Jenkinsfile` at the root of the repository, version-controlled alongside application code.

### Pipeline Stages
1. **Checkout**: Retrieves source code from Git repository.
2. **Compile & Static Analysis**: Compiles Java source files with Maven.
3. **Unit Tests**: Runs unit tests and archives JUnit XML reports.
4. **Package Artifact**: Assembles `.war` archive and fingerprints build artifacts.
5. **Deploy**: Parameters (`DEPLOY_ENV = staging | production`) route deployment to target environments.

---

## 2. Parameterization Evidence
- Parameter `DEPLOY_ENV` configured with default value `staging`.
- Parameter `RUN_SELENIUM_TESTS` provides toggle for regression testing.
- Pipeline verified executing on Jenkins with visual stage progress and artifact archiving.
