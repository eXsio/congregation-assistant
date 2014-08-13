/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.app.report.terraincard.view;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import pl.exsio.ca.app.report.terraincard.model.TerrainCardCell;
import pl.exsio.ca.app.report.terraincard.model.TerrainCardColumn;
import pl.exsio.ca.app.report.terraincard.model.TerrainCardPage;
import pl.exsio.ca.app.report.terraincard.viewmodel.TerrainCardViewModel;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.TerrainType;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;

/**
 *
 * @author exsio
 */
public class TerrainCardsView extends AbstractPdfView {

    private TerrainCardViewModel viewModel;

    private static final int START_COL = 0;

    private static final int START_ROW = 2;

    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document dcmnt, PdfWriter writer, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        LinkedList<TerrainCardPage> pages = this.viewModel.getPages(map);
        int pagesCount = pages.size();
        for (int i = 0; i < pagesCount; i++) {
            dcmnt.add(this.getHeader(map));
            dcmnt.add(this.buildTable(pages.get(i)));
            dcmnt.add(this.getCreationDate(new Date()));
            dcmnt.add(this.getPageCounter(pagesCount, i + 1));
            if (i < pages.size() - 1) {
                dcmnt.newPage();
            }
        }

    }

    private Element buildTable(TerrainCardPage page) throws Exception {
        Table table = new Table(this.viewModel.getTableColumnsNo() * 2);
        table.setBorderWidth(1);
        table.setPadding(1);
        table.setSpacing(1);
        table.setWidth(100);
        int currentCol = START_COL;
        int currentRow = START_ROW;
        for (int i = 0; i < page.getColumns().size(); i++) {
            TerrainCardColumn column = page.getColumns().get(i);
            table.addCell(this.getColumnTitleCell(column.getTerrainName()), 0, currentCol);
            table.addCell(this.getColumnDescCell("ca.report.terrain_card.from"), 1, currentCol);
            table.addCell(this.getColumnDescCell("ca.report.terrain_card.to"), 1, currentCol + 1);
            for (int j = 0; j < column.getCells().size(); j++) {
                TerrainCardCell cell = column.getCells().get(j);
                boolean odd = j % 2 != 0;
                table.addCell(this.getNotificationCell(cell.getGroup(), odd, 2), currentRow, currentCol);
                table.addCell(this.getNotificationCell(cell.getFrom(), odd, 1), currentRow + 1, currentCol);
                table.addCell(this.getNotificationCell(cell.getTo(), odd, 1), currentRow + 1, currentCol + 1);
                currentRow += 2;
            }
            currentCol += 2;
            currentRow = START_ROW;
        }

        return table;
    }
    
    private Element getHeader(Map<String, Object> params) throws Exception {
        StringBuilder sb = new StringBuilder(t("ca.report.terrain_cards.head")).append(" ");
        TerrainType type = this.viewModel.getTypeFromParams(params);
        if(type != null) {
            sb.append(t("ca.report.terrain_cards.type")).append(": ").append(type.getCaption()).append(". ");
        }
        
        ServiceGroup group = this.viewModel.getGroupFromParams(params);
        if(group != null) {
            sb.append(t("ca.report.terrain_cards.group")).append(": ").append(group.getCaption()).append(". ");
        }
        
        Date date = this.viewModel.getDateFromParams(params);
        if(date != null) {
            sb.append(t("ca.report.terrain_cards.date")).append(": ").append(new SimpleDateFormat("yyyy-MM-dd").format(date)).append(".");
        }
        Paragraph p = new Paragraph(sb.toString(), this.getFont());
        p.getFont().setStyle(Font.BOLD);
        p.getFont().setSize(9);
        return p;
    }

    private Element getPageCounter(int pagesCount, int currentPage) throws Exception {
        Paragraph p = new Paragraph(currentPage + " / " + pagesCount, this.getFont());
        p.setAlignment(Element.ALIGN_RIGHT);
        p.getFont().setSize(8);
        return p;
    }
    
    private Element getCreationDate(Date date) throws Exception {
        Paragraph p = new Paragraph(t("ca.report.terrain_cards.creation_date") + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date), this.getFont());
        p.setAlignment(Element.ALIGN_LEFT);
        p.getFont().setSize(7);
        return p;
    }

    private Cell getNotificationCell(String groupName, boolean odd, int colSpan) throws Exception {
        Paragraph p = new Paragraph(groupName, this.getFont());
        if (groupName.equals(TerrainCardViewModel.EMPTY_CELL_VALUE)) {
            p.getFont().setColor(Color.WHITE);
        }
        p.getFont().setSize(7);
        Cell cell = new Cell(p);
        if (odd) {
            cell.setBackgroundColor(new Color(240, 240, 240));
        }
        cell.setColspan(colSpan);
        return cell;
    }

    private Cell getColumnTitleCell(String title) throws Exception {

        Paragraph p = new Paragraph(title, this.getFont());
        p.getFont().setSize(10);
        p.getFont().setStyle(Font.BOLDITALIC);
        p.setSpacingAfter(2);
        p.setSpacingBefore(2);
        Cell cell = new Cell(p);
        cell.setColspan(2);
        cell.setBackgroundColor(new Color(230, 230, 230));
        return cell;
    }

    private Cell getColumnDescCell(String desc) throws Exception {
        Paragraph p = new Paragraph(t(desc), this.getFont());
        p.getFont().setSize(8);
        p.getFont().setStyle(Font.BOLDITALIC);
        p.setSpacingAfter(2);
        p.setSpacingBefore(2);
        Cell cell = new Cell(p);
        cell.setBackgroundColor(new Color(230, 230, 230));
        return cell;
    }

    private Font getFont() throws Exception {
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        Font f = new Font(bf, 10, Font.NORMAL);
        return f;
    }

    public void setViewModel(TerrainCardViewModel viewModel) {
        this.viewModel = viewModel;
    }

}
