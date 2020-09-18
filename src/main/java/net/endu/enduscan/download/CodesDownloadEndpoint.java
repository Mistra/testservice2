package net.endu.enduscan.download;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.stream.Stream;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CodesDownloadEndpoint {
    private String generateCsvCodes() {
        log.trace("generating dummy csv");
        return Stream
                .of("Nome;Numero codice", "John Pollazzi;01", "Gigio Progetti;02",
                        "Gundrea Andini;03")
                .reduce((a, b) -> a + System.lineSeparator() + b).orElse("");
    }

    private ByteArrayOutputStream generatePdfManual() throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, bos);

        document.open();

        Font title = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk("My awesome ENDUSCAN manual", title);
        Paragraph para1 = new Paragraph(chunk);
        para1.setAlignment(Paragraph.ALIGN_CENTER);
        para1.setSpacingAfter(50);
        document.add(para1);

        Font paragraph = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        chunk = new Chunk("Date of download: " + LocalDate.now().toString(), paragraph);
        Paragraph para2 = new Paragraph(chunk);
        para2.setAlignment(Paragraph.ALIGN_LEFT);
        para2.setSpacingAfter(50);
        document.add(para2);

        document.close();
        return bos;
    }

    private HttpHeaders csvHttpHeader() {
        log.trace("generating csv http header");
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Codes.csv");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        // header.add("Pragma", "no-cache");
        // header.add("Expires", "0");
        return header;
    }

    private HttpHeaders pdfHttpHeader() {
        log.trace("generating pdf http header");
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Enduscan manual.pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        // header.add("Pragma", "no-cache");
        // header.add("Expires", "0");
        return header;
    }

    @GetMapping(path = "/download/codes")
    public ResponseEntity<Resource> downloadCodes() {
        log.debug("serving codes from REST endpoint");
        ByteArrayResource resource = new ByteArrayResource(generateCsvCodes().getBytes());
        return ResponseEntity.ok().headers(csvHttpHeader()).contentLength(resource.contentLength())
                .contentType(MediaType.TEXT_PLAIN).body(resource);

    }

    @GetMapping(path = "/download/manual")
    public ResponseEntity<Resource> downloadManual() throws DocumentException {
        log.debug("serving pdf manual from REST endpoint");
        ByteArrayResource resource = new ByteArrayResource(generatePdfManual().toByteArray());
        return ResponseEntity.ok().headers(pdfHttpHeader()).contentLength(resource.contentLength())
                .contentType(MediaType.TEXT_PLAIN).body(resource);
    }
}
