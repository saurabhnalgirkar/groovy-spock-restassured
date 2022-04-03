# Task

API and UI Automated Validation for a calculator API using Groovy, Spock and REST-Assured.

The is an application that allows user to perform arithmetical operations on two arguments in the enormous range of Java int. 
No need to use human brain again!

# Documentation
Groovy : https://groovy-lang.org/single-page-documentation.html

Spock : http://spockframework.org/spock/docs/1.1-rc-1/all_in_one.html

REST-Assured : https://github.com/rest-assured/rest-assured/wiki/Usage

# Pre-requisites
This project assumes following pre-requisites are installed and the application is running locally.
- Java
- Maven
- Apache Tomcat

All other dependencies are bundled in `pom.xml`

# Instructions
The source `src/test/groovy` consists of three scripts with multiple Spock features.
Each Spock feature consists of sub-features which are equivalent of one test.
- REST Web Service : `RestWebServiceTest.groovy`
- SOAP Web Service: `SoapWebServiceTest.groovy`
- Web UI : `WebUserInterfaceTest.groovy`

Following is a sample maven command to build the entire test suite.

`mvn -Denv="local" -DselectBrowser="safari" clean install`

The `-DselectBrowser` takes the following argument assuming the respective web driver is installed.
- "safari"

The `-Denv` argument only takes "local" for now.

# Reports
Reports are generated in `target/spock-reports` folder after each build.

Sample Report :
<!DOCTYPE html><html>
<head>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8'></meta>
</head>
<body>
<h2>Specification run results</h2>
<hr></hr>
<div class='project-header'>
<span class='project-name'>Project: Ataccama Calc Task | </span>
<span class='project-version'>Version: 2021-02-15-16-24-44</span>
</div>
<div class='summary-report'>
<h3>Specifications summary:</h3>
<div class='date-test-ran'>Created on Mon Feb 15 16:25:13 CET 2021 by saurabhnalgirkar</div>
<table class='summary-table'>
<thead>
<th>Total</th>
<th>Passed</th>
<th>Failed</th>
<th>Feature failures</th>
<th>Feature errors</th>
<th>Success rate</th>
<th>Total time</th>
</thead>
<tbody>
<tr>
<td>3</td>
<td>3</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100.0%</td>
<td>27.341 seconds</td>
</tr>
</tbody>
</table>
</div>
<h3>Specifications:</h3>
<table class='summary-table'>
<thead>
<th>Name</th>
<th>Features</th>
<th>Failed</th>
<th>Errors</th>
<th>Skipped</th>
<th>Success rate</th>
<th>Time</th>
</thead>
<tbody>
<tr>
<td>
<a href='RestWebServiceTest.html'>RestWebServiceTest</a>
</td>
<td>3</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100.0%</td>
<td>3.255 seconds</td>
</tr>
<tr>
<td>
<a href='SoapWebServiceTest.html'>SoapWebServiceTest</a>
</td>
<td>2</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100.0%</td>
<td>2.318 seconds</td>
</tr>
<tr>
<td>
<a href='WebUserInterfaceTest.html'>WebUserInterfaceTest</a>
</td>
<td>3</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100.0%</td>
<td>21.768 seconds</td>
</tr>
</tbody>
</table>
<hr></hr>
<div class='footer'>Generated by <a href='https://github.com/renatoathaydes/spock-reports'>Athaydes Spock Reports</a></div>
</body>
</html>

** Note: The links in the report are for illustrative purposes only.
