package ru.mayday.pdf.api.repository;

import org.springframework.stereotype.Repository;
import ru.mayday.pdf.api.model.PDF;

import java.util.LinkedList;
import java.util.List;

@Repository
public class PDFRepository {

    private List<PDF> pdfFiles = new LinkedList<>();

    public List<PDF> getPdfFiles() {
        return pdfFiles;
    }

    public void setPdfFiles(List<PDF> pdfFiles) {
        this.pdfFiles = pdfFiles;
    }

    public void addPdfFile(PDF pdfFile) {
        this.pdfFiles.add(pdfFile);
    }

    public PDF getPdfFileByIndex(int pdfFileIndex) {
        return this.pdfFiles.get(pdfFileIndex);
    }

    public PDF getPdfFileByPdfFile(PDF pdfFile) {
        return this.pdfFiles.get(this.pdfFiles.indexOf(pdfFile));
    }

}
