package ru.mayday.pdf.api.operation;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.pdfbox.util.Matrix;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Repository
public class OperationPDF {

    public static String pdfToArchiveWithImages(File filePDF) throws IOException {
        PDDocument document = PDDocument.load(filePDF);
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        String pathFile = filePDF.getAbsolutePath();
        String nameFileInput = filePDF.getName();

        new File(pathFile + "\\" + nameFileInput).mkdirs();

        for (int page = 0; page < document.getNumberOfPages(); ++page) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300,
                    ImageType.RGB);

            // suffix in filename will be used as the file format
            ImageIOUtil.writeImage(bim, pathFile + "\\" + nameFileInput + "\\" +
                    nameFileInput + "-" + (page + 1) + ".png", 300);
        }
        document.close();

        archiveFolder(pathFile, nameFileInput);

        deleteDirectoryRecursion(Paths.get(pathFile + "\\" + nameFileInput));

        return pathFile + "\\" + nameFileInput;
    }

    static void deleteDirectoryRecursion(Path path) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteDirectoryRecursion(entry);
                }
            }
        }
        Files.delete(path);
    }

    private static void archiveFolder(String pathFile, String nameFileInput) throws IOException {
        FileOutputStream fos = new FileOutputStream(pathFile + "\\" + nameFileInput +
                ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(pathFile + "\\" + nameFileInput);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }


    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public static File splitFile(File pdfFile, List<Integer> numberPageForOperation) throws IOException {
        PDDocument document = PDDocument.load(pdfFile);
        Splitter splitter = new Splitter();
        ArrayList<PDDocument> Pages = (ArrayList<PDDocument>) splitter.split(document);
        Iterator<PDDocument> iterator = Pages.listIterator();

        for (int j = numberPageForOperation.get(0); j < numberPageForOperation.get(numberPageForOperation.size()); j++) {
            PDDocument pd = iterator.next();
            pd.save(pdfFile.getAbsoluteFile().toString() + j + ".pdf");
        }
        document.close();
        return new File(pdfFile.getAbsoluteFile().toString() + 0 + ".pdf");
    }

    public static File unionFile(List<File> pdfFiles) throws IOException {
        String pathFile = pdfFiles.get(0).getAbsolutePath() + "unionPDFs.pdf";
        PDFMergerUtility PDFmerger = new PDFMergerUtility();
        PDFmerger.setDestinationFileName(pathFile);
        for (File pdfFile : pdfFiles) {
            PDDocument doc = PDDocument.load(pdfFile);
            PDFmerger.addSource(pdfFile);
            doc.close();
        }
        PDFmerger.mergeDocuments();
        return new File(pathFile);
    }

    public static void rotateFile(File pdfFile, Integer numberPageForOperation, Integer degreeForRotate) throws IOException {
        PDDocument document = PDDocument.load(pdfFile);
        PDPage page = document.getDocumentCatalog().getPages().get(numberPageForOperation - 1);
        PDPageContentStream cs = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.PREPEND, false, false);
        Matrix matrix = Matrix.getRotateInstance(Math.toRadians(degreeForRotate), 0, 0);
        cs.transform(matrix);
        cs.close();

        PDRectangle cropBox = page.getCropBox();
        Rectangle rectangle = cropBox.transform(matrix).getBounds();
        PDRectangle newBox = new PDRectangle((float) rectangle.getX(),
                (float) rectangle.getY(), (float) rectangle.getWidth(),
                (float) rectangle.getHeight());
        page.setCropBox(newBox);
        page.setMediaBox(newBox);

        document.save(pdfFile);
        document.close();
    }

    public static File removePage(File pdfFile, List<Integer> instructionPageForOperation) throws IOException {
        PDDocument document = PDDocument.load(pdfFile);
        for (Integer instruction : instructionPageForOperation) {
            document.removePage(instruction - 1);
        }
        document.save(pdfFile.getAbsolutePath() + "removePDF.pdf");
        document.close();
        return pdfFile;
    }

}
