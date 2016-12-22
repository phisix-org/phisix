phisix
======
[![Build Status](https://travis-ci.org/edgedalmacio/phisix.svg?branch=master)](https://travis-ci.org/edgedalmacio/phisix)
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=com.googlecode.phisix:phisix)](http://sonar.paymaya.io/dashboard/index/com.googlecode.phisix:phisix)
[![Coverage](https://sonarqube.com/api/badges/measure?key=com.googlecode.phisix:phisix&metric=coverage)](https://sonarqube.com/dashboard/index/com.googlecode.phisix:phisix)

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

###XSD###

[http://phisix-api.appspot.com/schema/stocks/phisix-stocks.xsd](http://phisix-api.appspot.com/schema/stocks/phisix-stocks.xsd)
