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
public interface TerrainNote extends Serializable {

    Long getId();

    String getContent();

    void setContent(String content);

    Date getCreatedAt();

    String getCreatedBy();
    
    Terrain getTerrain();
    
    void setTerrain(Terrain terrain);

}
