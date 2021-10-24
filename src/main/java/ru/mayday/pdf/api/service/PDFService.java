package ru.mayday.pdf.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface PDFService {

    File unionPDFs(List<File> pdfFiles);

    void deletePDF(File pdfFile);

    List<File> splitPDF(File pdfFile);

    File rotatePDF(File pdfFile, List<Integer> numberPageForOperation, Integer degreeForRotate);

    //todo convert File to archive
    File archivePDF(File pdfFile);

    void savePDFs(File pdfFile);

    List<String> parseInstructionPageRange(String instructionPageRange);

    List<Integer> parseInstructionPageForBindingOperation(List<String> instructionPageForOperation);

    File createFile(MultipartFile pdfFile);

}
