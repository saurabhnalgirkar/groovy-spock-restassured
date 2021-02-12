package TestingUtils

class ResourceHelper {

    static String loadFileContent(String path) {
        byte[] bytes
        try {
            bytes = ((InputStream)Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path))).readAllBytes()
        } catch (NullPointerException | IOException ignored) {
            bytes = null
        }

        return new String(bytes)
    }

}
