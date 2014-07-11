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
public class TerrainCardPage {

    private String pageTitle;

    private final List<TerrainCardColumn> columns;

    private int pageNo;

    public TerrainCardPage() {
        this.columns = new LinkedList<>();
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<TerrainCardColumn> getColumns() {
        return columns;
    }
    
    public void addColumn(TerrainCardColumn column) {
        this.columns.add(column);
    }

}
