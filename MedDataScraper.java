import java.net.*;
import java.util.ArrayList;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class MedDataScraper{

	public MedDataScraper() throws Exception{

	//Creating DOM for XML
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        	DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        	Document doc = docBuilder.newDocument();


	//ArrayLists to hold scraped data
		ArrayList<String> companyName = new ArrayList<String>();
	    	ArrayList<String> drugName = new ArrayList<String>();
		ArrayList<String> drugClass = new ArrayList<String>();
		ArrayList<String> indications = new ArrayList<String>();
		ArrayList<String> dosageForm = new ArrayList<String>();
		ArrayList<String> sideEffects = new ArrayList<String>();
		ArrayList<String> warning = new ArrayList<String>();
		

	//Source URLs from where data will be scrapped
		URL[] item=new URL[1];
	       	item[0] = new URL("http://linter.structured-data.org/examples/schema.org/Drug-TreatmentIndication-MedicalContraindication-273-rdfa.html");	

		try{	
		        for(int i=0;i<item.length;i++){

			//Reading URL Stream
		        	BufferedReader in = new BufferedReader(
	                	new InputStreamReader(item[i].openStream()));
	

			//Reading Line by Line
				ArrayList<String> inLines = new ArrayList<String>();

				String inputLine;
	        	    	while ((inputLine = in.readLine()) != null){
					inLines.add(inputLine);
				}
				in.close();
				
			//Checking each line for specific data

				for(int j=0; j<inLines.size(); j++){
				//Manufacturer
					if(inLines.get(j).contains("manufacturer"))
						companyName.add(inLines.get(j+1).split("name\">")[1].split("<")[0].trim());

				//Drug Info
					if(inLines.get(j).contains("Drug Info"))
						drugName.add(inLines.get(j).split("name\">")[1].split("<")[0].trim());

				//Drug Class
					if(inLines.get(j).contains("drugClass"))
						drugClass.add(inLines.get(j+1).split("name\">")[1].split("<")[0].trim());

				//Treatement Indication
					if(inLines.get(j).contains("TreatmentIndication"))
						indications.add(inLines.get(j+1).split("name\">")[1].split("<")[0].trim());

				//Dosage Form
					if(inLines.get(j).contains("form"))
						dosageForm.add(inLines.get(j+1).split("dosageForm\">")[1].split("<")[0].trim());
				
				//Medical Symptoms
					if(inLines.get(j).contains("MedicalSymptom"))
						sideEffects.add(inLines.get(j+1).split("name\">")[1].split("<")[0].trim());

				//Warnings
					if(inLines.get(j).contains("warning"))
						warning.add(inLines.get(j).split("warning\">")[1].split("<")[0].trim());
				}
			}
	
			
		//Print scraped info
			System.out.println("Extracted Drug Info");
			System.out.println("===================");

			System.out.print("Drug Name\t: ");
			printList(drugName);

			System.out.print("Company Name\t: ");
			printList(companyName);

			System.out.print("Drug Class\t: ");
			printList(drugClass);

			System.out.print("Indications\t: ");
			printList(indications);
			
			System.out.print("Dosage Form\t: ");
			printList(dosageForm);
	
			System.out.print("Side Effects\t: ");
			printList(sideEffects);

			System.out.print("Warning(s)\t: ");
			printList(warning);
		

		}catch(Exception ex){
			System.out.println("Halted by the issue : " + ex.getMessage());
			ex.printStackTrace();
		}


		//Saving it to XML

		try{
		//Creating root element in XML
			Element root = doc.createElement("Drugs");
			doc.appendChild(root);
	
	
		//Appending child nodes in XML Root element
		        for(int i=0;i<item.length;i++){
			//Drug Node
				Element drug = doc.createElement("Drug");
				root.appendChild(drug);

			//Drug Name Node
				Element eDrugName = doc.createElement("Drug_Name");
				root.appendChild(eDrugName);
				eDrugName.appendChild( doc.createTextNode(drugName.get(0)));

			//Drug Company Node
				for(String company : companyName){
					Element eCompany = doc.createElement("Company");	
					drug.appendChild(eCompany);
					eCompany.appendChild( doc.createTextNode(company) );
				}

			//Drug Class Node
				Element eDrugClass = doc.createElement("Drug_Classes");
				for(String dgCls : drugClass){
					Element dgC = doc.createElement("Drug_Class");
					eDrugClass.appendChild(dgC);

					dgC.appendChild( doc.createTextNode(dgCls) );
				}
				drug.appendChild(eDrugClass);
		
			//Indications Node
				Element eIndications = doc.createElement("Indications");
				for(String indic : indications){
					Element eIndic = doc.createElement("Indication");
					eIndications.appendChild(eIndic);
	
					eIndic.appendChild( doc.createTextNode(indic) );
				}
				drug.appendChild(eIndications);
				
			//Dosage Form Node
				Element eDosageForm = doc.createElement("DosageForm");
				for(String dos : dosageForm){
					Element eDosage = doc.createElement("DosageForm");
					eDosageForm.appendChild(eDosage);
	
					eDosage.appendChild( doc.createTextNode(dos) );
				}
				drug.appendChild(eDosageForm);
							

			//Side Effects Node
				Element eSideEffects = doc.createElement("Side_Effects");
				for(String sfx : sideEffects){
					Element effect = doc.createElement("Side_Effect");
					eSideEffects.appendChild(effect);

					effect.appendChild( doc.createTextNode(sfx) );
				}
				drug.appendChild(eSideEffects);

			//Warning Node
				for(String w : warning){
					Element eWarnings = doc.createElement("Warnings");
					eWarnings.appendChild( doc.createTextNode(w) );
					drug.appendChild(eWarnings);
				}

			}

		//Tranforming XML Document
			TransformerFactory transfac = TransformerFactory.newInstance();
            		Transformer trans = transfac.newTransformer();
            		trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            		trans.setOutputProperty(OutputKeys.INDENT, "yes");


		//Writing XML to File
			StringWriter sw = new StringWriter();
	        	StreamResult result = new StreamResult(sw);
	        	DOMSource source = new DOMSource(doc);
	        	trans.transform(source, result);
	        	String xmlString = sw.toString();

			System.out.println("\n\n============================= X M L =============================\n");
			System.out.println(xmlString);

			saveXMLDocument("Scraped_Medical.xml", doc);

		}catch(Exception ex){	
			System.out.println("Unable to save, reason : " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	//Method will be used to print multiple values in Array List.
	private void printList(ArrayList<String> list){
		for(int i=0; i<list.size(); i++){	
			if(i > 0)
				System.out.print("\t\t  ");
			System.out.println(list.get(i));
		}
		System.out.println();
	}

	//To Save XML
	public boolean saveXMLDocument(String fileName, Document doc) {
        	System.out.println("Saving XML file... " + fileName);

        	File xmlOutputFile = new File(fileName);
        	FileOutputStream fos;
        	Transformer transformer;
        	try {
        	    fos = new FileOutputStream(xmlOutputFile);
        	}
        	catch (FileNotFoundException e) {
        	    System.out.println("Error occured: " + e.getMessage());
        	    return false;
        	}


        	TransformerFactory transformerFactory = TransformerFactory.newInstance();
        	try {
        	    transformer = transformerFactory.newTransformer();
        	}
        	catch (TransformerConfigurationException e) {
        	    System.out.println("Transformer configuration error: " + e.getMessage());
        	    return false;
        	}
        	DOMSource source = new DOMSource(doc);
        	StreamResult result = new StreamResult(fos);

        	try {
        	    transformer.transform(source, result);
        	}
        	catch (TransformerException e) {
        	    System.out.println("Error transform: " + e.getMessage());
        	}
        	System.out.println("XML file saved.");
        	return true;
    	}

	public static void main(String str[])
	throws Exception{
		new MedDataScraper();
	}
}