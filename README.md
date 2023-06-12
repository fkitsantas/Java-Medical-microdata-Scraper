# Java-Medical-microdata-Scrapper

Java-Medical-microdata-Scrapper is a Java web scraping tool specifically designed for extracting medical micro data from web pages. This tool allows you to scrape information such as company name, drug name, drug class, indications, dosage form, side effects, and warnings from target URLs. The extracted data can be printed to the console and saved in an XML format for further analysis or storage.

## Installation

To use Java-Medical-microdata-Scrapper, follow these steps:

1. Clone the repository to your local machine:

  `git clone https://github.com/fkitsantas/Java-Medical-microdata-Scrapper.git`

2. Import the project into your preferred Java IDE.

3. Build the project to resolve any dependencies.

4. Run the `MedDataScraper` class to execute the scraper.

## Usage

The scraper is designed to extract medical micro data from specific web pages. By default, it is set to scrape data from the URL `http://linter.structured-data.org/examples/schema.org/Drug-TreatmentIndication-MedicalContraindication-273-rdfa.html`. You can modify the URL in the code to scrape data from your desired source.

The extracted data will be printed to the console, displaying drug information including drug name, company name, drug class, indications, dosage form, side effects, and warnings.

The scraped data will also be saved in an XML format. The XML file will be named `Scraped_Medical.xml` and will be stored in the project directory.

## Dependencies

Java-Medical-microdata-Scrapper tool requires the following dependencies:

- Java 8 or above
- DOM API
- XML API

These dependencies are included in the standard Java Development Kit (JDK) libraries, so there's no need to install any additional packages.
