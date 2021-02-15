import TestingUtils.EnvironmentValues
import TestingUtils.ResourceHelper
import io.restassured.path.xml.XmlPath
import org.hamcrest.Matchers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

import static io.restassured.RestAssured.*

@Stepwise
class SoapWebServiceTest extends Specification {

    @Shared
    int statusCode
    @Shared
    String requestBodyPath
    @Shared
    String result

    def setupSpec() {
        baseURI = EnvironmentValues.instance.ServiceBaseURI
        basePath = EnvironmentValues.instance.ServiceBasePath
    }

    def 'Validate WSDL Specification'() {
        given: 'Setup Request'

        def request = given()
                .header("Content-Type", "text/xml")
                .log().uri()
                .log().method()

        when: 'GET Request'
        def response = request.when()
                .get("/soapWS?wsdl")

        def expectedWsdl = ResourceHelper.loadResourceContent("src/test/resources/sampleWsdl.xml")
        response.asString() == expectedWsdl

        then: 'Validate Response'
        response.then()
                .log().body()
                .body(Matchers.notNullValue())
                .statusCode(200)
    }

    @Unroll
    def 'SoapWS Test for #requestBodyPath'() {
        given: 'Setup Request'
        def rawBody = ResourceHelper.loadResourceContent(requestBodyPath)

        def request = given()
                .header("Content-Type", "text/xml")
                .body(rawBody)
                .log().uri()
                .log().method()

        when: 'POST Request'
        def response = request.when()
                .post("/soapWS")

        XmlPath xmlPath = new XmlPath(response.asString())

        switch (requestBodyPath) {
            case "src/test/resources/addSoapRequest.xml":
                result = xmlPath.getString("addResponse")
                assert result == "10"
                break
            case "src/test/resources/subSoapRequest.xml":
                result = xmlPath.getString("subtractResponse")
                assert result == "80"
                break
            case "src/test/resources/mulSoapRequest.xml":
                result = xmlPath.getString("multiplyResponse")
                assert result == "100"
                break
            case "src/test/resources/divSoapRequest.xml":
                result = xmlPath.getString("divideResponse")
                assert result == "5"
                break
            case "src/test/resources/invalidSoapRequest.xml":
                result = xmlPath.getString("Fault")
                assert result == "S:Server/ by zero"
                break
        }

        then: 'Validate Response'
        response.then()
                .log().body()
                .body(Matchers.notNullValue())
                .statusCode(statusCode)

        where:
        requestBodyPath                             | statusCode
        "src/test/resources/addSoapRequest.xml"     | 200
        "src/test/resources/subSoapRequest.xml"     | 200
        "src/test/resources/mulSoapRequest.xml"     | 200
        "src/test/resources/divSoapRequest.xml"     | 200
        "src/test/resources/invalidSoapRequest.xml" | 500

    }

}