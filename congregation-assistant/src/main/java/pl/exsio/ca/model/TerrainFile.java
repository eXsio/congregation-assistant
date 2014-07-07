/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author exsio
 */
public interface TerrainFile extends Serializable {
    
    Long getId();
    
    String getName();
    
    void setName(String name);
    
    String getDescription();
    
    void setDescription(String description);
    
    byte[] getData();
    
    void setData(byte[] data);
    
    Terrain getTerrain();
    
    void setTerrain(Terrain terrain);
    
    Date getCreatedAt();
    
    String getCreatedBy();
}
