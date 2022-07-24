package mycrm.views.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import mycrm.models.ElecCustomerContract;
import org.joda.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ElectricWelcomeLetter extends AbstractPdfView {

    private static final int LINE_HEIGHT = 11;
    private static final String PARAGRAPH1 = "Thank you for using Commercial Utility Consultants (CUC) to procure your electricity supply.";
    private static final String PARAGRAPH2 = "We are pleased to provide you with the information set out below regarding your electricity supply contract with";
    private static final String PARAGRAPH3 = "To ensure a smooth transfer of your supply, it is important to terminate your contract with your current supplier. This will prevent your current supplier extending the duration of the contract for a further year at a higher rate. Commercial Utility Consultants will endeavour to ensure this is completed with your current supplier but we cannot accept any liability for any termination not being accepted or within the timescales, as directed by your supplier.";
    private static final String PARAGRAPH4 = "Please also be aware that the contract you have entered in to is legally binding and as such you should not contract with any other provider. If your supply fails to transfer successfully due to other contracts being arranged, then you may be liable to pay a termination fee.  The termination is normally calculated based on 40% of the Estimated Annual Quantity (EAC) at the above unit prices and standing charges.";
    private static final String PARAGRAPH5 = "If you have any questions please feel free contact us.";

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        BaseFont base = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.WINANSI, true);
        Font font = new Font(base, 9f, Font.NORMAL);
        Font boldFont = new Font(base, 9f, Font.BOLD);

        ElecCustomerContract elecCustomerContract = (ElecCustomerContract) model.get("elecCustomerContract");

        Paragraph paragraphOne = new Paragraph(LINE_HEIGHT, PARAGRAPH1, font);
        Paragraph paragraphTwo = new Paragraph(LINE_HEIGHT,
                PARAGRAPH2 + " " + elecCustomerContract.getSupplier().getBusinessName() + ".", font);
        Paragraph paragraphThree = new Paragraph(LINE_HEIGHT, PARAGRAPH3, font);
        Paragraph paragraphFour = new Paragraph(LINE_HEIGHT, PARAGRAPH4, font);
        Paragraph paragraphFive = new Paragraph(LINE_HEIGHT, PARAGRAPH5, font);

        Paragraph letterTitle = new Paragraph(LINE_HEIGHT, "STATEMENT OF RENEWAL TERMS - ELECTRIC", boldFont);
        letterTitle.setAlignment(Element.ALIGN_CENTER);

        LocalDate localDate = new LocalDate();

        document.add(new Paragraph(LINE_HEIGHT, "Ref: " + elecCustomerContract.getCustomer().getCustomerReference()
                + " / " + elecCustomerContract.getAccountNumber(), font));
        document.add(new Paragraph(LINE_HEIGHT, localDate.toString("dd MMMM yyyy"), font));

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        String name = "";
        String startDateString = "";

        if (elecCustomerContract.getCustomer().getFirstName().length() > 0) {
            name = elecCustomerContract.getCustomer().getFirstName() + " "
                    + elecCustomerContract.getCustomer().getLastName();
        } else {
            name = elecCustomerContract.getCustomer().getBusinessName();
        }

        if (elecCustomerContract.getStartDate() != null) {
            startDateString = LocalDate.fromDateFields(elecCustomerContract.getStartDate()).toString("dd MMMM yyyy");
        }

        document.add(new Paragraph(LINE_HEIGHT, elecCustomerContract.getCustomer().getFirstName() + " "
                + elecCustomerContract.getCustomer().getLastName(), font));
        document.add(new Paragraph(LINE_HEIGHT, elecCustomerContract.getCustomerSite().getSiteName(), font));
        document.add(new Paragraph(LINE_HEIGHT, elecCustomerContract.getCustomerSite().getSiteAddr(), font));
        document.add(new Paragraph(LINE_HEIGHT, elecCustomerContract.getCustomerSite().getSiteAddr1(), font));
        document.add(new Paragraph(LINE_HEIGHT, elecCustomerContract.getCustomerSite().getSiteCity(), font));
        document.add(new Paragraph(LINE_HEIGHT, elecCustomerContract.getCustomerSite().getSitePostcodeOut() + " "
                + elecCustomerContract.getCustomerSite().getSitePostcodeIn(), font));

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

        PdfPCell companyNameCell = new PdfPCell(new Phrase("Company Name", font));
        companyNameCell.setUseAscender(true);
        companyNameCell.setUseDescender(true);
        companyNameCell.setLeading(LINE_HEIGHT, 0);
        companyNameCell.setBorder(0);
        PdfPCell companyNameValueCell = new PdfPCell(
                new Phrase(elecCustomerContract.getCustomerSite().getSiteName(), font));
        companyNameValueCell.setUseAscender(true);
        companyNameValueCell.setUseDescender(true);
        companyNameValueCell.setLeading(LINE_HEIGHT, 0);
        companyNameValueCell.setBorder(0);

        customerDetailsTable.addCell(companyNameCell);
        customerDetailsTable.addCell(companyNameValueCell);
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Contact Name", font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, elecCustomerContract.getCustomer().getFirstName() + " "
                + elecCustomerContract.getCustomer().getLastName(), font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Contact Tel No.", font));
        customerDetailsTable
                .addCell(new Phrase(LINE_HEIGHT, elecCustomerContract.getCustomer().getContactNumber(), font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Supplier", font));
        customerDetailsTable
                .addCell(new Phrase(LINE_HEIGHT, elecCustomerContract.getSupplier().getBusinessName(), font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Contract Start Date", font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, startDateString, font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Supply period", font));
        customerDetailsTable.addCell(
                new Phrase(LINE_HEIGHT, "" + elecCustomerContract.getContractMonthlyDuration() + " Months", font));
        customerDetailsTable.addCell(new Phrase(LINE_HEIGHT, "Site Address", font));
        customerDetailsTable.addCell(siteAddress(elecCustomerContract, font));

        document.add(customerDetailsTable);

        document.add(new Paragraph(" "));

        document.add(buildRatesTable(elecCustomerContract, font));

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
        document.add(new Paragraph(LINE_HEIGHT, elecCustomerContract.getBroker().getFirstName(), font));
        document.add(new Paragraph(LINE_HEIGHT, "Account Manager", font));

    }

    private Phrase siteAddress(ElecCustomerContract elecCustomerContract, Font font) {

        String siteAddr = elecCustomerContract.getCustomerSite().getSiteAddr();
        String siteAddr1 = elecCustomerContract.getCustomerSite().getSiteAddr1();
        String siteCity = elecCustomerContract.getCustomerSite().getSiteCity();
        String sitePostcode = elecCustomerContract.getCustomerSite().getSitePostcodeOut() + " "
                + elecCustomerContract.getCustomerSite().getSitePostcodeIn();

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

    private PdfPTable buildRatesTable(ElecCustomerContract elecCustomerContract, Font font) throws DocumentException {
        PdfPTable ratesTable = new PdfPTable(5);
        ratesTable.setWidthPercentage(100);
        ratesTable.setWidths(new int[] { 1, 1, 1, 2, 1 });
        ratesTable.getDefaultCell().setPadding(5);
        ratesTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        ratesTable.addCell(new Phrase(LINE_HEIGHT, "Standing Charge \n (pence)", font));
        ratesTable.addCell(new Phrase(LINE_HEIGHT, "Day Rate \n (p/kwh)", font));
        ratesTable.addCell(new Phrase(LINE_HEIGHT, "Night Rate \n (p/kwh)", font));
        ratesTable.addCell(new Phrase(LINE_HEIGHT, "Evening / Weekend Rate \n (p/kwh)", font));
        ratesTable.addCell(new Phrase(LINE_HEIGHT, "Feed In Tariff \n (p/kwh)", font));

        String standingCharge = elecCustomerContract.getStandingCharge() == 0.0 ? "N/A" : " " + elecCustomerContract.getStandingCharge();
        String standingChargeFrequency = elecCustomerContract.getStandingCharge() != 0.0 && elecCustomerContract.getStandingChargeFrequency() != null ?
                elecCustomerContract.getStandingChargeFrequency() : "";

        ratesTable.addCell(new Phrase(LINE_HEIGHT, standingCharge + " " + standingChargeFrequency,
                font));
        ratesTable.addCell(new Phrase(LINE_HEIGHT,
                "" + (0.0 == elecCustomerContract.getDayRate() ? "N/A" : elecCustomerContract.getDayRate()), font));
        ratesTable.addCell(new Phrase(LINE_HEIGHT,
                "" + (0.0 == elecCustomerContract.getNightRate() ? "N/A" : elecCustomerContract.getNightRate()), font));
        ratesTable.addCell(new Phrase(LINE_HEIGHT, "" + (0.0 == elecCustomerContract.getEveningWeekendRate() ? "N/A"
                : elecCustomerContract.getEveningWeekendRate()), font));
        ratesTable.addCell(new Phrase(LINE_HEIGHT,
                "" + (0.0 == elecCustomerContract.getUnitRate() ? "N/A" : elecCustomerContract.getUnitRate()), font));

        return ratesTable;
    }

}
