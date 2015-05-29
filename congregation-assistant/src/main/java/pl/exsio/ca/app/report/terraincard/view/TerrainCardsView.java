/* 
 * The MIT License
 *
 * Copyright 2015 exsio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.exsio.ca.app.report.terraincard.view;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
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
import pl.exsio.jin.annotation.TranslationPrefix;
import static pl.exsio.jin.translationcontext.TranslationContext.t;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.report.terrain_card")
public class TerrainCardsView extends AbstractPdfView {

    private TerrainCardViewModel viewModel;

    private static final int START_COL = 0;

    private static final int START_ROW = 3;
    
    private static final String BACKGROUND_PATH = "/img/back.png";
    
    private static final Color BACK_COLOR = new Color(244, 206, 138);

    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document dcmnt, PdfWriter writer, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        PdfContentByte canvas = writer.getDirectContentUnder();
        Image back = Image.getInstance(getClass().getClassLoader().getResource(BACKGROUND_PATH));
        back.scaleAbsolute(PageSize.A4.getWidth(),PageSize.A4.getHeight());
        back.setAbsolutePosition(0, 0);
        canvas.addImage(back);
        
        LinkedList<TerrainCardPage> pages = this.viewModel.getPages(map);
        int pagesCount = pages.size();
        
        for (int i = 0; i < pagesCount; i++) {
            
            dcmnt.add(this.buildTable(pages.get(i)));
            if (i < pages.size() - 1) {
                dcmnt.newPage();
                canvas.addImage(back);
            }
        }

    }

    private Element buildTable(TerrainCardPage page) throws Exception {
        Table table = new Table(this.viewModel.getTableColumnsNo() * 2);
        table.setBorderWidth(0);
        table.setPadding(0.565f);
        table.setSpacing(0.7f);
        table.setWidth(100);
        table.setAlignment(Element.ALIGN_RIGHT);

        int currentCol = START_COL;
        int currentRow = START_ROW;
        for (int i = 0; i < page.getColumns().size(); i++) {
            TerrainCardColumn column = page.getColumns().get(i);
            table.addCell(this.getColumnSpacingCell(4), 0, currentCol);
            table.addCell(this.getColumnTitleCell(column.getTerrainName()), 1, currentCol);
            table.addCell(this.getColumnSpacingCell(3), 2, currentCol);
            for (int j = 0; j < column.getCells().size(); j++) {
                TerrainCardCell cell = column.getCells().get(j);
                table.addCell(this.getNotificationCell("            "+cell.getGroup(), 2,  6), currentRow, currentCol);
                table.addCell(this.getNotificationCell(cell.getFrom(), 1, 7), currentRow + 1, currentCol);
                table.addCell(this.getNotificationCell(cell.getTo(), 1, 7), currentRow + 1, currentCol + 1);
                currentRow += 2;
            }
            currentCol += 2;
            currentRow = START_ROW;
        }

        return table;
    }

    private Cell getNotificationCell(String groupName, int colSpan, int size) throws Exception {
        Paragraph p = new Paragraph(groupName, this.getFont());
        if (groupName.trim().equalsIgnoreCase(TerrainCardViewModel.EMPTY_CELL_VALUE)) {
            p.getFont().setColor(BACK_COLOR);
        }
        p.getFont().setSize(size);
        Cell cell = new Cell(p);
        cell.setBorder(0);
        cell.setColspan(colSpan);
        return cell;
    }
    
    private Cell getColumnSpacingCell(int size) throws Exception {
        Paragraph p = new Paragraph(TerrainCardViewModel.EMPTY_CELL_VALUE, this.getFont());
        p.getFont().setColor(BACK_COLOR);
        p.getFont().setSize(size);
        Cell cell = new Cell(p);
        cell.setBorder(0);
        return cell;
    }

    private Cell getColumnTitleCell(String title) throws Exception {

        Paragraph p = new Paragraph("         "+title, this.getFont());
        int size = 8;
        if(title.length() > 18) {
            size = 6;
        }
        p.getFont().setSize(size);
        p.getFont().setStyle(Font.ITALIC);
        p.setSpacingAfter(2);
        p.setSpacingBefore(2);
        Cell cell = new Cell(p);
        cell.setColspan(2);
         cell.setBorder(0);
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
