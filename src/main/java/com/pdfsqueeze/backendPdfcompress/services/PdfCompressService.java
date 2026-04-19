package com.pdfsqueeze.backendPdfcompress.services;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PdfCompressService {

    public byte[] compress(MultipartFile file, String quality) throws IOException, InterruptedException {

        // Сохраняем входной файл во временную папку
        Path inputPath = Files.createTempFile("input_", ".pdf");
        Path outputPath = Files.createTempFile("output_", ".pdf");

        try {
            file.transferTo(inputPath);

            // Выбираем настройку Ghostscript по качеству
            String pdfSettings = switch (quality) {
                case "low"  -> "/screen";   // максимальное сжатие
                case "high" -> "/printer";  // высокое качество
                default     -> "/ebook";    // баланс
            };

            // Запускаем Ghostscript
            ProcessBuilder pb = new ProcessBuilder(
                    "/usr/local/bin/gs", // полный путь вместо просто "gs"
                    "-sDEVICE=pdfwrite",
                    "-dCompatibilityLevel=1.4",
                    "-dPDFSETTINGS=" + pdfSettings,
                    "-dNOPAUSE",
                    "-dBATCH",
                    "-dQUIET",
                    "-sOutputFile=" + outputPath.toString(),
                    inputPath.toString()
            );

            Process process = pb.start();

            // Читаем ошибку от Ghostscript
            String error = new String(process.getErrorStream().readAllBytes());
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new IOException("Ghostscript завершился с ошибкой: " + exitCode);
            }

            // Читаем результат и возвращаем байты
            return Files.readAllBytes(outputPath);

        } finally {
            // Всегда удаляем temp файлы
            Files.deleteIfExists(inputPath);
            Files.deleteIfExists(outputPath);
        }
    }
}
