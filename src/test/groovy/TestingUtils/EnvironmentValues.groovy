package TestingUtils

class EnvironmentValues {

    public String Env
    public String ServiceBaseURI
    public String ServiceBasePath
    public String SelectBrowser
    public String Browser

    private EnvironmentValues() {}

    public static EnvironmentValues getInstance() {

        EnvironmentValues TheInstance

        if (TheInstance == null ) {
            TheInstance = new EnvironmentValues(
            ServiceBaseURI: System.properties["baseUri"],
            ServiceBasePath: System.properties["basePath"],
            SelectBrowser: System.properties["selectBrowser"],
            Env: System.properties["env"]
            )
            // Environment Configuration is defined here
            switch (TheInstance.Env) {
                case "local":
                    TheInstance.ServiceBaseURI = "http://localhost:8080/"
                    TheInstance.ServiceBasePath = "qa_testCalc_java11/"
                    break
            }

            // Selenide WebDriver Configuration is defined here
            switch (TheInstance.SelectBrowser) {
                case "safari":
                    TheInstance.Browser = "org.openqa.selenium.safari.SafariDriver"
                    break
                case "chrome":
                    TheInstance.Browser = "org.openqa.selenium.chrome.ChromeDriver"
                    break
                case "firefox":
                    TheInstance.Browser = "org.openqa.selenium.firefox.FirefoxDriver"
                    break
            }
        }
        return TheInstance
    }
}
