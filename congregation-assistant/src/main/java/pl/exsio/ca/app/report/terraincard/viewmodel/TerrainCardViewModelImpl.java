/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.app.report.terraincard.viewmodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import pl.exsio.ca.app.report.terraincard.model.TerrainCardCell;
import pl.exsio.ca.app.report.terraincard.model.TerrainCardColumn;
import pl.exsio.ca.app.report.terraincard.model.TerrainCardPage;
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
public class TerrainCardViewModelImpl implements TerrainCardViewModel {

    private static final String PARAM_TYPE = "type";

    private static final String PARAM_GROUP = "group";

    private static final String PARAM_DATE = "date";

    private static final int TABLE_COLS = 5;

    private static final int TABLE_ROWS = 19;

    private CaRepositoryProvider caRepositories;

    private final SimpleDateFormat sdf;

    private final LinkedHashMap<Terrain, LinkedList<TerrainCardCell>> cellsMap;

    public TerrainCardViewModelImpl() {
        this.cellsMap = new LinkedHashMap<>();
        this.sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public LinkedList<TerrainCardPage> getPages(Map<String, Object> params) {
        this.createCellsMap(params);
        LinkedList<TerrainCardPage> pages = this.createPages();
        return pages;
    }

    private LinkedList<TerrainCardPage> createPages() {
        int terrainsCount = this.cellsMap.keySet().size();
        LinkedList<TerrainCardPage> pages = new LinkedList<>();

        for (int i = 0; i < terrainsCount; i += TABLE_COLS) {
            int to = i + TABLE_COLS < terrainsCount ? i + TABLE_COLS : terrainsCount;
            LinkedHashMap<Terrain, LinkedList<TerrainCardCell>> slice = this.getSlice(i, to);
            pages.addAll(this.createPagesFromTerrainsSlice(slice));
        }

        return pages;
    }

    private LinkedList<TerrainCardPage> createPagesFromTerrainsSlice(LinkedHashMap<Terrain, LinkedList<TerrainCardCell>> slice) {
        LinkedList<TerrainCardPage> pages = new LinkedList<>();
        int pagesNo = this.getPagesNo(slice);
        int currentPageNo = 1;
        for (int i = 0; i < pagesNo * TABLE_ROWS; i += TABLE_ROWS) {
            TerrainCardPage page = new TerrainCardPage();
            page.setPageTitle("");
            page.setPageNo(currentPageNo);
            for (Terrain terrain : slice.keySet()) {
                int cellsCount = slice.get(terrain).size();
                TerrainCardColumn column = new TerrainCardColumn(terrain.getNo() + ". (" + terrain.getName() + ")");
                if (cellsCount > i) {
                    int to = i + TABLE_ROWS < cellsCount ? i + TABLE_ROWS : cellsCount;
                    LinkedList<TerrainCardCell> cells = new LinkedList<>(slice.get(terrain).subList(i, to));
                    column.getCells().addAll(cells);
                }
                this.fillCells(column);
                page.addColumn(column);
            }
            pages.add(page);
        }

        return pages;
    }

    private int getPagesNo(LinkedHashMap<Terrain, LinkedList<TerrainCardCell>> slice) {
        int pagesNo = 1;
        for (Terrain key : slice.keySet()) {
            int sliceCount = new Double(Math.ceil((double) slice.get(key).size() / TABLE_ROWS)).intValue();
            if (sliceCount > pagesNo) {
                pagesNo = sliceCount;
            }
        }
        return pagesNo;
    }

    private LinkedHashMap<Terrain, LinkedList<TerrainCardCell>> getSlice(int from, int to) {
        LinkedHashMap<Terrain, LinkedList<TerrainCardCell>> slice = new LinkedHashMap<>();
        ArrayList<Terrain> keys = new ArrayList<>(new ArrayList<>(this.cellsMap.keySet()).subList(from, to));
        for (Terrain key : keys) {
            slice.put(key, this.cellsMap.get(key));
        }
        return slice;
    }

    private void createCellsMap(Map<String, Object> params) {

        Date date = this.getDateFromParams(params);
        for (Terrain terrain : this.getTerrains(params)) {
            TerrainAssignmentDao assignments = this.caRepositories.getTerrainAssignmentRepository();
            LinkedHashSet<TerrainAssignment> terrainAssignments = date == null ? assignments.findForTerrainCard(terrain) : assignments.findForTerrainCard(terrain, date);
            LinkedList<TerrainCardCell> terrainCells = new LinkedList<>();

            for (TerrainAssignment assignment : terrainAssignments) {
                ArrayList<TerrainNotification> notifications = new ArrayList<>(assignment.getNotifications());
                for (int i = 0; i < notifications.size(); i++) {
                    TerrainCardCell cell = new TerrainCardCell();
                    TerrainNotification notification = notifications.get(i);
                    cell.setGroup(assignment.getGroup().getCaption());
                    cell.setFrom(sdf.format(notification.getDate()));
                    String toDateContent = this.getToDateContent(i, notifications, assignment);
                    cell.setTo(toDateContent);
                    terrainCells.add(cell);

                    if (!toDateContent.equals(EMPTY_CELL_VALUE) && i == notifications.size() - 1) {
                        TerrainCardCell lastCell = new TerrainCardCell();
                        lastCell.setGroup(assignment.getGroup().getCaption());
                        lastCell.setFrom(toDateContent);
                        lastCell.setTo(EMPTY_CELL_VALUE);
                        terrainCells.add(lastCell);
                    }
                }
            }
            this.cellsMap.put(terrain, terrainCells);
        }

    }

    private void fillCells(TerrainCardColumn column) {

        while (column.getCells().size() < TABLE_ROWS) {
            TerrainCardCell emptyCell = new TerrainCardCell();
            emptyCell.setGroup(EMPTY_CELL_VALUE);
            emptyCell.setFrom(EMPTY_CELL_VALUE);
            emptyCell.setTo(EMPTY_CELL_VALUE);
            column.addCell(emptyCell);
        }
    }

    private String getToDateContent(int i, ArrayList<TerrainNotification> notifications, TerrainAssignment assignment) {
        String toDate = null;
        if (i == notifications.size() - 1) {
            Date assignmentEndDate = assignment.getEndDate();
            toDate = assignmentEndDate == null ? EMPTY_CELL_VALUE : sdf.format(assignmentEndDate);
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
