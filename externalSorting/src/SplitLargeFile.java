import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

/* splitting a large input file into chunks as temporary files */
public class SplitLargeFile extends Thread {

    private final String fileName;
    private final int startLine;
    private final int endLine;

    SplitLargeFile(String fileName, int startLine, int endLine) {
        this.fileName = fileName;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public void run() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            int totalLines = endLine + 1 - startLine;
            // splitting a large input file into chunks
            Stream<String> chunks = Files.lines(Paths.get(ExternalSorting.fPath))
                            .skip(startLine - 1)
                            .limit(totalLines)
                            .sorted(Comparator.naturalOrder());
            // converting chunks as temporary files
            chunks.forEach(line -> writeToFile(writer, line));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(BufferedWriter writer, String line) {
        try {
            writer.write(line + "\r\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}