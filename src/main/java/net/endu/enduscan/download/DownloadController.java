package net.endu.enduscan.download;

import java.io.IOException;
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
public class DownloadController {

    private HttpHeaders textFileHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=testo.txt");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;
    }

    @GetMapping(path = "/download")
    public ResponseEntity<Resource> download(String param) throws IOException {
        try {
            String fileDiTesto = "Salve, mio buon Taddeo!";
            ByteArrayResource resource = new ByteArrayResource(fileDiTesto.getBytes());

            return ResponseEntity.ok().headers(textFileHeader()).contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
        } catch (Exception e) {
            log.error("File download endpoint error", e);
            throw e;
        }
    }
}
