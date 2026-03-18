# Office Document Converter Connector

## Project Overview

- **Name**: Office Document Converter Connector
- **Artifact**: `org.bonitasoft.connectors:bonita-connector-document-converter`
- **Version**: 3.0.0-SNAPSHOT
- **Description**: Bonita connector that converts office documents (DOCX, ODT) to PDF using the XDocReport/OpenSagres library.
- **License**: GPL-2.0
- **Tech stack**: Java 17, Maven, Bonita Engine 10.0.0, XDocReport 2.1.0 (fr.opensagres), JUnit 5, Mockito, AssertJ

## Build Commands

```bash
# Full build with tests (default goal)
./mvnw clean verify

# Skip tests
./mvnw clean verify -DskipTests

# Run tests only
./mvnw test

# Package connector zip (assembly)
./mvnw clean package

# Run SonarCloud analysis
./mvnw sonar:sonar

# Deploy to Maven Central (requires GPG key)
./mvnw clean deploy -Pdeploy
```

The build produces:
- `target/bonita-connector-document-converter-<version>.jar` — the connector JAR
- `target/bonita-connector-document-converter-<version>-*.zip` — the Bonita connector assembly (impl + def + deps)

## Architecture

### Class hierarchy

```
AbstractConnector (bonita-common)
  └── DocumentConverterConnector          # Main connector entry point
        ├── uses InputParametersValidator  # Validates sourceDocument, encoding, outputFileName
        ├── uses DocumentConverterFactory  # Factory that creates the right converter
        └── uses MimeTypeUtil / FilenameUtil

DocumentConverter (interface)
  └── AbstractDocumentConverter
        ├── DocToPDFConverter              # DOCX/ODT → PDF via XDocReport
        └── DocToHTMLConverter             # DOCX/ODT → HTML via XDocReport

DocumentConverterFactory                   # Selects converter based on ConverterTypeTo
```

### Key patterns

- **Template method**: `AbstractDocumentConverter` holds the conversion logic; subclasses supply format-specific XDocReport converters.
- **Factory**: `DocumentConverterFactory` instantiates converters, injected into `DocumentConverterConnector` for testability.
- **Connector lifecycle**: `validateInputParameters()` → `executeBusinessLogic()` (standard Bonita pattern).
- **Resources**: Connector definition (`.def`) and implementation (`.impl`) descriptors live in `src/main/resources-filtered/` and are filtered at build time with Maven properties.
- **Assembly**: `src/assembly/` contains the Maven Assembly descriptor that packages the ZIP deployable into Bonita Studio.
- **Dependency list script**: `src/script/dependencies-as-var.groovy` runs at `generate-resources` to produce a filtered resource listing bundled dependencies.

## Testing Strategy

- Framework: JUnit 5 (Jupiter) + AssertJ + Mockito
- Unit tests cover `DocumentConverterConnector`, converters, factory, validators, and utilities.
- Tests use Mockito to mock `APIAccessor`, `ProcessAPI`, and `Document` so no Bonita runtime is required.
- Test resources in `src/test/resources/` include sample DOCX and ODT files for conversion round-trip tests.
- Coverage enforced via JaCoCo (`verify` phase report in `target/site/jacoco/`).
- SonarCloud project key: `bonitasoft_bonita-connector-document-converter`.

## Commit Message Format

Use [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <short description>

[optional body]

[optional footer(s)]
```

Common types: `feat`, `fix`, `chore`, `refactor`, `test`, `docs`, `ci`.

Examples:
```
feat: support ODT to PDF conversion
fix: handle null encoding input gracefully
chore: bump XDocReport to 2.1.1
```

## Release Process

1. Remove `-SNAPSHOT` from `version` in `pom.xml`.
2. Update connector definition version (`document-converter.def.version`) if the connector definition changed.
3. Commit: `chore: release X.Y.Z`.
4. Tag: `git tag X.Y.Z`.
5. Deploy to Maven Central: `./mvnw clean deploy -Pdeploy` (requires GPG key and Central credentials configured in `~/.m2/settings.xml`).
6. Push tag: `git push origin X.Y.Z`.
7. Bump version to next `-SNAPSHOT` and commit: `chore: prepare next development iteration`.
