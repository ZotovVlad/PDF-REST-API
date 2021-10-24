package ru.mayday.pdf.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface PDFService {

    File unionPDFs(List<File> pdfFiles) throws IOException;

    File removePDF(File pdfFile, List<Integer> instructionPageForOperation) throws IOException;

    List<File> splitPDF(File pdfFile, List<String> instructionPageForOperation) throws IOException;

    File rotatePDF(File pdfFile, List<Integer> numberPageForOperation, Integer degreeForRotate);

    File archivePDF(File pdfFile) throws IOException;

    void savePDF(File pdfFile);

    List<String> parseInstructionPageRange(String instructionPageRange);

    List<Integer> parseInstructionPageForBindingOperation(List<String> instructionPageForOperation);

    File createFile(MultipartFile pdfFile);

    byte[] fileToByteArray(File pdfFileCustom) throws Exception;
}
