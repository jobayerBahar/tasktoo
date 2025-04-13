import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;

public class xmlReaderApp {
    public static void main(String[] args) {
        try {
            // Replace with the path to your XML file
            File file = new File("practical_2.xml");  // Ensure this file is in the right place

            if (!file.exists()) {
                System.out.println("XML file does not exist!");
                return;
            }

            // Initialize a DocumentBuilderFactory and parse the file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize(); // Normalize the document

            System.out.println("Parsing complete. Checking for 'record' elements...");

            // Get all "record" elements from the XML
            NodeList nodeList = doc.getElementsByTagName("record");

            // Check if we have any 'record' entries
            if (nodeList.getLength() == 0) {
                System.out.println("No 'record' elements found in the XML.");
            }

            // Iterate through the list and print each element's field values
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Extract the field values for each <record>
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    String postalZip = element.getElementsByTagName("postalZip").item(0).getTextContent();
                    String region = element.getElementsByTagName("region").item(0).getTextContent();
                    String country = element.getElementsByTagName("country").item(0).getTextContent();
                    String address = element.getElementsByTagName("address").item(0).getTextContent();
                    String list = element.getElementsByTagName("list").item(0).getTextContent();

                    // Print the field values to the console
                    System.out.println("Name: " + name);
                    System.out.println("Postal Code: " + postalZip);
                    System.out.println("Region: " + region);
                    System.out.println("Country: " + country);
                    System.out.println("Address: " + address);
                    System.out.println("List: " + list);
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred:");
            e.printStackTrace();
        }
    }
}
