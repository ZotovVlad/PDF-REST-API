package ru.mayday.pdf.api.service;


import org.springframework.stereotype.Service;
import ru.mayday.pdf.api.model.PDF;
import ru.mayday.pdf.api.repository.PDFRepository;

import java.io.File;
import java.util.List;

@Service
public class PDFServiceImpl implements PDFService {

    private final PDFRepository pdfRepository;

    public PDFServiceImpl(PDFRepository pdfRepository) {
        this.pdfRepository = pdfRepository;
    }

    @Override
    public PDF unionPDFs(List<PDF> pdfFiles) {
        return null;
    }

    @Override
    public void deletePDF(PDF pdfFile) {

    }

    @Override
    public List<PDF> splitPDF(PDF pdfFile) {
        return null;
    }

    @Override
    public PDF rotatePDF(PDF pdfFile) {
        return null;
    }

    @Override
    public File archivePDF(PDF pdfFile) {
        return null;
    }

    @Override
    public void savePDFs(PDF pdfFile) {

    }
}
