/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.frameset.security.userdetails.UserDetailsProvider;

/**
 *
 * @author exsio
 */
@Entity
@Table(name = "ca_terrain_notifications")
@Inheritance(strategy = InheritanceType.JOINED)
public class TerrainNotificationImpl implements TerrainNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    protected String createdBy;

    @Column(name = "date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date date;

    @Column(name = "comment")
    protected String comment;

    @ManyToOne(targetEntity = TerrainAssignmentImpl.class)
    @JoinColumn(name = "terrain_assignment_id", nullable = false)
    protected TerrainAssignment assignment;

    @ManyToOne(targetEntity = ServiceGroupImpl.class)
    @JoinColumn(name = "override_group_id", nullable = true)
    protected ServiceGroup overrideGroup;

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
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public TerrainAssignment getAssignment() {
        return assignment;
    }

    @Override
    public void setAssignment(TerrainAssignment assignment) {
        this.assignment = assignment;
    }

    @Override
    public ServiceGroup getOverrideGroup() {
        return overrideGroup;
    }

    @Override
    public void setOverrideGroup(ServiceGroup overrideGroup) {
        this.overrideGroup = overrideGroup;
    }

    @Override
    public boolean isOverriden() {
        return this.overrideGroup instanceof ServiceGroup;
    }

}
