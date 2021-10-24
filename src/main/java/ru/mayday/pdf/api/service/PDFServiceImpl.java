package ru.mayday.pdf.api.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mayday.pdf.api.operation.OperationPDF;
import ru.mayday.pdf.api.repository.PDFRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PDFServiceImpl implements PDFService {

    private final PDFRepository pdfRepository;

    public PDFServiceImpl(PDFRepository pdfRepository) {
        this.pdfRepository = pdfRepository;
    }

    @Override
    public File unionPDFs(List<File> pdfFiles) {
        return null;
    }

    @Override
    public void deletePDF(File pdfFile) {

    }

    @Override
    public List<File> splitPDF(File pdfFile) {
        return null;
    }

    @Override
    public File rotatePDF(File pdfFile, List<Integer> numberPageForOperation, Integer degreeForRotate) {
        try {
            for (Integer numberPage : numberPageForOperation) {
                OperationPDF.rotateFile(pdfFile, numberPage, degreeForRotate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfFile;
    }

    @Override
    public File archivePDF(File pdfFile) {
        return null;
    }

    @Override
    public void savePDFs(File pdfFile) {

    }

    @Override
    public List<String> parseInstructionPageRange(String instructionPageRange) {
        return Arrays.asList(instructionPageRange.split("\\s*,\\s*"));
    }

    @Override
    public List<Integer> parseInstructionPageForBindingOperation(List<String> instructionPageForOperation) {
        List<Integer> numberPage = new ArrayList<>();
        for (String instruction : instructionPageForOperation) {
            if (instruction.contains("-")) {
                String[] numberOfInstructionString = instruction.split("-");
                Integer[] numberOfInstruction = new Integer[2];
                numberOfInstruction[0] = Integer.parseInt(numberOfInstructionString[0]);
                numberOfInstruction[1] = Integer.parseInt(numberOfInstructionString[1]);
                for (int i = numberOfInstruction[0]; i < numberOfInstruction[1] + 1; i++) {
                    numberPage.add(i);
                }
            } else {
                numberPage.add(Integer.parseInt(instruction));
            }
        }
        return numberPage;
    }

    @Override
    public File createFile(MultipartFile pdfFile) {
        File outputFile = null;
        FileOutputStream outputStream = null;
        try {
            String originalnameFile = pdfFile.getOriginalFilename();
            String pathToSaveFile = System.getProperty("user.home") + "/Desktop/PDF-REST-API-files/" + originalnameFile;
            outputFile = new File(pathToSaveFile);
            outputStream = new FileOutputStream(outputFile);
            outputStream.write(pdfFile.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }
}
