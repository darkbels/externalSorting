import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/* generating a large input file with random data */
class GenerateRandomDataFile {

    void generateRandomFile(int minLength, int maxLength, int linesFile) {
        StringBuilder result = new StringBuilder();
        for (long i = 0; i < linesFile; i++) {
            // generating random characters in a string
            String generatedString = getRandomStringChars(minLength, maxLength);
            if (i == 0) {
                result.append(generatedString);
            } else {
                result.append(System.getProperty("line.separator")).append(generatedString);
            }
        }
        try (FileWriter writer = new FileWriter(ExternalSorting.fPath); BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(String.valueOf(result));
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private String getRandomStringChars(int minLength, int maxLength) {
        Random random = new Random();
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        return random.ints(leftLimit, rightLimit + 1)
                // excluding Unicode characters out of range (0-9, A-Z, a-z)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                // generating random string length
                .limit(getRandomStringLength(minLength, maxLength))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private int getRandomStringLength(int minLength, int maxLength) {
        Random random = new Random();
        return random.nextInt(maxLength + minLength) + minLength;
    }
}