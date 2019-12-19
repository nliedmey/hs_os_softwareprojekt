package de.swprojekt.speeddating.model;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;

public class MatchingAsPDF {
	
	public MatchingAsPDF() {

	}

	public String pdfErstellen(ArrayList<EventMatching> arrayList, int event_id, String eventname, String password) throws FileNotFoundException {
		String filepath="";
		String filename="";
		try {

			String directory = "C:\\Users\\mariu\\Documents"; //LOKAL; bei Server-deploy siehe unten; Link funktioniert logischerweise lokal nicht
			//String directory=System.getProperty("jboss.server.data.dir"); //Property verweist auf Datenverzeichnes des Wildflyservers (auf Server: opt/wildfly)
			filename=event_id+"_"+eventname+".pdf";
			//FILEPATH WEBDEPLOY
			//filepath = directory+"/matchingAuswertungen/"+filename;
			
			//FILEPATH LOKAL
			filepath = directory+"\\matchingAuswertungen\\"+filename;
			
			File eventauswertungenDir=new File(directory, "matchingAuswertungen"); //Unterordner wird erstellt, wenn er nicht existiert
			eventauswertungenDir.mkdir();
			
			System.out.println("Ablage hier: "+filepath);
			
			Document doc = new Document();
			doc.addAuthor("Patrick_Marius_Nico");
			doc.addCreationDate();
			doc.addProducer();
			doc.addCreator("speeddating_gruppe");
			doc.addTitle("Matching Ergebnis - " + eventname);

			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filepath));
			writer.setEncryption(password.getBytes(), "standard".getBytes(), PdfWriter.ALLOW_COPY|PdfWriter.ALLOW_PRINTING|PdfWriter.ALLOW_DEGRADED_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128); //setzt Passwort auf PDF, da Link theoretisch fuer jeden erreichbar
			doc.open();

			// Uberschrift setzen
			doc.add(new Paragraph("Event Matching Ergebnisse - "+ eventname,
					FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD, BaseColor.BLACK)));
			// Leerzeile einfuegen
			doc.add(new Paragraph(" "));

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			
			 PdfPCell header = new PdfPCell();
		        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        header.setPhrase(new Phrase("Studenten"));
		        table.addCell(header);
		        header.setPhrase(new Phrase("Unternehmen"));
		        table.addCell(header);

			if (arrayList.isEmpty()) {				
				String zeilentext = "Es konnte keine passenden Matchings ermittelt werden";
				table.addCell(zeilentext);
			} else {
				
				for(EventMatching aEventMatching: arrayList) {
				
					String zeilentext = "";
					zeilentext = aEventMatching.getStudentenname();
					table.addCell(zeilentext);
					zeilentext = aEventMatching.getUnternehmensname();
					table.addCell(zeilentext);

//					zeilentext = aStudierender.getStringFullNameOfStudent();
//					table.addCell(zeilentext);
//					zeilentext = aUnternehmen.getUnternehmensname();
//					table.addCell(zeilentext);
				}
		
				
//				for (Map.Entry<Studierender, Unternehmen> entry : matchingResultMap.entrySet()) {
//					String zeilentext = "";
//					Studierender aStudierender = entry.getKey();
//					Unternehmen aUnternehmen = entry.getValue();
//					zeilentext = aStudierender.getStringFullNameOfStudent();
//					table.addCell(zeilentext);
//					zeilentext = aUnternehmen.getUnternehmensname();
//					table.addCell(zeilentext);
//				}
			}
			table.completeRow();
			doc.add(table);
			doc.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return filename;
	}
}
