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

            // Create a Gson object for later conversion to JSON
            Gson gson = new Gson();
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Prompt the user to enter comma-separated field names or type "exit" to quit
                System.out.println("Enter comma-separated field names (e.g., name, postalZip, country), or type 'exit' to quit:");
                String userInput = scanner.nextLine().trim();

                // Allow the user to exit by typing "exit"
                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting the program.");
                    break;
                }

                // Split the input into an array of field names
                String[] fieldsToInclude = userInput.split("\\s*,\\s*");

                // Set up a flag to check if any invalid field exists
                boolean invalidFieldFound = false;
                HashMap<String, String> missingFields = new HashMap<>();

                // Pre-check if any requested fields do not exist in the XML structure
                for (String field : fieldsToInclude) {
                    boolean fieldExists = false;
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            if (element.getElementsByTagName(field).getLength() > 0) {
                                fieldExists = true;
                                break;
                            }
                        }
                    }
                    if (!fieldExists) {
                        missingFields.put(field, "Error: Field not found");
                        invalidFieldFound = true;
                    }
                }

                // If any fields are missing, print the error message and prompt the user again
                if (invalidFieldFound) {
                    System.out.println("Error: One or more fields were not found. Please try again.");
                    continue; // Skip to the next iteration and prompt the user again
                }

                // Process each record and output the selected fields in JSON format
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        // Create a HashMap to store the field names and their values for this record
                        HashMap<String, String> recordMap = new HashMap<>();

                        // Loop through each requested field
                        for (String field : fieldsToInclude) {
                            field = field.trim(); // Trim any spaces around field names

                            // Check if the field exists and add it to the map
                            if (element.getElementsByTagName(field).getLength() > 0) {
                                String fieldValue = element.getElementsByTagName(field).item(0).getTextContent();
                                recordMap.put(field, fieldValue);
                            } else {
                                // If field doesn't exist, add the error message
                                recordMap.put(field, "Error: Field not found");
                            }
                        }

                        // Convert the HashMap to a JSON string and print it
                        String jsonOutput = gson.toJson(recordMap);
                        System.out.println(jsonOutput);
                    }
                }

                // Exit the loop after processing the valid fields
                break;

            }

        } catch (Exception e) {
            // Print the error in a single line
            System.out.println("Error: " + e.getMessage());
        }
    }
}
