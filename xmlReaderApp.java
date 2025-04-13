import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.Scanner;

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
                return;
            }

            // Prompt user for the fields they want to print
            Scanner scanner = new Scanner(System.in);
            System.out.println("Which fields would you like to print?");
            System.out.println("Options: name, postalZip, region, country, address, list");
            System.out.print("Enter comma-separated fields: ");
            String userInput = scanner.nextLine();

            // Split the input into an array of fields to print
            String[] fieldsToPrint = userInput.split(",");

            // Iterate through the list and print each element's selected field values
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // For each selected field, print the corresponding value
                    for (String field : fieldsToPrint) {
                        field = field.trim();  // Remove any extra spaces
                        switch (field) {
                            case "name":
                                System.out.println("Name: " + element.getElementsByTagName("name").item(0).getTextContent());
                                break;
                            case "postalZip":
                                System.out.println("Postal Code: " + element.getElementsByTagName("postalZip").item(0).getTextContent());
                                break;
                            case "region":
                                System.out.println("Region: " + element.getElementsByTagName("region").item(0).getTextContent());
                                break;
                            case "country":
                                System.out.println("Country: " + element.getElementsByTagName("country").item(0).getTextContent());
                                break;
                            case "address":
                                System.out.println("Address: " + element.getElementsByTagName("address").item(0).getTextContent());
                                break;
                            case "list":
                                System.out.println("List: " + element.getElementsByTagName("list").item(0).getTextContent());
                                break;
                            default:
                                System.out.println("Invalid field: " + field);
                        }
                    }
                    System.out.println(); // Blank line between records
                }
            }

            scanner.close(); // Close the scanner after use

        } catch (Exception e) {
            System.out.println("An error occurred:");
            e.printStackTrace();
        }
    }
}
