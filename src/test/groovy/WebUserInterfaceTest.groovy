import TestingUtils.EnvironmentValues
import com.codeborne.selenide.Configuration
import org.openqa.selenium.By
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static com.codeborne.selenide.Condition.value
import static com.codeborne.selenide.Selenide.*

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
        value1            | value2            | addResult       | subResult       | mulResult                   | divResult
        18                | 6                 | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2
        12                | 4                 | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2
        52                | 2                 | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2
        435               | 5                 | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2
        534               | 2                 | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2

        Integer.MAX_VALUE | Integer.MAX_VALUE | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2
        Integer.MAX_VALUE | Integer.MAX_VALUE | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2
        Integer.MAX_VALUE | Integer.MIN_VALUE | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2
        Integer.MAX_VALUE | Integer.MIN_VALUE | value1 + value2 | value1 - value2 | value1 * (value2 as Number) | value1 / value2

    }

}






