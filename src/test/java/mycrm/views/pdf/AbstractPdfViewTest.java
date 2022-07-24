package mycrm.views.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import mycrm.models.GasCustomerContract;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class AbstractPdfViewTest {

    private AbstractPdfView abstractPdfView = spy(AbstractPdfView.class);

    @Mock
    private GasCustomerContract gasCustomerContract;

    private Document document = new Document(PageSize.A4);

    @Mock
    private PdfWriter writer;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    private MockHttpServletResponse response = new MockHttpServletResponse();

    private Map<String, Object> model = aContractModel();

    @Test
    public void shouldReturnTrueWhenGenerateDownloadContent() {
        boolean generatesDownloadContent = abstractPdfView.generatesDownloadContent();
        assertTrue(generatesDownloadContent);
    }

    @Test
    public void shouldPrepareWriter() throws DocumentException {
        abstractPdfView.prepareWriter(model, writer, request);
    }

    @Test
    public void shouldBuildPdfMetadata() throws DocumentException {
        abstractPdfView.buildPdfMetadata(model, document, request);
    }

    @Test
    public void shouldBuildPdfDocument() throws Exception {
        abstractPdfView.buildPdfDocument(model, document, writer, request, response);
    }

    @Test
    public void shouldReturnViewerPreference() {
        int viewerPreferences = abstractPdfView.getViewerPreferences();
        assertEquals(2053, viewerPreferences);
    }

    @Ignore
    @Test
    public void shouldRenderMergedOutputModel() throws Exception {

        final String text = "this should be in the PDF";
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        AbstractPdfView pdfView = new AbstractPdfView() {
            protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
                document.add(new Paragraph(text));
            }
        };

        pdfView.render(new HashMap<String, Object>(), request, response);
        byte[] pdfContent = response.getContentAsByteArray();
        assertEquals("correct response content type", "application/pdf", response.getContentType());
        assertEquals("correct response content length", pdfContent.length, response.getContentLength());

        // rebuild iText document for comparison
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
        document.open();
        document.add(new Paragraph(text));
        document.close();
        byte[] baosContent = baos.toByteArray();
        assertEquals("correct size", pdfContent.length, baosContent.length);

        int diffCount = 0;
        for (int i = 0; i < pdfContent.length; i++) {
            if (pdfContent[i] != baosContent[i]) {
                diffCount++;
            }
        }
        assertTrue("difference only in encryption", diffCount < 70);
    }

    private Map<String, Object> aContractModel() {
        Map<String, Object> model = new HashMap<>();
        model.put("gasCustomerContract", gasCustomerContract);
        return model;
    }

}
