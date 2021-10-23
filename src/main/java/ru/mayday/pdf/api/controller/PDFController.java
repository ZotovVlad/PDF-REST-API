package ru.mayday.pdf.api.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mayday.pdf.api.model.PDF;
import ru.mayday.pdf.api.service.PDFService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class PDFController {

    private final PDFService pdfService;

    public PDFController(PDFService pdfService) {
        this.pdfService = pdfService;
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/v1/pdfs/rotate/all", method = RequestMethod.POST,  consumes ="multipart/form-data")
    public void rotatePDFFileAllPage(@RequestParam("file") MultipartFile pdfFile) {
        File outputFile = null;
        FileOutputStream outputStream = null;
        try {
            outputFile = new File("C:\\Users\\user\\Desktop\\testFileGet.pdf");
            outputStream = new FileOutputStream(outputFile);
            outputStream.write(pdfFile.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return outputFile;
    }

    @RequestMapping(value = "/v1/pdfs/rotate/{customRotatePages}", method = RequestMethod.POST)
    public PDF rotatePDFFileCustomPage(@PathVariable String customRotatePages, @RequestBody byte[] pdfFile) {


        return new PDF();
    }


}
