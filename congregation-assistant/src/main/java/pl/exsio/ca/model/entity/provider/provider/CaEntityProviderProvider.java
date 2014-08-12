/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.model.entity.provider.provider;

import com.vaadin.addon.jpacontainer.EntityProvider;

/**
 *
 * @author exsio
 */
public interface CaEntityProviderProvider {
    
    EntityProvider getPreacherAssignmentEntityProvider();
    
    EntityProvider getOverseerAssignmentEntityProvider();
    
    EntityProvider getPreacherEntityProvider();
    
    EntityProvider getPreacherPriviledgeEntityProvider();
    
    EntityProvider getServiceGroupEntityProvider();
    
    EntityProvider getTerrainAssignmentEntityProvider();
    
    EntityProvider getTerrainEntityProvider();
    
    EntityProvider getTerrainFileEntityProvider();
    
    EntityProvider getTerrainNoteEntityProvider();
    
    EntityProvider getTerrainNotificationEntityProvider();
}
