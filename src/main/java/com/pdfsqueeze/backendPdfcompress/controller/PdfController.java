package com.pdfsqueeze.backendPdfcompress.controller;

import com.pdfsqueeze.backendPdfcompress.services.PdfCompressService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {
    private final PdfCompressService compressService;

    public PdfController(PdfCompressService compressService) {
        this.compressService = compressService;
    }

    @PostMapping("/compress")
    public ResponseEntity<byte[]> compress(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "quality", defaultValue = "medium") String quality
    ) throws Exception {

        // Валидация — это точно PDF?
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (!file.getOriginalFilename().endsWith(".pdf")) {
            return ResponseEntity.badRequest().build();
        }

        // Лимит 20МБ
        if (file.getSize() > 20 * 1024 * 1024) {
            return ResponseEntity.badRequest().build();
        }

        // Сжимаем
        byte[] compressedBytes = compressService.compress(file, quality);

        // Возвращаем файл
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=compressed.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(compressedBytes);
    }
}