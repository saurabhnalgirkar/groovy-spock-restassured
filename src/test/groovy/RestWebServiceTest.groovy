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
    long value1
    @Shared
    long value2
    @Shared
    long moreThanIntMax = (Integer.MAX_VALUE as long) + 4
    @Shared
    long lessThanIntMin = (Integer.MIN_VALUE as long) - 4

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

        if (statusCode == 200) {
            def result = response.then().extract().path("result") as long
            reportInfo("$value1 $operation $value2 = " + result)
            println("$value1 $operation $value2 = " + result)

            // This check will be necessary once the "results" start handling overflow and underflow.
            // Currently this condition is never met
            if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
                throw new Exception("The number ${result} is beyond Java integer limit")
            }
        }

        then: 'Validate Response'
        response.then()
                .log().body()
                .body(Matchers.notNullValue())
                .statusCode(statusCode)
                .spec(responseValidation)

        where: 'Test Data Specification'
        operation   | value1                                    | value2                                  | statusCode | responseValidation
        "add"       | ResourceHelper.getRandomNumber(-100, 100) | ResourceHelper.getRandomNumber(-10, 50) | 200        | expect().body("result", Matchers.equalTo(Math.addExact(value1,value2)))
        "subtract"  | ResourceHelper.getRandomNumber(-100, 100) | ResourceHelper.getRandomNumber(-10, 50) | 200        | expect().body("result", Matchers.equalTo(Math.subtractExact(value1,value2)))
        "multiply"  | ResourceHelper.getRandomNumber(-100, 100) | ResourceHelper.getRandomNumber(-10, 50) | 200        | expect().body("result", Matchers.equalTo(Math.multiplyExact(value1,value2)))
        "divide"    | ResourceHelper.getRandomNumber(-100, 100) | ResourceHelper.getRandomNumber(10, 30)  | 200        | expect().body("result", Matchers.equalTo(value1.intdiv(value2)))

        // Edge Cases
        "add"       | moreThanIntMax                            | ResourceHelper.getRandomNumber(-10, 30) | 404        | expect().body(Matchers.notNullValue())
        "add"       | ResourceHelper.getRandomNumber(-10, 30)   | lessThanIntMin                          | 404        | expect().body(Matchers.notNullValue())
        "divide"    | ResourceHelper.getRandomNumber(-10, 30)   | 0                                       | 500        | expect().body(Matchers.notNullValue())
//        "add"       | Integer.MAX_VALUE as long                 | 4                                       | 400        | expect().body(Matchers.notNullValue())
//        "subtract"  | Integer.MIN_VALUE as long                 | 4                                       | 400        | expect().body(Matchers.notNullValue())

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

        if (statusCode == 200) {
            def result = response.then().extract().path("result") as long
            reportInfo("$value1 $operation $value2 = " + result)
            println("$value1 $operation $value2 = " + result)

            // This check will be necessary once the "results" start handling overflow and underflow.
            // Currently this condition is never met
            if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
                throw new Exception("The number ${result} is beyond Java integer limit")
            }
        }

        then: 'Validate Response'
        response.then()
                .log().body()
                .body(Matchers.notNullValue())
                .statusCode(statusCode)
                .spec(responseValidation)

        where: 'Test Data Specification'
        operation | value1                                    | value2                                  | statusCode | responseValidation
        "add"     | ResourceHelper.getRandomNumber(-100, 100) | ResourceHelper.getRandomNumber(-10, 50) | 200        | expect().body("result", Matchers.equalTo(Math.addExact(value1,value2)))
        "sub"     | ResourceHelper.getRandomNumber(-100, 100) | ResourceHelper.getRandomNumber(-10, 50) | 200        | expect().body("result", Matchers.equalTo(Math.subtractExact(value1,value2)))
        "mul"     | ResourceHelper.getRandomNumber(-100, 100) | ResourceHelper.getRandomNumber(-10, 50) | 200        | expect().body("result", Matchers.equalTo(Math.multiplyExact(value1,value2)))
        "div"     | ResourceHelper.getRandomNumber(-100, 100) | ResourceHelper.getRandomNumber(10, 30)  | 200        | expect().body("result", Matchers.equalTo(value1.intdiv(value2)))

        // Edge Cases
        "add"       | moreThanIntMax                            | ResourceHelper.getRandomNumber(-10, 30) | 400        | expect().body(Matchers.containsString("Numeric value ($value1) out of range of int"))
        "add"       | ResourceHelper.getRandomNumber(-10, 30)   | lessThanIntMin                          | 400        | expect().body(Matchers.containsString("Numeric value ($value2) out of range of int"))
        "div"       | ResourceHelper.getRandomNumber(-10, 30)   | 0                                       | 500        | expect().body(Matchers.notNullValue())
//        "add"       | Integer.MAX_VALUE as long                 | 4                                       | 400        | expect().body(Matchers.notNullValue())
//        "sub"       | Integer.MIN_VALUE as long                 | 4                                       | 400        | expect().body(Matchers.notNullValue())
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


