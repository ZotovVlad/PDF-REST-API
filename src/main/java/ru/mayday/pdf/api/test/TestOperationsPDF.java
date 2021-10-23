package ru.mayday.pdf.api.test;

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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TestOperationsPDF {

    private static final String pathFile = "C:\\Users\\user\\Desktop\\";
    private static final String nameFileInput = "testPDFfile";
    private static final String pathFileInput = pathFile + nameFileInput + ".pdf";
    private static final String pathFileInput1 = pathFile + "testPDFfile1.pdf";

    public static void main(String args[]) throws IOException {

        // removePage(0);

        // rotateFile(90);

        // union2File();

        // splitFile(3);

        // pdfToArchiveWithImages();

    }

    private static void pdfToArchiveWithImages() throws IOException {
        PDDocument document = PDDocument.load(new File(pathFileInput));
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        new File(pathFile + "\\" + nameFileInput).mkdirs();

        for (int page = 0; page < document.getNumberOfPages(); ++page) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300,
                    ImageType.RGB);

            // suffix in filename will be used as the file format
            ImageIOUtil.writeImage(bim, pathFile + "\\" + nameFileInput + "\\" +
                    nameFileInput + "-" + (page + 1) + ".png", 300);
        }
        document.close();

        archiveFolder();

        deleteDirectoryRecursion(Paths.get(pathFile + "\\" + nameFileInput));

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

    private static void archiveFolder() throws IOException {
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

    private static void splitFile(int splitCount) throws IOException {
        //Loading an existing PDF document
        File file = new File(pathFileInput);
        PDDocument document = PDDocument.load(file);

        //Instantiating Splitter class
        Splitter splitter = new Splitter();

        //splitting the pages of a PDF document
        ArrayList<PDDocument> Pages = (ArrayList<PDDocument>) splitter.split(document);

        //Creating an iterator
        Iterator<PDDocument> iterator = Pages.listIterator();

        //Saving each page as an individual document
        for (int j = 0; j < splitCount; j++) {
            PDDocument pd = iterator.next();
            pd.save(pathFile + j + ".pdf");
        }
        System.out.println("Multiple PDF’s created");
        document.close();
    }

    private static void union2File() throws IOException {
        //Loading an existing PDF document
        File file1 = new File(pathFileInput);
        PDDocument doc1 = PDDocument.load(file1);

        File file2 = new File(pathFileInput1);
        PDDocument doc2 = PDDocument.load(file2);

        //Instantiating PDFMergerUtility class
        PDFMergerUtility PDFmerger = new PDFMergerUtility();

        //Setting the destination file
        PDFmerger.setDestinationFileName(pathFile + "testUnionPDFs.pdf");

        //adding the source files
        PDFmerger.addSource(file1);
        PDFmerger.addSource(file2);

        //Merging the two documents
        PDFmerger.mergeDocuments();

        System.out.println("Documents merged");
        //Closing the documents
        doc1.close();
        doc2.close();
    }

    private static void rotateFile(int rotateDegree) throws IOException {
        PDDocument document = PDDocument.load(new File(pathFileInput));
        PDPage page = document.getDocumentCatalog().getPages().get(0);
        PDPageContentStream cs = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.PREPEND, false, false);
        Matrix matrix = Matrix.getRotateInstance(Math.toRadians(rotateDegree), 0, 0);
        cs.transform(matrix);
        cs.close();

        PDRectangle cropBox = page.getCropBox();
        Rectangle rectangle = cropBox.transform(matrix).getBounds();
        PDRectangle newBox = new PDRectangle((float) rectangle.getX(),
                (float) rectangle.getY(), (float) rectangle.getWidth(),
                (float) rectangle.getHeight());
        page.setCropBox(newBox);
        page.setMediaBox(newBox);

        document.save(new File(pathFile + "testRotatePDF.pdf"));

        //Closing the document
        document.close();
    }

    private static void removePage(int numberPage) throws IOException {
        //Loading an existing document
        File file = new File(pathFile + "testPDFfile.pdf");
        PDDocument document = PDDocument.load(file);

        //Listing the number of existing pages
        int noOfPages = document.getNumberOfPages();
        System.out.print(noOfPages);

        //Removing the pages
        document.removePage(numberPage);

        System.out.println("page removed");

        //Saving the document
        document.save(pathFile + "testRemovePDF.pdf");

        //Closing the document
        document.close();
    }


}
