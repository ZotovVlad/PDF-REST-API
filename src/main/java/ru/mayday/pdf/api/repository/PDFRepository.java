package ru.mayday.pdf.api.repository;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Repository
public class PDFRepository {

    private List<File> pdfFiles = new LinkedList<>();

    public List<File> getPdfFiles() {
        return pdfFiles;
    }

    public void setPdfFiles(List<File> pdfFiles) {
        this.pdfFiles = pdfFiles;
    }

    public void addPdfFile(File pdfFile) {
        this.pdfFiles.add(pdfFile);
    }

    public File getPdfFileByIndex(int pdfFileIndex) {
        return this.pdfFiles.get(pdfFileIndex);
    }

    public File getPdfFileByPdfFile(File pdfFile) {
        return this.pdfFiles.get(this.pdfFiles.indexOf(pdfFile));
    }

}
