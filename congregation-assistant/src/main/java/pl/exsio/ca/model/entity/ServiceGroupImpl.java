/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.entity;

import java.util.Date;
import java.util.Objects;
import java.util.SortedSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.PreacherAssignment;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.frameset.security.userdetails.UserDetailsProvider;

/**
 *
 * @author exsio
 */
@Entity
@Table(name = "ca_service_groups")
@Inheritance(strategy = InheritanceType.JOINED)
public class ServiceGroupImpl implements ServiceGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    protected String createdBy;

    @Column(name = "group_no", nullable = false)
    protected Long no;

    @Column(name = "is_archival", columnDefinition = "BOOLEAN", nullable = false)
    protected boolean archival = false;

    @ManyToOne(targetEntity = PreacherImpl.class)
    @JoinColumn(name = "overseer_id", nullable = false)
    protected Preacher overseer;

    @OneToMany(targetEntity = PreacherAssignmentImpl.class, mappedBy = "group", cascade = CascadeType.REMOVE)
    @OrderBy("date DESC")
    protected SortedSet<PreacherAssignment> preacherAssignments;

    @OneToMany(targetEntity = TerrainAssignmentImpl.class, mappedBy = "group", cascade = CascadeType.REMOVE)
    @OrderBy("startDate DESC")
    protected SortedSet<TerrainAssignment> terrainAssignments;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
        this.createdBy = UserDetailsProvider.getUserDetails().getUsername();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public Long getNo() {
        return no;
    }

    @Override
    public void setNo(Long no) {
        this.no = no;
    }

    @Override
    public Preacher getOverseer() {
        return overseer;
    }

    @Override
    public void setOverseer(Preacher overseer) {
        this.overseer = overseer;
    }

    @Override
    public SortedSet<PreacherAssignment> getPreacherAssignments() {
        return preacherAssignments;
    }

    public void setPreacherAssignments(SortedSet<PreacherAssignment> preacherAssignments) {
        this.preacherAssignments = preacherAssignments;
    }

    @Override
    public SortedSet<TerrainAssignment> getTerrainAssignments() {
        return terrainAssignments;
    }

    public void setTerrainAssignments(SortedSet<TerrainAssignment> terrainAssignments) {
        this.terrainAssignments = terrainAssignments;
    }

    @Override
    public boolean isArchival() {
        return archival;
    }

    @Override
    public void setArchival(boolean archival) {
        this.archival = archival;
    }

    @Override
    public String toString() {
       return this.getCaption();
    }

    @Override
    public String getCaption() {
         return this.no + " (" + this.overseer + ")";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServiceGroupImpl other = (ServiceGroupImpl) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}