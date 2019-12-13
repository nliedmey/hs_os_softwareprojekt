package de.swprojekt.speeddating.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.io.FileNotFoundException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;

public class MatchingAsPDF {

	public MatchingAsPDF() {

	}

	public void pdfErstellen(Map<Studierender, Unternehmen> matchingResultMap) throws FileNotFoundException {
		try {

			String filename;
			filename = "C:\\Users\\patri\\Desktop\\MatchingErgebnisse.pdf";
			Document doc = new Document();
			doc.addAuthor("Patrick_Marius_Nico");
			doc.addCreationDate();
			doc.addProducer();
			doc.addCreator("speeddating_gruppe");
			doc.addTitle("Matching Ergebnis");

			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filename));
			doc.open();

			// Uberschrift setzen
			doc.add(new Paragraph("Event Matching Ergebnisse",
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

			if (matchingResultMap.isEmpty()) {				
				String zeilentext = "Es konnte keine passenden Matchings ermittelt werden";
				table.addCell(zeilentext);
			} else {
				for (Map.Entry<Studierender, Unternehmen> entry : matchingResultMap.entrySet()) {
					String zeilentext = "";
					Studierender aStudierender = entry.getKey();
					Unternehmen aUnternehmen = entry.getValue();
					zeilentext = aStudierender.getStringFullNameOfStudent();
					table.addCell(zeilentext);
					zeilentext = aUnternehmen.getUnternehmensname();
					table.addCell(zeilentext);
				}
			}
			table.completeRow();
			doc.add(table);
			doc.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
