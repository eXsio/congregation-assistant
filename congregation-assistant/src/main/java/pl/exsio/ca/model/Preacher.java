/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import pl.exsio.frameset.security.model.User;

/**
 *
 * @author exsio
 */
public interface Preacher extends Serializable {

    Long getId();

    String getFirstName();

    void setFirstName(String name);

    String getLastName();

    void setLastName(String name);

    String getEmail();

    void setEmail(String email);

    String getPhoneNo();

    void setPhoneNo(String phoneNo);

    User getUser();

    void setUser(User user);

    boolean hasUser();

    SortedSet<PreacherAssignment> getAssignments();

    Set<PreacherPriviledge> getPriviledges();

    Set<Priviledge> getGrantedPriviledges();

    Date getCreatedAt();

    String getCreatedBy();
        
    boolean isArchival();
    
    void setArchival(boolean archival);
    
    String getCaption();
}
