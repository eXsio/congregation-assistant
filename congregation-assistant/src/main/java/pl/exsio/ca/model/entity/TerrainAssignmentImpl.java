/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
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
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.frameset.security.userdetails.UserDetailsProvider;

/**
 *
 * @author exsio
 */
@Entity
@Table(name = "ca_terrain_assignments")
@Inheritance(strategy = InheritanceType.JOINED)
public class TerrainAssignmentImpl implements TerrainAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    protected String createdBy;

    @Column(name = "start_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date startDate;

    @Column(name = "end_date", nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date endDate;

    @Column(name = "comment")
    protected String comment;

    @Column(name = "is_active", columnDefinition = "BOOLEAN", nullable = false)
    protected boolean active;

    @ManyToOne(targetEntity = ServiceGroupImpl.class)
    @JoinColumn(name = "group_id", nullable = false)
    protected ServiceGroup group;

    @ManyToOne(targetEntity = TerrainImpl.class)
    @JoinColumn(name = "terrain_id", nullable = false)
    protected Terrain terrain;

    @OneToMany(targetEntity = TerrainNotificationImpl.class, mappedBy = "assignment", cascade = CascadeType.REMOVE)
    @OrderBy("date DESC")
    protected SortedSet<TerrainNotification> notifications;

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
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public ServiceGroup getGroup() {
        return group;
    }

    @Override
    public void setGroup(ServiceGroup group) {
        this.group = group;
    }

    @Override
    public Terrain getTerrain() {
        return terrain;
    }

    @Override
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    @Override
    public SortedSet<TerrainNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(SortedSet<TerrainNotification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public String getCaption() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String caption = this.group.getCaption() + " (" + sdf.format(this.startDate);
        if (this.endDate instanceof Date) {
            caption += " - " + sdf.format(this.endDate);
        }
        caption += ")";
        return caption;
    }

    @Override
    public String toString() {
        return this.getCaption();
    }

    @Override
    public int compareTo(TerrainAssignment o) {
        return this.getStartDate().compareTo(o.getStartDate());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final TerrainAssignmentImpl other = (TerrainAssignmentImpl) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isNotificationDateValid(Date date) {
        return date.compareTo(this.startDate) >= 0 && (this.endDate == null || date.compareTo(this.endDate) <= 0);
    }

}