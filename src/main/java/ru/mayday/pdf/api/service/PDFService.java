package ru.mayday.pdf.api.service;

import ru.mayday.pdf.api.model.PDF;

import java.io.File;
import java.util.List;

public interface PDFService {

    PDF unionPDFs(List<PDF> pdfFiles);

    void deletePDF(PDF pdfFile);

    List<PDF> splitPDF(PDF pdfFile);

    PDF rotatePDF(PDF pdfFile);

    //todo convert File to archive
    File archivePDF(PDF pdfFile);

    void savePDFs(PDF pdfFile);

}
