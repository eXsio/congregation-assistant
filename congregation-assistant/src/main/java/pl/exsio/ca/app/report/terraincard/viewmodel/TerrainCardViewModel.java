/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.app.report.terraincard.viewmodel;

import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import pl.exsio.ca.app.report.terraincard.model.TerrainCardPage;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.TerrainType;

/**
 *
 * @author exsio
 */
public interface TerrainCardViewModel {

    String EMPTY_CELL_VALUE = "x";

    LinkedList<TerrainCardPage> getPages(Map<String, Object> params);

    int getTableColumnsNo();

    TerrainType getTypeFromParams(Map<String, Object> params);

    ServiceGroup getGroupFromParams(Map<String, Object> params) throws NumberFormatException;

    Date getDateFromParams(Map<String, Object> params);
}
