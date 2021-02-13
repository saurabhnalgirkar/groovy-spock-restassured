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


}
