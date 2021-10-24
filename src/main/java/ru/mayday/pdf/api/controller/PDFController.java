package ru.mayday.pdf.api.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mayday.pdf.api.service.PDFService;

import java.io.File;
import java.util.List;

@RestController
public class PDFController {

    private final PDFService pdfService;

    public PDFController(PDFService pdfService) {
        this.pdfService = pdfService;
    }

    @ResponseBody
    @RequestMapping(value = "/v1/pdfs/rotate", method = RequestMethod.POST, consumes = "multipart/form-data")
    public void rotatePDFFilePage(
            @RequestParam("file") MultipartFile pdfFile,
            @RequestParam("instructionPageRange") String instructionPageRange,
            @RequestParam("degreeForRotate") Integer degreeForRotate) {

        List<String> instructionPageForOperation = pdfService.parseInstructionPageRange(instructionPageRange);
        List<Integer> numberPageForOperation = pdfService.parseInstructionPageForBindingOperation(instructionPageForOperation);

        File pdfFileCustom = pdfService.createFile(pdfFile);

        pdfFileCustom = pdfService.rotatePDF(pdfFileCustom, numberPageForOperation, degreeForRotate);

        //return outputFile;
    }

}
