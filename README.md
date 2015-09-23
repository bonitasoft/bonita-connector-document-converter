# bonita-connector-document-converter
BonitaBPM Connector converting office document (docx,odt... etc) to pdf/html format

Implementation is based on [XDocReport](https://github.com/opensagres/xdocreport)

Knonw limitations
=================
Rendering issues:
* Bullet points 
* Strikethrough
* Exponents
* Indexes
* Hightlights
* Asian fonts support need a specific iText jar with a not compliant license

Execution performance
=====================
* Simple doc (1 to 5 pages) without images (< 500ms) on a standard desktop configuration.
* Simple doc (1 to 5 pages) with images (1 to 5 s) on a standard desktop configuration.
* Advanced doc (~50 pages) without images (~ 5s) on a standard desktop configuration.
* Advanced doc (~50 pages) with images (> 5s) on a standard desktop configuration and **can fail with a stackoverflow excpetion** for very large document and many images.
