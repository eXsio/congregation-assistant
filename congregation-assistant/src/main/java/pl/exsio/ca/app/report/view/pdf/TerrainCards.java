/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.app.report.view.pdf;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.ca.model.TerrainType;
import pl.exsio.ca.model.dao.TerrainAssignmentDao;
import pl.exsio.ca.model.dao.TerrainDao;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;

/**
 *
 * @author exsio
 */
public class TerrainCards extends AbstractPdfView {

    private static final String PARAM_TYPE = "type";

    private static final String PARAM_GROUP = "group";

    private static final String PARAM_DATE = "date";

    private static final int TABLE_COLS = 5;

    private CaRepositoryProvider caRepositories;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document dcmnt, PdfWriter writer, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        LinkedHashSet<Terrain> terrains = this.getTerrains(map);
        Date date = this.getDateFromParams(map);
        for (int i = 0; i < terrains.size(); i += TABLE_COLS) {
            int to = i + TABLE_COLS < terrains.size() - 1 ? i + TABLE_COLS : terrains.size() - 1;
            dcmnt.add(this.buildTable(new ArrayList<>(new ArrayList<>(terrains).subList(i, to)), date));
            if (i < terrains.size() - 1) {
                dcmnt.newPage();
            }
        }

    }

    private Element buildTable(ArrayList<Terrain> terrainsSlice, Date date) throws BadElementException, DocumentException, IOException {
        Table table = new Table(TABLE_COLS * 2);
        table.setBorderWidth(1);
        table.setPadding(1);
        table.setSpacing(1);
        table.setWidth(100);

        int currentCol = 0;
        int currentRow = 1;
        this.drawCells(terrainsSlice, date, table, currentCol, currentRow);
        return table;
    }

    private void drawCells(ArrayList<Terrain> terrainsSlice, Date date, Table table, int currentCol, int currentRow) throws BadElementException, DocumentException, IOException {
        for (int i = 0; i < terrainsSlice.size(); i++) {
            Cell[] col = this.buildCol(terrainsSlice.get(i), date);
            table.addCell(col[0], 0, currentCol);
            for (int j = 1; j < col.length; j += 3) {
                table.addCell(col[j], currentRow, currentCol);
                table.addCell(col[j + 1], currentRow + 1, currentCol);
                table.addCell(col[j + 2], currentRow + 1, currentCol + 1);
                currentRow += 2;
            }
            currentCol += 2;
            currentRow = 1;
        }
    }

    private Cell[] buildCol(Terrain terrain, Date date) throws BadElementException, DocumentException, IOException {
        LinkedList<Cell> cells = new LinkedList<>();
        cells.add(this.getColumnTitle(terrain));
        TerrainAssignmentDao assignments = this.caRepositories.getTerrainAssignmentRepository();
        LinkedHashSet<TerrainAssignment> terrainAssignments = date == null ? assignments.findForTerrainCard(terrain) : assignments.findForTerrainCard(terrain, date);
        for (TerrainAssignment assignment : terrainAssignments) {
            ArrayList<TerrainNotification> notifications = new ArrayList<>(assignment.getNotifications());
            for (int i = 0; i < notifications.size(); i++) {
                getCellsFromNotification(notifications, i, assignment, cells);
            }
        }

        this.addEmptyCells(cells);
        Cell[] cellArr = new Cell[cells.size()];
        for (int i = 0; i < cells.size(); i++) {
            cellArr[i] = cells.get(i);
        }
        return cellArr;
    }

    private void getCellsFromNotification(ArrayList<TerrainNotification> notifications, int i, TerrainAssignment assignment, LinkedList<Cell> cells) throws IOException, DocumentException {
        TerrainNotification notification = notifications.get(i);
        Cell head = getNotificationHead(assignment);
        Cell from = getNotificationFromDate(notification);
        String toDateContent = getToDateContent(i, notifications, assignment);
        Cell to = getNotificationToDate(toDateContent);
        cells.add(head);
        cells.add(from);
        cells.add(to);

        if (!toDateContent.equals("") && i == notifications.size() - 1) {
            cells.add(head);
            cells.add(to);
            cells.add(new Cell(""));
        }
    }

    private void addEmptyCells(LinkedList<Cell> cells) throws BadElementException {
        while (cells.size() < 46) {
            Paragraph whiteP = new Paragraph("x");
            whiteP.getFont().setColor(Color.WHITE);
            Cell fakeHead = new Cell(whiteP);
            fakeHead.setColspan(2);
            cells.add(fakeHead);
            cells.add(new Cell(whiteP));
            cells.add(new Cell(whiteP));
        }
    }

    private Cell getNotificationToDate(String toDate) throws BadElementException, DocumentException, IOException {

        Paragraph toP = new Paragraph(toDate, this.getFont());
        toP.getFont().setSize(8);
        Cell to = new Cell(toP);
        return to;
    }

    private Cell getNotificationFromDate(TerrainNotification notification) throws BadElementException, DocumentException, IOException {
        Paragraph fromP = new Paragraph(sdf.format(notification.getDate()), this.getFont());
        fromP.getFont().setSize(8);
        Cell from = new Cell(fromP);
        return from;
    }

    private Cell getNotificationHead(TerrainAssignment assignment) throws BadElementException, DocumentException, IOException {
        Paragraph headP = new Paragraph(assignment.getGroup().getCaption(), this.getFont());
        headP.getFont().setSize(8);
        Cell head = new Cell(headP);
        head.setColspan(2);
        return head;
    }

    private Cell getColumnTitle(Terrain terrain) throws BadElementException, DocumentException, IOException {

        Paragraph terrainNameP = new Paragraph(terrain.getNo() + ". (" + terrain.getName() + ")", this.getFont());
        terrainNameP.getFont().setSize(10);
        terrainNameP.getFont().setStyle(Font.BOLDITALIC);
        terrainNameP.setSpacingAfter(2);
        terrainNameP.setSpacingBefore(2);
        Cell terrainName = new Cell(terrainNameP);
        terrainName.setColspan(2);

        return terrainName;
    }

    private Font getFont() throws DocumentException, IOException {
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        Font f = new Font(bf, 10, Font.NORMAL);
        return f;
    }

    private String getToDateContent(int i, ArrayList<TerrainNotification> notifications, TerrainAssignment assignment) {
        String toDate = null;
        if (i == notifications.size() - 1) {
            Date assignmentEndDate = assignment.getEndDate();
            toDate = assignmentEndDate == null ? "" : sdf.format(assignmentEndDate);
        } else {
            toDate = sdf.format(notifications.get(i + 1).getDate());
        }
        return toDate;
    }

    private LinkedHashSet<Terrain> getTerrains(Map<String, Object> params) {
        TerrainDao terrains = this.caRepositories.getTerrainRepository();
        TerrainType type = getTypeFromParams(params);
        ServiceGroup group = getGroupFromParams(params);
        Date date = getDateFromParams(params);

        LinkedHashSet<Terrain> result = null;
        if (type != null && group == null && date == null) {
            result = terrains.findByType(type);
        } else if (type != null && group != null && date == null) {
            result = terrains.findByTypeAndGroup(type, group);
        } else if (type != null && group != null && date != null) {
            result = terrains.findByTypeAndGroupAndDate(type, group, date);
        } else if (type != null && group == null && date != null) {
            result = terrains.findByTypeAndDate(type, date);
        } else if (type == null && group != null && date != null) {
            result = terrains.findByGroupAndDate(group, date);
        } else if (type == null && group != null && date == null) {
            result = terrains.findByGroup(group);
        } else if (type == null && group == null && date != null) {
            result = terrains.findByDate(date);
        } else {
            result = terrains.findAllTerrains();
        }

        return result;
    }

    private TerrainType getTypeFromParams(Map<String, Object> params) {
        TerrainType type = (params.get(PARAM_TYPE) == null || params.get(PARAM_TYPE).equals("")) ? null : TerrainType.valueOf(params.get(PARAM_TYPE).toString());
        return type;
    }

    private ServiceGroup getGroupFromParams(Map<String, Object> params) throws NumberFormatException {
        ServiceGroup group = (params.get(PARAM_GROUP) == null || (params.get(PARAM_GROUP).equals("")) ? null : this.caRepositories.getServiceGroupRepository().findOne(Long.valueOf(params.get(PARAM_GROUP).toString())));
        return group;
    }

    private Date getDateFromParams(Map<String, Object> params) {
        Date date = (params.get(PARAM_DATE) == null || params.get(PARAM_DATE).equals("")) ? null : new Date(Date.parse(params.get(PARAM_DATE).toString()));
        return date;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

}
