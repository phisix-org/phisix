phisix
======
[![Java CI with Maven](https://github.com/phisix-org/phisix/actions/workflows/maven.yml/badge.svg)](https://github.com/phisix-org/phisix/actions/workflows/maven.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/b523693e36944745bd3e2072d380a272)](https://app.codacy.com/gh/phisix-org/phisix/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/b523693e36944745bd3e2072d380a272)](https://app.codacy.com/gh/phisix-org/phisix/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)
[![Mergify Status][mergify-status]][mergify]

[mergify]: https://mergify.com
[mergify-status]: https://img.shields.io/endpoint.svg?url=https://api.mergify.com/v1/badges/phisix-org/phisix

Simple PSEi (formerly known as PHISIX) RESTful API hosted on Google AppEngine

> **⚠️ IMPORTANT NOTICE: The public phisix-api.appspot.com service has been discontinued for legal reasons.**  
> **A takedown notice was received on November 30, 2023, and the service was shut down on December 4, 2023.**  
> **To access Philippine Stock Exchange data, please deploy your own instance using the instructions below.**

## Development & Deployment

### Prerequisites

- **Java 17** (OpenJDK recommended)
- **Maven 3.6+** 
- **Google Cloud SDK** (for App Engine deployment)
- **Git**

### Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/phisix-org/phisix.git
   cd phisix
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run tests**
   ```bash
   mvn test
   ```

4. **Run full verification** (includes integration tests & code coverage)
   ```bash
   mvn clean verify
   ```

5. **Run locally** (for development)
   ```bash
   mvn clean package appengine:run
   ```

### Local Development

#### Running with App Engine Development Server

**Note**: Always use `mvn clean package appengine:run` during development to ensure:
- Source code changes are recompiled
- Tests are executed
- Documentation changes are regenerated 
- Site content is updated with latest modifications
- WAR file is properly packaged

1. **Stage the application** (optional - for testing staging process)
   ```bash
   mvn clean package appengine:stage
   ```

2. **Run locally** (recommended for development)
   ```bash
   mvn clean package appengine:run
   ```

   The API will be available at: `http://localhost:8080`

#### Test the local API

- **All stocks (JSON)**: `http://localhost:8080/stocks.json`
- **All stocks (XML)**: `http://localhost:8080/stocks.xml`
- **Specific stock**: `http://localhost:8080/stocks/BDO.json`
- **Historical data**: `http://localhost:8080/stocks/BDO.2024-01-15.json`

### Deployment to Google App Engine

#### Prerequisites for Deployment

1. **Setup Google Cloud Project**
   ```bash
   gcloud projects create your-project-id
   gcloud config set project your-project-id
   gcloud app create
   ```

2. **Enable App Engine API**
   ```bash
   gcloud services enable appengine.googleapis.com
   ```

3. **Authenticate**
   ```bash
   gcloud auth login
   gcloud auth application-default login
   ```

#### Deploy to App Engine

1. **Update `pom.xml`** - Change the App Engine application name:
   ```xml
   <properties>
       <appengine.app.name>your-project-id</appengine.app.name>
   </properties>
   ```

2. **Deploy**
   ```bash
   mvn clean package appengine:deploy
   ```

   Your API will be available at: `https://your-project-id.appspot.com`

### Project Structure

```
phisix/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/googlecode/phisix/api/
│   │   │       ├── client/          # PSE website scraping clients
│   │   │       ├── ext/             # JAX-RS providers & filters  
│   │   │       ├── parser/          # HTML & JSON parsers
│   │   │       ├── repository/      # Data access layer
│   │   │       └── resource/        # REST API endpoints
│   │   ├── resources/               # Configuration files
│   │   └── webapp/
│   │       ├── WEB-INF/            # Web app configuration
│   │       └── schema/             # XSD schemas
│   └── test/                       # Unit & integration tests
├── target/                         # Build output
├── pom.xml                        # Maven configuration
└── README.md                      # This file
```

### Technology Stack

- **Runtime**: Java 17
- **Framework**: JAX-RS (RESTEasy)
- **Platform**: Google App Engine Standard
- **Build Tool**: Maven
- **Data Binding**: JAXB (XML), Gson (JSON)
- **Web Scraping**: JSoup
- **Testing**: JUnit, Mockito

### API Endpoints

Once running, the following endpoints will be available:

### Resources Summary

**Stocks**: look up stocks

    GET http://phisix-api.appspot.com/stocks.{json|xml}
    GET http://phisix-api.appspot.com/stocks/{symbol}.{json|xml}
    GET http://phisix-api.appspot.com/stocks/{symbol}.{yyyy-MM-dd}.{json|xml}

### JSON feeds

<table>
	<tr>
		<th>Symbol</th>
		<th>Name</th>
		<th>URL</th>
	</tr>
	<tr>
		<td>-</td>
		<td>ALL</td>
		<td><a class="externalLink" href="http://phisix-api.appspot.com/stocks.json">/stocks.json</a></td>
	</tr>
	<tr>
		<td>BDO</td>
		<td>BDO UNIBANK, INC.</td>
		<td><a class="externalLink" href="http://phisix-api.appspot.com/stocks/BDO.json">/stocks/BDO.json</a></td>
	</tr>
	<tr>
		<td>BPI</td>
		<td>BANK OF THE PHILIPPINE ISLANDS</td>
		<td><a class="externalLink" href="/stocks/BPI.json">/stocks/BPI.json</a></td>
	</tr>
	<tr>
		<td>CHIB</td>
		<td>CHINA BANKING CORPORATION</td>
		<td><a class="externalLink" href="http://phisix-api.appspot.com/stocks/CHIB.json">/stocks/CHIB.json</a></td>
	</tr>
	<tr>
		<td>COL</td>
		<td>COL FINANCIAL GROUP, INC.</td>
		<td><a class="externalLink" href="http://phisix-api.appspot.com/stocks/COL.json">/stocks/COL.json</a></td>
	</tr>
	<tr>
		<td>EW</td>
		<td>EAST WEST BANKING CORPORATION</td>
		<td><a class="externalLink" href="http://phisix-api.appspot.com/stocks/EW.json">/stocks/EW.json</a></td>
	</tr>
</table>

### XML feeds

<table>
	<tr>
		<th>Symbol</th>
		<th>Name</th>
		<th>URL</th>
	</tr>
	<tr>
		<td>-</td>
		<td>ALL</td>
		<td><a class="externalLink" href="http://phisix-api.appspot.com/stocks.xml">/stocks.xml</a></td>
	</tr>
	<tr>
		<td>MBT</td>
		<td>METROPOLITAN BANK &amp; TRUST COMPANY</td>
		<td><a class="externalLink" href="http://phisix-api.appspot.com/stocks/MBT.xml">/stocks/MBT.xml</a></td>
	</tr>
	<tr>
		<td>PNB</td>
		<td>PHILIPPINE NATIONAL BANK</td>
		<td><a class="externalLink" href="http://phisix-api.appspot.com/stocks/PNB.xml">/stocks/PNB.xml</a></td>
	</tr>
	<tr>
		<td>PSE</td>
		<td>THE PHILIPPINE STOCK EXCHANGE, INC.</td>
		<td><a class="externalLink" href="http://phisix-api.appspot.com/stocks/PSE.xml">/stocks/PSE.xml</a></td>
	</tr>
	<tr>
		<td>RCB</td>
		<td>RIZAL COMMERCIAL BANKING CORPORATION</td>
		<td><a class="externalLink" href="http://phisix-api.appspot.com/stocks/RCB.xml">/stocks/RCB.xml</a></td>
	</tr>
	<tr>
		<td>SECB</td>
		<td>SECURITY BANK CORPORATION</td>
		<td><a class="externalLink" href="http://phisix-api.appspot.com/stocks/SECB.xml">/stocks/SECB.xml</a></td>
	</tr>
</table>

### XSD

[http://phisix-api.appspot.com/schema/stocks/phisix-stocks.xsd](http://phisix-api.appspot.com/schema/stocks/phisix-stocks.xsd)
