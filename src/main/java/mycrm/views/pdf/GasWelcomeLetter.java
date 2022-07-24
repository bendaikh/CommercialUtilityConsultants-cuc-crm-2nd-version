package mycrm.views.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import mycrm.models.GasCustomerContract;
import org.joda.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class GasWelcomeLetter extends AbstractPdfView {

    private static final int LINE_HEIGHT = 11;
    private static final String PARAGRAPH1 = "Thank you for using Commercial Utility Consultants (CUC) to procure your gas supply.";
    private static final String PARAGRAPH2 = "We are pleased to provide you with the information set out below regarding your gas supply contract with";
    private static final String PARAGRAPH3 = "To ensure a smooth transfer of your supply, it is important to terminate your contract with your current supplier. This will prevent your current supplier extending the duration of the contract for a further year at a higher rate. Commercial Utility Consultants will endeavour to ensure this is completed with your current supplier but we cannot accept any liability for any termination not being accepted or within the timescales, as directed by your supplier.";
    private static final String PARAGRAPH4 = "Please also be aware that the contract you have entered in to is legally binding and as such you should not contract with any other provider. If your supply fails to transfer successfully due to other contracts being arranged, then you may be liable to pay a termination fee.  The termination fee will be calculated based on your current Annual Quantity (AQ) at the above Unit Price and Daily Charge.";
    private static final String PARAGRAPH5 = "If you have any questions please feel free contact us.";

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        GasCustomerContract gasCustomerContract = (GasCustomerContract) model.get("gasCustomerContract");

        BaseFont base = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.WINANSI, true);
        // BaseFont base = BaseFont.createFont();
        Font font = new Font(base, 9f, Font.NORMAL);
        Font boldFont = new Font(base, 9f, Font.BOLD);
        // Font font = FontFactory.getFont(FontFactory.COURIER);
        // font.setSize(10f);

        Paragraph letterTitle = new Paragraph(LINE_HEIGHT, "STATEMENT OF RENEWAL TERMS - GAS", boldFont);
        letterTitle.setAlignment(Element.ALIGN_CENTER);

        Paragraph paragraphOne = new Paragraph(LINE_HEIGHT, PARAGRAPH1, font);
        Paragraph paragraphTwo = new Paragraph(LINE_HEIGHT,
                PARAGRAPH2 + " " + gasCustomerContract.getSupplier().getBusinessName() + ".", font);
        Paragraph paragraphThree = new Paragraph(LINE_HEIGHT, PARAGRAPH3, font);
        Paragraph paragraphFour = new Paragraph(LINE_HEIGHT, PARAGRAPH4, font);
        Paragraph paragraphFive = new Paragraph(LINE_HEIGHT, PARAGRAPH5, font);

        LocalDate localDate = new LocalDate();

        document.add(new Paragraph(LINE_HEIGHT, "Ref: " + gasCustomerContract.getCustomer().getCustomerReference()
                + " / " + gasCustomerContract.getAccountNumber(), font));
        document.add(new Paragraph(LINE_HEIGHT, localDate.toString("dd MMMM yyyy"), font));

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        String name = "";
        String startDateString = "";

        if (gasCustomerContract.getCustomer().getFirstName().length() > 0) {
            name = gasCustomerContract.getCustomer().getFirstName() + " "
                    + gasCustomerContract.getCustomer().getLastName();
        } else {
            name = gasCustomerContract.getCustomer().getBusinessName();
        }

        if (gasCustomerContract.getStartDate() != null) {
            startDateString = LocalDate.fromDateFields(gasCustomerContract.getStartDate()).toString("dd MMMM yyyy");
        }

        String customerName = gasCustomerContract.getCustomer().getFirstName() + " "
                + gasCustomerContract.getCustomer().getLastName();
        String siteName = gasCustomerContract.getCustomerSite().getSiteName();
        String siteAddr = gasCustomerContract.getCustomerSite().getSiteAddr();
        String siteAddr1 = gasCustomerContract.getCustomerSite().getSiteAddr1();
        String siteCity = gasCustomerContract.getCustomerSite().getSiteCity();
        String postcode = gasCustomerContract.getCustomerSite().getSitePostcodeOut() + " "
                + gasCustomerContract.getCustomerSite().getSitePostcodeIn();

        Paragraph pCustomerName = new Paragraph(LINE_HEIGHT, customerName, font);
        // pCustomerName.setIndentationLeft(50);
        Paragraph pSiteName = new Paragraph(LINE_HEIGHT, siteName, font);
        // pSiteName.setIndentationLeft(50);
        Paragraph pSiteAddr = new Paragraph(LINE_HEIGHT, siteAddr, font);
        // pSiteAddr.setIndentationLeft(50);
        Paragraph pSiteAddr1 = new Paragraph(LINE_HEIGHT, siteAddr1, font);
        // pSiteAddr1.setIndentationLeft(50);
        Paragraph pSiteCity = new Paragraph(LINE_HEIGHT, siteCity, font);
        // pSiteCity.setIndentationLeft(50);
        Paragraph pPostcode = new Paragraph(LINE_HEIGHT, postcode, font);
        // pPostcode.setIndentationLeft(50);

        document.add(pCustomerName);
        document.add(pSiteName);
        document.add(pSiteAddr);
        document.add(pSiteAddr1);
        document.add(pSiteCity);
        document.add(pPostcode);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph(LINE_HEIGHT, "Dear " + name, font));
        document.add(Chunk.NEWLINE);

        document.add(letterTitle);
        document.add(Chunk.NEWLINE);

        document.add(paragraphOne);

        document.add(Chunk.NEWLINE);

        document.add(paragraphTwo);

        document.add(new Paragraph(" "));

        PdfPTable customerDetailsTable = new PdfPTable(2);
        customerDetailsTable.setHorizontalAlignment(100);
        customerDetailsTable.getDefaultCell().setBorder(0);
        customerDetailsTable.setWidths(new int[] { 1, 2 });
        customerDetailsTable.setSpacingAfter(0);
        customerDetailsTable.setSpacingBefore(0);
        customerDetailsTable.setPaddingTop(0);
        customerDetailsTable.setWidthPercentage(100);

        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Company Name", font));
        customerDetailsTable
                .addCell(new Phrase(LINE_HEIGHT, gasCustomerContract.getCustomerSite().getSiteName(), font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Contact Name", font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, gasCustomerContract.getCustomer().getFirstName() + " "
                + gasCustomerContract.getCustomer().getLastName(), font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Contact Tel No.", font));
        customerDetailsTable
                .addCell(new Phrase(LINE_HEIGHT, gasCustomerContract.getCustomer().getContactNumber(), font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Supplier", font));
        customerDetailsTable
                .addCell(new Phrase(LINE_HEIGHT, gasCustomerContract.getSupplier().getBusinessName(), font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Contract Start Date", font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, startDateString, font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Supply period", font));
        customerDetailsTable
                .addCell(new Phrase("" + gasCustomerContract.getContractMonthlyDuration() + " Months", font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Unit price (ppkWh)", font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "" + gasCustomerContract.getUnitRate(), font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Standing Charge (pence)", font));

        String standingCharge = "" + gasCustomerContract.getStandingCharge();
        String standingChargeFrequency = gasCustomerContract.getStandingChargeFrequency() != null ? gasCustomerContract.getStandingChargeFrequency() : "";

        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, standingCharge + " " + standingChargeFrequency, font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Site Address", font));
        customerDetailsTable.addCell(siteAddress(gasCustomerContract, font));

        document.add(customerDetailsTable);

        document.add(new Paragraph(" "));

        document.add(paragraphThree);
        document.add(Chunk.NEWLINE);
        document.add(paragraphFour);
        document.add(Chunk.NEWLINE);
        document.add(paragraphFive);
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph(LINE_HEIGHT, "Yours faithfully,", font));
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph(LINE_HEIGHT, gasCustomerContract.getBroker().getFirstName(), font));
        document.add(new Paragraph(LINE_HEIGHT, "Account Manager", font));
    }

    private Phrase siteAddress(GasCustomerContract energyContract, Font font) {

        String siteAddr = energyContract.getCustomerSite().getSiteAddr();
        String siteAddr1 = energyContract.getCustomerSite().getSiteAddr1();
        String siteCity = energyContract.getCustomerSite().getSiteCity();
        String sitePostcode = energyContract.getCustomerSite().getSitePostcodeOut() + " "
                + energyContract.getCustomerSite().getSitePostcodeIn();

        StringBuilder siteAddress = new StringBuilder();

        if (siteAddr != null && siteAddr.length() > 0) {
            siteAddress.append(siteAddr + ", ");
        }
        if (siteAddr1 != null && siteAddr1.length() > 0) {
            siteAddress.append(siteAddr1 + ", ");
        }
        if (siteCity != null && siteCity.length() > 0) {
            siteAddress.append(siteCity + ", ");
        }
        if (sitePostcode != null && sitePostcode.length() > 0) {
            siteAddress.append(sitePostcode);
        }

        return new Phrase(LINE_HEIGHT, siteAddress.toString(), font);
    }
}
