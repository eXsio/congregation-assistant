/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.app.report.terraincard.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author exsio
 */
public class TerrainCardColumn {

    private String terrainName;

    private final List<TerrainCardCell> cells;

    public TerrainCardColumn(String terrainName) {
        this.terrainName = terrainName;
        this.cells = new LinkedList();
    }

    public String getTerrainName() {
        return terrainName;
    }

    public void setTerrainName(String terrainName) {
        this.terrainName = terrainName;
    }

    public List<TerrainCardCell> getCells() {
        return cells;
    }

    public void addCell(TerrainCardCell cell) {
        this.cells.add(cell);
    }
}
