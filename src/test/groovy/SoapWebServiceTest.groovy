import TestingUtils.EnvironmentValues
import TestingUtils.ResourceHelper
import io.restassured.specification.ResponseSpecification
import org.hamcrest.Matchers
import org.json.JSONObject
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import static io.restassured.RestAssured.*

@Ignore
class SoapWebServiceTest extends Specification {

    @Shared
    int statusCode
    @Shared
    ResponseSpecification responseValidation
    @Shared
    String operation
    @Shared
    int value1
    @Shared
    int value2

    def setupSpec() {
        baseURI = "http://soap.testcalc.qa.ataccama.com/"
        basePath = "TestCalcSoapImplementationService"
    }

    @Unroll
    def 'GET Method Test for #operation'() {
        given: 'Setup Request'
        def request = given()
                .header("Content-Type", "application/json")
                .log().uri()
                .log().method()

        when: 'GET Request'
        def response = request.when()
                .get()

        then: 'Validate Response'
        response.then()
                .log().body()
                .body(Matchers.notNullValue())
                .statusCode(200)
//                .spec(responseValidation)

//        where: 'Test Data Specification'
//        operation  | value1 | value2 | statusCode | responseValidation
//        "add"      | 50     | 20     | 200        | expect().body("result", Matchers.equalTo(value1 + value2))
//        "subtract" | 800    | 141    | 200        | expect().body("result", Matchers.equalTo(value1 - value2))
//        "multiply" | 92     | 92     | 200        | expect().body("result", Matchers.equalTo(value1 * value2))
//        "divide"   | 90     | 15     | 200        | expect().body("result", Matchers.equalTo(value1.intdiv(value2)))
    }

}


