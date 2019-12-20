package de.swprojekt.speeddating.service.pdf;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

import de.swprojekt.speeddating.model.EventMatching;
import de.swprojekt.speeddating.model.Role;
import de.swprojekt.speeddating.model.User;
import de.swprojekt.speeddating.service.showstudierender.IShowStudierendeService;
import de.swprojekt.speeddating.service.showunternehmen.IShowUnternehmenService;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;

@Service
public class MatchingAsPDFImpl implements IMatchingAsPDFService {
	
	@Autowired
	IShowStudierendeService iShowStudierendeService;
	
	@Autowired
	IShowUnternehmenService iShowUnternehmenService;
	
	public MatchingAsPDFImpl() {

	}

	public String pdfErstellen(List<User> users, int event_id, String eventname, String password) throws FileNotFoundException {
		String filepath="";
		String filename="";
		try {

			String directory = "C:\\Users\\mariu\\Documents"; //LOKAL; bei Server-deploy siehe unten; Link funktioniert logischerweise lokal nicht
			//String directory=System.getProperty("jboss.server.data.dir"); //Property verweist auf Datenverzeichnes des Wildflyservers (auf Server: opt/wildfly)
			filename=event_id+"_"+eventname+".pdf";
			//FILEPATH WEBDEPLOY
			//filepath = directory+"/matchingAuswertungen/"+filename;
			
			//FILEPATH LOKAL
			filepath = directory+"\\teilnehmerZugaenge\\"+filename;
			
			File eventauswertungenDir=new File(directory, "teilnehmerZugaenge"); //Unterordner wird erstellt, wenn er nicht existiert
			eventauswertungenDir.mkdir();
			
			System.out.println("Ablage hier: "+filepath);
			
			Document doc = new Document();
			doc.addAuthor("Patrick_Marius_Nico");
			doc.addCreationDate();
			doc.addProducer();
			doc.addCreator("speeddating_gruppe");
			doc.addTitle("Teilnehmer Zugaenge - " + eventname);

			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filepath));
			writer.setEncryption(password.getBytes(), "standard".getBytes(), PdfWriter.ALLOW_COPY|PdfWriter.ALLOW_PRINTING|PdfWriter.ALLOW_DEGRADED_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128); //setzt Passwort auf PDF, da Link theoretisch fuer jeden erreichbar
			doc.open();

			// Uberschrift setzen
			doc.add(new Paragraph("Teilnehmer Zugaenge - "+ eventname,
					FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD, BaseColor.BLACK)));
			// Leerzeile einfuegen
			doc.add(new Paragraph(" "));

			PdfPTable studierendeTable = new PdfPTable(3); //3 Spalten (Name, Username, Passwort) fuer Studierende
			studierendeTable.setWidthPercentage(100);
			
			PdfPTable unternehmenTable = new PdfPTable(3); //3 Spalten (Name, Username, Passwort) fuer Unternehmen
			unternehmenTable.setWidthPercentage(100);
			
			 PdfPCell header = new PdfPCell();
		        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        header.setPhrase(new Phrase("Name"));
		        studierendeTable.addCell(header);
		        header.setPhrase(new Phrase("Unternehmensname"));
		        unternehmenTable.addCell(header);
		        
		        header.setPhrase(new Phrase("Username"));
		        studierendeTable.addCell(header);
		        unternehmenTable.addCell(header);
		        
		        header.setPhrase(new Phrase("Passwort"));
		        studierendeTable.addCell(header);
		        unternehmenTable.addCell(header);

			if (users.isEmpty()) {				
				String zeilentext = "Keine User vorhanden";
				studierendeTable.addCell(zeilentext);
				unternehmenTable.addCell(zeilentext);
			} else {
				
				for(User aUser: users) {
				
					String zeilentext = "";
					Set<Role> userRoles=aUser.getRoles();
					String userRole="";
					for(Role aRole:userRoles)
					{
						if(aRole.getRole().equals("STUDENT"))
						{
							userRole="STUDENT";
						}
						else if(aRole.getRole().equals("UNTERNEHMEN"))
						{
							userRole="UNTERNEHMEN";
						}
					}
					
					if(userRole.equals("STUDENT"))
					{
						zeilentext = iShowStudierendeService.showStudierenden(aUser.getEntity_id_ref()).getVorname()+" "+iShowStudierendeService.showStudierenden(aUser.getEntity_id_ref()).getNachname();
						studierendeTable.addCell(zeilentext); //fuer Studenten wird Vor- und Nachname in Spalte "Name" angezeigt
						zeilentext = aUser.getUsername(); 
						studierendeTable.addCell(zeilentext);
						zeilentext = "pass*"+aUser.getUser_id(); //uebereinstimmend mit Initalpasswort
						studierendeTable.addCell(zeilentext); //Passwort in dritte Spalte einfuegen
					}
					else if(userRole.equals("UNTERNEHMEN"))
					{
						zeilentext = iShowUnternehmenService.showEinUnternehmen(aUser.getEntity_id_ref()).getUnternehmensname();
						unternehmenTable.addCell(zeilentext); //fuer Unternehmen wird Unternehmensname in Spalte "Name" angezeigt
						zeilentext = aUser.getUsername(); 
						studierendeTable.addCell(zeilentext);
						zeilentext = "pass*"+aUser.getUser_id(); //uebereinstimmend mit Initalpasswort
						studierendeTable.addCell(zeilentext); //Passwort in dritte Spalte einfuegen
					}
				}
			}
			studierendeTable.completeRow();
			unternehmenTable.completeRow();
			doc.add(studierendeTable);
			doc.add(unternehmenTable);
			doc.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return filename;
	}

	@Override
	public String pdfMatchingErgebnisseErstellen(ArrayList<EventMatching> arrayList, int event_id, String eventname,
			String password) throws FileNotFoundException {
		
		String filepath="";
		String filename="";
		try {
//			String directory = "C:\\Users\\mariu\\Documents"; //LOKAL; bei Server-deploy siehe unten; Link funktioniert logischerweise lokal nicht
			String directory = "C:\\projekt";
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
				}
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
