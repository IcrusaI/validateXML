import javax.xml.transform.TransformerException;
import java.io.File;
import java.util.Scanner;

public class App {
    private static final String DEFAULT_SOURCE_PATH = "src/main/resources/bookcase.xml";
    private static final String DEFAULT_OUTPUT_PATH = "src/main/resources/bookcase_result.xml";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите путь к исходному документу (по умолчанию: " + DEFAULT_SOURCE_PATH + "): ");

        String sourcePath = scanner.nextLine();
        if (sourcePath.equals("")) {
            sourcePath = DEFAULT_SOURCE_PATH;
        }

        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()){
            System.err.println("Файл не найден.");
            return;
        }

        System.out.print("Введите путь к результирущему документу (по умолчанию: " + DEFAULT_OUTPUT_PATH + "): ");

        String outputPath = scanner.nextLine();
        if (outputPath.equals("")) {
            outputPath = DEFAULT_OUTPUT_PATH;
        }

        File outputFile = new File(outputPath);

        BookcaseValidator validator = new BookcaseValidator();

        try {
            validator.validator(sourceFile, outputFile);
        } catch (TransformerException e) {
            System.err.println("Произошла внутренняя ошибка.");
        }
    }
}
