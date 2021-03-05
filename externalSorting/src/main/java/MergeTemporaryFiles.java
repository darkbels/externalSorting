import java.io.*;
import java.nio.file.Files;

/* merging generated temporary files */
public class MergeTemporaryFiles extends Thread {

    private final String file1;
    private final String file2;
    private final String file3;

    MergeTemporaryFiles(String file1, String file2, String file3) {
        this.file1 = file1;
        this.file2 = file2;
        this.file3 = file3;
    }

    public void run() {
        try (FileReader fileReader1 = new FileReader(file1);
             FileReader fileReader2 = new FileReader(file2);
             FileWriter writer = new FileWriter(file3);
             BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
             BufferedReader bufferedReader2 = new BufferedReader(fileReader2)) {
            String line1 = bufferedReader1.readLine();
            String line2 = bufferedReader2.readLine();
            // merge two temporary files based on whose line is greater
            while (line1 != null || line2 != null) {
                if (line1 == null || (line2 != null && line1.compareTo(line2) > 0)) {
                    writer.write(line2 + "\r\n");
                    line2 = bufferedReader2.readLine();
                } else {
                    writer.write(line1 + "\r\n");
                    line1 = bufferedReader1.readLine();
                }
            }
            // deleting temporary files
            delete(new File(file1), new File(file2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delete(final File file1, final File file2) throws IOException {
        Files.delete(file1.toPath());
        Files.delete(file2.toPath());
    }
}