# bonita-connector-document-converter
![](https://github.com/bonitasoft/bonita-connector-document-converter/workflows/Build/badge.svg)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=bonitasoft_bonita-connector-document-converter&metric=alert_status)](https://sonarcloud.io/dashboard?id=bonitasoft_bonita-connector-document-converter)
[![GitHub release](https://img.shields.io/github/v/release/bonitasoft/bonita-connector-document-converter?color=blue&label=Release)](https://github.com/bonitasoft/bonita-connector-document-converter/releases)
[![Maven Central](https://img.shields.io/maven-central/v/org.bonitasoft.connectors/bonita-connector-document-converter.svg?label=Maven%20Central&color=orange)](https://search.maven.org/search?q=g:%22org.bonitasoft.connectors%22%20AND%20a:%22bonita-connector-document-converter%22)
[![License: GPL v2](https://img.shields.io/badge/License-GPL%20v2-yellow.svg)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html)

Bonita connector converting office document (in formats docx and odt) to pdf/html format

Implementation is based on [XDocReport](https://github.com/opensagres/xdocreport)

## Bonita compatibility

[v2.3.0](https://github.com/bonitasoft/bonita-connector-document-converter/releases/2.3.0) is compatible with Bonita version 7.11.x and above

## Build

__Clone__ or __fork__ this repository, then at the root of the project run:

`./mvnw`

## Release

In order to create a new release:
- On the release branch, make sure to update the pom version (remove the -SNAPSHOT)
- Run the action 'Create release', set the version to release as parameter
- When the action is completed, do not forget to manage the release on the nexus (close and release)
- Update the `master` with the next SNAPSHOT version.

## Known limitations

Rendering issues:

* Bullet points
* Strike-through
* Exponents
* Indexes
* Highlighting
* Asian fonts support need a specific iText jar with a non-compliant license. Therefore, it cannot be provided built-in.
* Image positioning
* Drawing shapes
* Aligning of some text elements

## Execution performance

* Simple doc (1 to 5 pages) without images (< 500ms) on a standard desktop configuration.
* Simple doc (1 to 5 pages) with images (1 to 5 s) on a standard desktop configuration.
* Advanced doc (~50 pages) without images (~ 5s) on a standard desktop configuration.
* Advanced doc (~50 pages) with images (> 5s) on a standard desktop configuration and **can fail with a stack overflow exception** for very large documents and many images.

## Contributing

We would love you to contribute, pull requests are welcome! Please see the [CONTRIBUTING.md](CONTRIBUTING.md) for more information.

## License

The sources and documentation in this project are released under the [GPLv2 License](LICENSE)
