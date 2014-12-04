/* 
 * The MIT License
 *
 * Copyright 2014 exsio.
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
package pl.exsio.ca.app.report.terraincard.viewmodel;

import java.text.ParseException;
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
import pl.exsio.ca.model.OverseerAssignment;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.ca.model.TerrainType;
import pl.exsio.ca.model.dao.TerrainDao;
import pl.exsio.ca.model.dao.TerrainNotificationDao;
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

    private static final int TABLE_ROWS = 21;

    private CaRepositoryProvider caRepositories;

    private final SimpleDateFormat sdf;

    private LinkedHashMap<Terrain, LinkedList<TerrainCardCell>> cellsMap;

    public TerrainCardViewModelImpl() {
        this.sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public LinkedList<TerrainCardPage> getPages(Map<String, Object> params) {
        this.cellsMap = new LinkedHashMap<>();
        this.createCellsMap(params);
        LinkedList<TerrainCardPage> pages = this.createPages();
        return pages;
    }

    @Override
    public int getTableColumnsNo() {
        return TABLE_COLS;
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
        for (int i = 0; i < pagesNo * TABLE_ROWS; i += TABLE_ROWS) {
            TerrainCardPage page = new TerrainCardPage();
            for (Terrain terrain : slice.keySet()) {
                int cellsCount = slice.get(terrain).size();
                TerrainCardColumn column = new TerrainCardColumn(terrain.getNo() + ". (" + terrain.getName() + ")");
                if (cellsCount > i) {
                    int to = i + TABLE_ROWS < cellsCount ? i + TABLE_ROWS : cellsCount;
                    LinkedList<TerrainCardCell> cells = new LinkedList<>(slice.get(terrain).subList(i, to));
                    column.getCells().addAll(cells);
                }
                this.createEmptyCells(column);
                page.addColumn(column);
            }
            pages.add(page);
        }

        return pages;
    }

    private void createCellsMap(Map<String, Object> params) {

        Date date = this.getDateFromParams(params);
        for (Terrain terrain : this.getTerrains(params)) {
            this.createCellsForTerrain(date, terrain);
        }

    }

    private void createCellsForTerrain(Date date, Terrain terrain) {
        TerrainNotificationDao notifiationsDao = this.caRepositories.getTerrainNotificationRepository();
        LinkedHashSet<TerrainNotification> notificationsSet = date == null ? notifiationsDao.findForTerrainCard(terrain) : notifiationsDao.findForTerrainCard(terrain, date);
        LinkedList<TerrainCardCell> terrainCells = new LinkedList<>();

        ArrayList<TerrainNotification> notifications = new ArrayList<>(notificationsSet);
        if (terrain.getAssignments().size() > 0) {
            if (notifications.size() > 0) {
                for (int i = 0; i < notifications.size(); i++) {
                    this.createCell(notifications, i, terrainCells);
                }
            } else {
                terrainCells.add(this.createVoidCell(terrain));
            }
        }
        this.cellsMap.put(terrain, terrainCells);
    }

    private void createCell(ArrayList<TerrainNotification> notifications, int i, LinkedList<TerrainCardCell> terrainCells) {
        TerrainCardCell cell = new TerrainCardCell();
        TerrainNotification notification = notifications.get(i);
        TerrainAssignment assignment = notification.getAssignment();

        Date from = this.getFrom(i, notification, assignment, notifications);
        ServiceGroup group = notification.getOverrideGroup() instanceof ServiceGroup ? notification.getOverrideGroup() : notification.getAssignment().getGroup();
        OverseerAssignment latestAssignment = this.getOverseerAssignment(group, notification.getDate());

        cell.setGroup(group.getCaption(latestAssignment));
        cell.setFrom(sdf.format(from));
        cell.setTo(sdf.format(notification.getDate()));

        terrainCells.add(cell);
        if (i == notifications.size() - 1) {
            TerrainCardCell lastCell = this.createLastCell(assignment, notification);
            terrainCells.add(lastCell);
        }
    }
    
    private OverseerAssignment getOverseerAssignment(ServiceGroup group, Date date) {
        ArrayList<OverseerAssignment> candidates = this.caRepositories.getServiceGroupRepository().getOverseerAssignmentByDate(group, date);
        if(candidates.size() > 0) {
            return candidates.get(0);
        } else {
            throw new RuntimeException("There is no configured ovserseer for group "+group+" and date "+sdf.format(date));
        }
    }

    private Date getFrom(int i, TerrainNotification notification, TerrainAssignment assignment, ArrayList<TerrainNotification> notifications) {
        Date from = null;
        if (i == 0 || notification.equals(new ArrayList<>(assignment.getNotifications()).get(0))) {
            from = assignment.getStartDate();
        } else {
            from = notifications.get(i - 1).getDate();
        }
        return from;
    }

    private TerrainCardCell createLastCell(TerrainAssignment assignment, TerrainNotification notification) {
        TerrainCardCell lastCell = new TerrainCardCell();
        Terrain terrain = assignment.getTerrain();
        TerrainAssignment lastAssignment = terrain.getAssignments().last();

        TerrainAssignment lastCellAssignment = null;
        Date lastCellFrom = null;
        if (lastAssignment.equals(assignment)) {
            lastCellAssignment = assignment;
            lastCellFrom = notification.getDate();
        } else {
            lastCellAssignment = lastAssignment;
            lastCellFrom = lastAssignment.getStartDate();
        }

        if (!lastCellAssignment.isExpired()) {
            lastCell.setFrom(sdf.format(lastCellFrom));
            lastCell.setGroup(lastCellAssignment.getGroup().getCaption());
        } else {
            lastCell.setFrom(EMPTY_CELL_VALUE);
            lastCell.setGroup(EMPTY_CELL_VALUE);
        }
        lastCell.setTo(EMPTY_CELL_VALUE);
        return lastCell;
    }

    private TerrainCardCell createVoidCell(Terrain terrain) {
        TerrainCardCell voidCell = new TerrainCardCell();
        TerrainAssignment lastAssignment = terrain.getAssignments().last();
        voidCell.setGroup(lastAssignment.getGroup().getCaption());
        voidCell.setFrom(sdf.format(lastAssignment.getStartDate()));
        voidCell.setTo(EMPTY_CELL_VALUE);
        return voidCell;
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

    private void createEmptyCells(TerrainCardColumn column) {

        while (column.getCells().size() < TABLE_ROWS) {
            TerrainCardCell emptyCell = new TerrainCardCell();
            emptyCell.setGroup(EMPTY_CELL_VALUE);
            emptyCell.setFrom(EMPTY_CELL_VALUE);
            emptyCell.setTo(EMPTY_CELL_VALUE);
            column.addCell(emptyCell);
        }
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
            result = terrains.findByTypeAndGroupAndLastNotificationDate(type, group, date);
        } else if (type != null && group == null && date != null) {
            result = terrains.findByTypeAndLastNotificationDate(type, date);
        } else if (type == null && group != null && date != null) {
            result = terrains.findByGroupAndLastNotificationDate(group, date);
        } else if (type == null && group != null && date == null) {
            result = terrains.findByGroup(group);
        } else if (type == null && group == null && date != null) {
            result = terrains.findByLastNotificationDate(date);
        } else {
            result = terrains.findAllTerrains();
        }

        return result;
    }

    @Override
    public TerrainType getTypeFromParams(Map<String, Object> params) {
        TerrainType type = (params.get(PARAM_TYPE) == null || params.get(PARAM_TYPE).equals("")) ? null : TerrainType.valueOf(params.get(PARAM_TYPE).toString());
        return type;
    }

    @Override
    public ServiceGroup getGroupFromParams(Map<String, Object> params) throws NumberFormatException {
        ServiceGroup group = (params.get(PARAM_GROUP) == null || (params.get(PARAM_GROUP).equals("")) ? null : this.caRepositories.getServiceGroupRepository().findOne(Long.valueOf(params.get(PARAM_GROUP).toString())));
        return group;
    }

    @Override
    public Date getDateFromParams(Map<String, Object> params) {
        Date date = null;
        try {
            date = (params.get(PARAM_DATE) == null || params.get(PARAM_DATE).equals("")) ? null : this.sdf.parse(params.get(PARAM_DATE).toString());
        } catch (ParseException ex) {
            throw new RuntimeException("invalid date: " + params.get(PARAM_DATE).toString());
        }
        return date;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

}
