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
* Very large source document (more than 100 pages + images) might not works.
