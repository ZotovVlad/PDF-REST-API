package ru.mayday.pdf.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mayday.pdf.api.service.PDFService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PDFController {

    private final PDFService pdfService;

    public PDFController(PDFService pdfService) {
        this.pdfService = pdfService;
    }

    @RequestMapping(value = "/v1/pdfs/rotate", method = RequestMethod.POST, consumes = "multipart/form-data")
    public byte[] rotatePDFFilePage(
            @RequestParam("file") MultipartFile pdfFile,
            @RequestParam("instructionPageRange") String instructionPageRange,
            @RequestParam("degreeForRotate") Integer degreeForRotate) throws Exception {

        List<String> instructionPageForOperation = pdfService.parseInstructionPageRange(instructionPageRange);
        List<Integer> numberPageForOperation = pdfService.parseInstructionPageForBindingOperation(instructionPageForOperation);

        File pdfFileCustom = pdfService.createFile(pdfFile);
        pdfFileCustom = pdfService.rotatePDF(pdfFileCustom, numberPageForOperation, degreeForRotate);
        pdfService.savePDF(pdfFileCustom);

        return pdfService.fileToByteArray(pdfFileCustom);
    }

    @RequestMapping(value = "/v1/pdfs/union", method = RequestMethod.POST, consumes = "multipart/form-data")
    public byte[] unionPDFFilePage(
            @RequestParam("file") ArrayList<MultipartFile> pdfFiles) throws Exception {

        List<File> pdfFilesCustom = new ArrayList<>();
        for (MultipartFile pdfFile : pdfFiles) {
            pdfFilesCustom.add(pdfService.createFile(pdfFile));
        }
        File pdfFileCustom = pdfService.unionPDFs(pdfFilesCustom);
        pdfService.savePDF(pdfFileCustom);

        return pdfService.fileToByteArray(pdfFileCustom);
    }

    @RequestMapping(value = "/v1/pdfs/split", method = RequestMethod.POST, consumes = "multipart/form-data")
    public List<byte[]> splitPDFFilePage(
            @RequestParam("file") MultipartFile pdfFile,
            @RequestParam("instructionPageRange") String instructionPageRange) throws Exception {

        List<String> instructionPageForOperation = pdfService.parseInstructionPageRange(instructionPageRange);

        File pdfFileCustom = pdfService.createFile(pdfFile);
        List<File> pdfFilesCustom = pdfService.splitPDF(pdfFileCustom, instructionPageForOperation);

        List<byte[]> filesToByteArray = new ArrayList<>();
        for (File file : pdfFilesCustom) {
            pdfService.savePDF(file);
            filesToByteArray.add(pdfService.fileToByteArray(file));
        }

        return filesToByteArray;
    }

    @RequestMapping(value = "/v1/pdfs/remove", method = RequestMethod.POST, consumes = "multipart/form-data")
    public byte[] removePDFFilePage(
            @RequestParam("file") MultipartFile pdfFile,
            @RequestParam("instructionPageRange") String instructionPageRange,
            HttpServletResponse response) throws Exception {

        List<String> instructionPageForOperation = pdfService.parseInstructionPageRange(instructionPageRange);
        List<Integer> numberPageForOperation = pdfService.parseInstructionPageForBindingOperation(instructionPageForOperation);

        File pdfFileCustom = pdfService.createFile(pdfFile);
        pdfFileCustom = pdfService.removePDF(pdfFileCustom, numberPageForOperation);
        pdfService.savePDF(pdfFileCustom);

        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment; filename=" + pdfFileCustom.getAbsolutePath()+ pdfFileCustom.getName());
        response.setContentLength(pdfService.fileToByteArray(pdfFileCustom).length);
        response.getOutputStream().write(pdfService.fileToByteArray(pdfFileCustom));
        response.getOutputStream().flush();

        return pdfService.fileToByteArray(pdfFileCustom);
    }

    @RequestMapping(value = "/v1/pdfs/archive", method = RequestMethod.POST, consumes = "multipart/form-data")
    public byte[] archivePDFFilePage(
            @RequestParam("file") MultipartFile pdfFiles) throws Exception {

        File fileCustom = pdfService.createFile(pdfFiles);

        File pdfFileCustom = pdfService.archivePDF(fileCustom);
        pdfService.savePDF(pdfFileCustom);

        return pdfService.fileToByteArray(pdfFileCustom);
    }

}
