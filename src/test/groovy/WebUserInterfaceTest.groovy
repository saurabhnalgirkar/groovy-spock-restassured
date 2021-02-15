import TestingUtils.EnvironmentValues
import com.codeborne.selenide.Configuration
import org.openqa.selenium.By
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

import static com.codeborne.selenide.Condition.*
import static com.codeborne.selenide.Selenide.*

@Stepwise
class WebUserInterfaceTest extends Specification {

    @Shared
    def completeURL = EnvironmentValues.instance.ServiceBaseURI + EnvironmentValues.instance.ServiceBasePath + "webUI"
    @Shared
    def selectedBrowser = EnvironmentValues.instance.Browser
    @Shared
    String value1
    @Shared
    String value2
    @Shared
    String addResult
    @Shared
    String subResult
    @Shared
    String mulResult
    @Shared
    String divResult

    @Unroll
    def 'Calculator Web UI Test For Value Pair: #value1 and #value2' () {
        given: 'Set Browser to Safari'

        Configuration.browser = selectedBrowser
        open(completeURL)

        and: 'Addition Test'
        $(By.name("val1")).setValue(value1)
        $(By.name("val2")).setValue(value2)
        $$(By.tagName("input"))[2].click()
        $$(By.tagName("input"))[6].click()
        reportInfo("$value1 plus $value2 is: " + $(By.name("result")).getValue())
        $(By.name("result")).shouldHave(value(addResult))

        and: 'Subtraction Test'
        $(By.name("val1")).setValue(value1)
        $(By.name("val2")).setValue(value2)
        $$(By.tagName("input"))[3].click()
        $$(By.tagName("input"))[6].click()
        reportInfo("$value1 minus $value2 is: " + $(By.name("result")).getValue())
        $(By.name("result")).shouldHave(value(subResult))

        and: 'Multiplication Test'
        $(By.name("val1")).setValue(value1)
        $(By.name("val2")).setValue(value2)
        $$(By.tagName("input"))[4].click()
        $$(By.tagName("input"))[6].click()
        reportInfo("$value1 multiplied by $value2 is: " + $(By.name("result")).getValue())
        $(By.name("result")).shouldHave(value(mulResult))

        and: 'Division Test'
        $(By.name("val1")).setValue(value1)
        $(By.name("val2")).setValue(value2)
        $$(By.tagName("input"))[5].click()
        $$(By.tagName("input"))[6].click()
        reportInfo("$value1 divided by $value2 is: " + $(By.name("result")).getValue())
        $(By.name("result")).shouldHave(value(divResult as String))

        where: 'Test Data Specification'
        value1 | value2 | addResult       | subResult       | mulResult                   | divResult
        18     | 6      | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2
        12     | -4     | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2
        -52    | -2     | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2
        0      | 10     | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2
        4      | 7      | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | 0

    }

    def 'Negative Test: Invalid Input Value Fields' () {
        given: 'Set Browser to Safari'

        Configuration.browser = selectedBrowser
        open(completeURL)

        $(By.name("val1")).setValue(value1)
        $(By.name("val2")).setValue(value2)
        $$(By.tagName("input"))[2].click()
        $$(By.tagName("input"))[6].click()
        sleep(100)
        $$(By.tagName("title"))[0].shouldHave(exactText("HTTP Status 500 – Internal Server Error"))

        where: 'Test Data Specification'
        value1 | value2
        "aa"   | 6
        6      | "aa"

    }

    def 'Negative Test: Divide By 0 Test' () {
        given: 'Set Browser to Safari'

        Configuration.browser = selectedBrowser
        open(completeURL)

        $(By.name("val1")).setValue("3")
        $(By.name("val2")).setValue("0")
        $$(By.tagName("input"))[5].click()
        $$(By.tagName("input"))[6].click()
        sleep(100)
        $$(By.tagName("title"))[0].shouldHave(exactText("HTTP Status 500 – Internal Server Error"))

    }

    def 'Negative Test: Input Value1 more than Int Max' () {
        given: 'Set Browser to Safari'

        Configuration.browser = selectedBrowser
        open(completeURL)

        $(By.name("val1")).setValue("2147483652")
        $(By.name("val2")).setValue("1")
        $$(By.tagName("input"))[2].click()
        $$(By.tagName("input"))[6].click()
        sleep(100)
        $$(By.tagName("title"))[0].shouldHave(exactText("HTTP Status 500 – Internal Server Error"))

    }

    def 'Negative Test: Input Value2 more than Int Max' () {
        given: 'Set Browser to Safari'

        Configuration.browser = selectedBrowser
        open(completeURL)

        $(By.name("val1")).setValue("1")
        $(By.name("val2")).setValue("2147483652")
        $$(By.tagName("input"))[2].click()
        $$(By.tagName("input"))[6].click()
        sleep(100)
        $$(By.tagName("title"))[0].shouldHave(exactText("HTTP Status 500 – Internal Server Error"))

    }

}






