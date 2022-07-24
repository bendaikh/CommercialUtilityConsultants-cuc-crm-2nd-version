package mycrm.views.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Map;

public abstract class AbstractPdfView extends AbstractView {

    public AbstractPdfView() {
        initView();
    }

    private void initView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = createTemporaryOutputStream();

        FileInputStream template = new FileInputStream("C:\\cecfiles\\templates\\cuc-letterhead.pdf");

        PdfReader letterhead = new PdfReader(template);
        Document document = new Document(letterhead.getPageSizeWithRotation(1), 70, 55, 90, 90);

        PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);

        prepareWriter(model, writer, request);
        buildPdfMetadata(model, document, request);

        document.open();

        PdfImportedPage page = writer.getImportedPage(letterhead, 1);
        PdfContentByte cb = writer.getDirectContent();
        cb.addTemplate(page, 0, 0);

        buildPdfDocument(model, document, writer, request, response);
        document.close();

        writeToResponse(response, byteArrayOutputStream);
    }

    protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request)
            throws DocumentException {
        writer.setViewerPreferences(getViewerPreferences());
    }

    protected int getViewerPreferences() {
        return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
    }

    protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {
    }

    protected abstract void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception;
}
