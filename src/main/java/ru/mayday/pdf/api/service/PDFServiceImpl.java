package ru.mayday.pdf.api.service;


import org.apache.pdfbox.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mayday.pdf.api.operation.OperationPDF;
import ru.mayday.pdf.api.repository.PDFRepository;

import java.io.*;
import java.nio.file.Files;
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
    public File unionPDFs(List<File> pdfFiles) throws IOException {
        return OperationPDF.unionFile(pdfFiles);
    }

    @Override
    public File removePDF(File pdfFile, List<Integer> instructionPageForOperation) throws IOException {
        return OperationPDF.removePage(pdfFile, instructionPageForOperation);
    }

    @Override
    public List<File> splitPDF(File pdfFile, List<String> instructionPageForOperation) throws IOException {

        List<File> files = new ArrayList<>();
        for (String instructionPage : instructionPageForOperation) {
            List<Integer> numberPageForOperation = this.parseInstructionPageForBindingOperation(instructionPageForOperation);
            files.add(OperationPDF.splitFile(pdfFile, numberPageForOperation));
        }
        return files;
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
    public File archivePDF(File pdfFile) throws IOException {
        return new File(OperationPDF.pdfToArchiveWithImages(pdfFile));
    }

    @Override
    public void savePDF(File pdfFile) {
        pdfRepository.addPdfFile(pdfFile);
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

    @Override
    public byte[] fileToByteArray(File pdfFileCustom) throws Exception {
        byte[] bytes = new byte[(int) pdfFileCustom.length()];
        // funny, if can use Java 7, please uses Files.readAllBytes(path)
        try (FileInputStream fis = new FileInputStream(pdfFileCustom)) {
            fis.read(bytes);
        }
        System.out.println(Arrays.toString(bytes));
        return bytes;
        //return Files.readAllBytes(pdfFileCustom.toPath());
    }
}
