import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class BookcaseValidator {
    private DocumentBuilder documentBuilder;

    BookcaseValidator() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(true);

        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void validator(File sourceFile, File outputFile) throws TransformerException {
        Document document;
        try {
            document = documentBuilder.parse(sourceFile);
        } catch (Exception e) {
            System.err.println("Не удалось считать файл. Проверьте его валидность.");
            return;
        }

        NodeList bookshelves = document.getElementsByTagName("bookshelf");

        for (int i = 0; i < bookshelves.getLength(); i++) {
            Element bookshelf = (Element) bookshelves.item(i);

            NodeList books = bookshelf.getElementsByTagName("book");
            for (int j = 0; j < books.getLength(); j++) {
                Element book = (Element) books.item(j);

                int countPages = getCountPageInBook(book);

                NodeList pages = book.getElementsByTagName("pages");

                Node page = pages.item(0);

                if (page == null) {
                    page = document.createElement("pages");
                    book.appendChild(page);
                }

                page.setTextContent(String.valueOf(countPages));
            }
        }

        saveOnDisk(document, outputFile);
    }

    private int getCountPageInBook(Element book) {
        NodeList chapters = book.getElementsByTagName("chapter");
        Element intro = (Element) book.getElementsByTagName("intro").item(0);

        int count = 0;

        for (int i = 0; i < chapters.getLength(); i++) {
            Element chapter = (Element) chapters.item(i);

            int pages = Integer.parseInt(chapter.getAttribute("pages"));

            count += pages;
        }

        int introPages = Integer.parseInt(intro.getAttribute("pages"));

        count += introPages;

        return count;
    }

    private void saveOnDisk(Document doc, File output) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(output));
    }
}
