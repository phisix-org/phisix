phisix
======
[![Build Status](https://travis-ci.org/edgedalmacio/phisix.svg?branch=master)](https://travis-ci.org/edgedalmacio/phisix)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.googlecode.phisix%3Aphisix&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.googlecode.phisix%3Aphisix)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.googlecode.phisix%3Aphisix&metric=coverage)](https://sonarcloud.io/dashboard?id=com.googlecode.phisix%3Aphisix)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b523693e36944745bd3e2072d380a272)](https://www.codacy.com/manual/edgedalmacio/phisix?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=edgedalmacio/phisix&amp;utm_campaign=Badge_Grade)
[![Dependabot Status](https://api.dependabot.com/badges/status?host=github&repo=edgedalmacio/phisix)](https://dependabot.com)
[![Mergify Status](https://img.shields.io/endpoint.svg?url=https://gh.mergify.io/badges/edgedalmacio/phisix&style=flat)](https://mergify.io)

[![Status](https://statuspage.freshping.io/badge/2f4f0c1e-4d5a-45b1-bf86-a0cbbccaa74b?0.9828701333113494)](https://statuspage.freshping.io/29889-phisixapi)
-----

Simple PSEi (formerly known as PHISIX) RESTful API hosted on Google AppEngine

###Resources Summary###

**Stocks**: look up stocks

    GET http://phisix-api.appspot.com/stocks.{json|xml}
    GET http://phisix-api.appspot.com/stocks/{symbol}.{json|xml}
    GET http://phisix-api.appspot.com/stocks/{symbol}.{yyyy-MM-dd}.{json|xml}

###JSON feeds###

<table>
	<tr>
		<th>Symbol</th>
		<th>Name</th>
		<th>URL</th>
	</tr>
	<tr>
		<td>-</td>
		<td>ALL</td>
		<td><a class="externalLink" href="/stocks.json">/stocks.json</a></td>
	</tr>
	<tr>
		<td>BDO</td>
		<td>BDO UNIBANK, INC.</td>
		<td><a class="externalLink" href="/stocks/BDO.json">/stocks/BDO.json</a></td>
	</tr>
	<tr>
		<td>BPI</td>
		<td>BANK OF THE PHILIPPINE ISLANDS</td>
		<td><a class="externalLink" href="/stocks/BPI.json">/stocks/BPI.json</a></td>
	</tr>
	<tr>
		<td>CHIB</td>
		<td>CHINA BANKING CORPORATION</td>
		<td><a class="externalLink" href="/stocks/CHIB.json">/stocks/CHIB.json</a></td>
	</tr>
	<tr>
		<td>COL</td>
		<td>COL FINANCIAL GROUP, INC.</td>
		<td><a class="externalLink" href="/stocks/COL.json">/stocks/COL.json</a></td>
	</tr>
	<tr>
		<td>EW</td>
		<td>EAST WEST BANKING CORPORATION</td>
		<td><a class="externalLink" href="/stocks/EW.json">/stocks/EW.json</a></td>
	</tr>
</table>

###XML feeds###

<table>
	<tr>
		<th>Symbol</th>
		<th>Name</th>
		<th>URL</th>
	</tr>
	<tr>
		<td>-</td>
		<td>ALL</td>
		<td><a class="externalLink" href="/stocks.xml">/stocks.xml</a></td>
	</tr>
	<tr>
		<td>MBT</td>
		<td>METROPOLITAN BANK &amp; TRUST COMPANY</td>
		<td><a class="externalLink" href="/stocks/MBT.xml">/stocks/MBT.xml</a></td>
	</tr>
	<tr>
		<td>PNB</td>
		<td>PHILIPPINE NATIONAL BANK</td>
		<td><a class="externalLink" href="/stocks/PNB.xml">/stocks/PNB.xml</a></td>
	</tr>
	<tr>
		<td>PSE</td>
		<td>THE PHILIPPINE STOCK EXCHANGE, INC.</td>
		<td><a class="externalLink" href="/stocks/PSE.xml">/stocks/PSE.xml</a></td>
	</tr>
	<tr>
		<td>RCB</td>
		<td>RIZAL COMMERCIAL BANKING CORPORATION</td>
		<td><a class="externalLink" href="/stocks/RCB.xml">/stocks/RCB.xml</a></td>
	</tr>
	<tr>
		<td>SECB</td>
		<td>SECURITY BANK CORPORATION</td>
		<td><a class="externalLink" href="/stocks/SECB.xml">/stocks/SECB.xml</a></td>
	</tr>
</table>

###XSD###

[http://phisix-api.appspot.com/schema/stocks/phisix-stocks.xsd](/schema/stocks/phisix-stocks.xsd)
