import TestingUtils.EnvironmentValues
import TestingUtils.ResourceHelper
import io.restassured.specification.ResponseSpecification
import org.hamcrest.Matchers
import org.json.JSONObject
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

import static io.restassured.RestAssured.*

@Stepwise
class RestWebServiceTest extends Specification {

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
        baseURI = EnvironmentValues.instance.ServiceBaseURI
        basePath = EnvironmentValues.instance.ServiceBasePath
    }

    @Unroll
    def 'GET Method Test For Value Pair: #value1 #operation #value2'() {
        given: 'Setup Request'
        def request = given()
                .header("Content-Type", "application/json")
                .log().uri().log().method()

        when: 'GET Request'
        def response = request.when()
                .queryParam("val1", value1)
                .queryParam("val2", value2)
                .get("restWS/$operation")

        then: 'Validate Response'
        response.then()
                .log().body()
                .body(Matchers.notNullValue())
                .statusCode(statusCode)
                .spec(responseValidation)

        where: 'Test Data Specification'
        operation   | value1            | value2            | statusCode | responseValidation
        "add"       | 50                | 20                | 200        | expect().body("result", Matchers.equalTo(value1 + value2))
        "subtract"  | 800               | 141               | 200        | expect().body("result", Matchers.equalTo(value1 - value2))
        "multiply"  | 92                | 92                | 200        | expect().body("result", Matchers.equalTo(value1 * value2))
        "divide"    | 90                | 15                | 200        | expect().body("result", Matchers.equalTo(value1.intdiv(value2)))

        "add"       | Integer.MAX_VALUE | Integer.MAX_VALUE | 200        | expect().body("result", Matchers.equalTo(value1 + value2))
        "subtract"  | Integer.MAX_VALUE | Integer.MAX_VALUE | 200        | expect().body("result", Matchers.equalTo(value1 - value2))
        "multiply"  | Integer.MAX_VALUE | Integer.MIN_VALUE | 200        | expect().body("result", Matchers.equalTo(value1 * value2))
        "divide"    | Integer.MAX_VALUE | Integer.MAX_VALUE | 200        | expect().body("result", Matchers.equalTo(value1.intdiv(value2)))
        "aaa"       | 5                 | 4                 | 404        | expect().body(Matchers.notNullValue())

    }

    @Unroll
    def 'POST Method Test For Value Pair: #value1 #operation #value2'() {
        given: 'Setup Request'
        def rawBody = ResourceHelper.loadResourceContent("src/test/resources/requestBody.json")

        def requestBody = new JSONObject(rawBody)
                .put("val1", value1)
                .put("val2", value2)
                .put("operation", operation)
                .toString()
        
        def request = given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().uri().log().method().log().body()

        when: 'POST Request'
        def response = request.when().post("restWS/compute")

        then: 'Validate Response'
        response.then()
                .log().body()
                .body(Matchers.notNullValue())
                .statusCode(statusCode)
                .spec(responseValidation)

        where: 'Test Data Specification'
        operation   | value1            | value2            | statusCode | responseValidation
        "add"       | 50                | 20                | 200        | expect().body("result", Matchers.equalTo(value1 + value2))
        "sub"       | 800               | 141               | 200        | expect().body("result", Matchers.equalTo(value1 - value2))
        "mul"       | 92                | 92                | 200        | expect().body("result", Matchers.equalTo(value1 * value2))
        "div"       | 90                | 15                | 200        | expect().body("result", Matchers.equalTo(value1.intdiv(value2)))

        "add"       | Integer.MAX_VALUE | Integer.MAX_VALUE | 200        | expect().body("result", Matchers.equalTo(value1 + value2))
        "sub"       | Integer.MAX_VALUE | Integer.MAX_VALUE | 200        | expect().body("result", Matchers.equalTo(value1 - value2))
        "mul"       | Integer.MAX_VALUE | Integer.MIN_VALUE | 200        | expect().body("result", Matchers.equalTo(value1 * value2))
        "div"       | Integer.MAX_VALUE | Integer.MIN_VALUE | 200        | expect().body("result", Matchers.equalTo(value1.intdiv(value2)))
        "aaa"       | 5                 | 4                 | 500        | expect().body(Matchers.notNullValue())
    }

    def 'Invalid Method Test'() {
        given: 'Setup Request'
        def rawBody = ResourceHelper.loadResourceContent("src/test/resources/requestBody.json")
        def requestBody = new JSONObject(rawBody)
                .put("val1", 42)
                .put("val2", 6)
                .put("operation", "Invalid")
                .toString()

        def putRequest = given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().uri().log().method().log().body()

        def patchRequest = given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().uri().log().method().log().body()

        def deleteRequest = given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .log().uri().log().method()


        when: 'PUT PATCH DELETE Methods Not Supported'
        def putResponse = putRequest.when().put()
        def patchResponse = patchRequest.when().patch()
        def deleteResponse = deleteRequest.when().delete()

        then: 'Validate Response'
        putResponse.then().log().body().statusCode(405)
        patchResponse.then().log().body().statusCode(405)
        deleteResponse.then().log().body().statusCode(405)

    }

}


