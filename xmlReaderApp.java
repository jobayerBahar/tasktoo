import org.xml.sax.*;
import org.xml.sax.helpers.*;
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

            // Initialize SAXParserFactory and create SAXParser
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // Create a Gson object for later conversion to JSON
            Gson gson = new Gson();
            Scanner scanner = new Scanner(System.in);

            // SAX handler that processes XML elements
            DefaultHandler handler = new DefaultHandler() {
                HashMap<String, String> recordMap = new HashMap<>();
                String currentElement = "";
                boolean isFieldValid = true;

                // Start of element
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    currentElement = qName;
                }

                // End of element
                public void endElement(String uri, String localName, String qName) {
                    if (qName.equals("record")) {
                        // Once a record element ends, output its data as JSON
                        String jsonOutput = gson.toJson(recordMap);
                        System.out.println(jsonOutput);
                        recordMap.clear();  // Clear record map for the next record
                    }
                }

                // Handle element content
                public void characters(char[] ch, int start, int length) {
                    String data = new String(ch, start, length).trim();

                    if (!data.isEmpty() && isFieldValid) {
                        if (currentElement.equals("name") || currentElement.equals("postalZip") ||
                            currentElement.equals("region") || currentElement.equals("country") ||
                            currentElement.equals("address") || currentElement.equals("list")) {
                            recordMap.put(currentElement, data);  // Add the field value to the map
                        }
                    }
                }

                // Method to handle invalid field
                public void error(SAXParseException e) {
                    isFieldValid = false;
                    System.out.println("Error: " + e.getMessage());
                }
            };

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

                // Check if any requested fields do not exist in the XML structure
                for (String field : fieldsToInclude) {
                    boolean fieldExists = false;
                    for (String validField : new String[]{"name", "postalZip", "region", "country", "address", "list"}) {
                        if (field.equals(validField)) {
                            fieldExists = true;
                            break;
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

                // Process the XML using SAX parser
                try {
                    // Parse the XML file using SAX
                    saxParser.parse(file, handler);
                } catch (Exception e) {
                    System.out.println("Error during parsing: " + e.getMessage());
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
