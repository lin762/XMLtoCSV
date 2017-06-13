import javax.xml.parsers.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.w3c.dom.*;
import java.io.*;
import javafx.*;

public class ReadXMLFile {

    public static void main(String argv[]) {

        FileWriter writer = null;
        try{
            writer = new FileWriter("new.csv");
            writer.append("WaferID,SlotNumber,FindX,FindY,LocX,LocY,TN,LocationX,LocationY,MisX,MisY,TisX,TisY");
            writer.append("\n");

        }catch(IOException e){
            e.printStackTrace();
        }


        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            FileWriter finalWriter = writer;
            DefaultHandler handler = new DefaultHandler() {


                String lines = "";

                String waferID;
                String slotNumber;

                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {


                    if (qName.equalsIgnoreCase("WAFERBASE")) {
                        waferID = new String(attributes.getValue(6));
                        slotNumber = new String(attributes.getValue(5));

                    }

                    if (qName.equalsIgnoreCase("MEASBASE")) {
                        String findX = new String(attributes.getValue(2));
                        String findY = new String(attributes.getValue(3));
                        String tn = new String(attributes.getValue(8));

                        try {
                            File inputFile = new File("test.xml");
                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            Document doc = dBuilder.parse(inputFile);
                            doc.getDocumentElement().normalize();

                            NodeList nList = doc.getElementsByTagName("FieldBase");

                            for (int temp = 0; temp < nList.getLength(); temp++) {
                                Node nNode = nList.item(temp);

                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement = (Element) nNode;

                                    String indX = eElement.getAttribute("IndX");
                                    String indY = eElement.getAttribute("IndY");

                                    if (findX.equals(indX) && findY.equals(indY)) {
                                        String locX = eElement.getAttribute("LocX");
                                        String locY = eElement.getAttribute("LocY");
                                        lines += waferID + "," + slotNumber + "," + findX + "," + findY + "," + locX + "," + locY + ",";
                                    }

                                }
                            }

                            nList = doc.getElementsByTagName("TargetBase");

                            for (int temp = 0; temp < nList.getLength(); temp++) {
                                Node nNode = nList.item(temp);

                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement = (Element) nNode;

                                    String testNum = eElement.getAttribute("TestNumber");

                                    if (tn.equals(testNum)) {
                                        String locationX = eElement.getAttribute("LocationX");
                                        String locationY = eElement.getAttribute("LocationY");
                                        lines += tn + "," + locationX + "," + locationY + ",";
                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (qName.equalsIgnoreCase("OVL")) {
                        String misX = new String(attributes.getValue(0));
                        String misY = new String(attributes.getValue(1));
                        String tisX = new String(attributes.getValue(3));
                        String tisY = new String(attributes.getValue(4));
                        lines += misX + "," + misY + "," + tisX + "," + tisY;

                        try{
                            finalWriter.append(lines);
                            finalWriter.append("\n");
                        }catch(IOException e){
                            e.printStackTrace();
                        }

                        lines = "";
                    }


                }

            };

            saxParser.parse("test.xml", handler);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}