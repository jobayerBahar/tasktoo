import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import com.google.gson.Gson;

public class xmlReaderApp {
    public static void main(String[] args) {
        try {
            // Replace with your XML file's path
            File file = new File("practical_2.xml"); 

            // Initialize a DocumentBuilderFactory and parse the file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize(); // Normalize the document

            // Get all "record" elements from the XML (change "record" to your XML structure)
            NodeList nodeList = doc.getElementsByTagName("record");

            // Prompt the user to enter comma-separated field names
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter comma-separated field names (e.g., name, postalZip, country):");
            String userInput = scanner.nextLine();

            // Split the input into an array of field names
            String[] fieldsToInclude = userInput.split("\\s*,\\s*");

            // Create a Gson object for later conversion to JSON
            Gson gson = new Gson();

            // Iterate through the list and print only the user-selected field values in JSON format
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    
                    // Create a HashMap to store the field names and their values
                    HashMap<String, String> recordMap = new HashMap<>();

                    // Loop through each requested field
                    for (String field : fieldsToInclude) {
                        // Make sure to trim any spaces from the field
                        field = field.trim();

                        // Check if the field exists and add it to the map
                        if (element.getElementsByTagName(field).getLength() > 0) {
                            String fieldValue = element.getElementsByTagName(field).item(0).getTextContent();
                            recordMap.put(field, fieldValue);
                        } else {
                            recordMap.put(field, "Field not found");
                        }
                    }

                    // Convert the HashMap to a JSON string and print it
                    String jsonOutput = gson.toJson(recordMap);
                    System.out.println(jsonOutput);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
