package TestingUtils

class ResourceHelper {

    public static String fileContent = ""

    static String loadResourceContent(String path) {
        def file = new File(path)

        try {
             fileContent = file.text
        } catch (NullPointerException | IOException ignored) {
             fileContent = null
        }
        return fileContent
    }

    static int getRandomNumber(int minAcceptedValue, int maxAcceptedValue) {
        def randomValue = (Math.random() * (maxAcceptedValue - minAcceptedValue + 1) + minAcceptedValue) as Integer
        return randomValue
    }

}
