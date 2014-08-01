/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.entity;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainFile;
import pl.exsio.ca.model.TerrainNote;
import pl.exsio.ca.model.TerrainType;
import pl.exsio.frameset.security.userdetails.UserDetailsProvider;

/**
 *
 * @author exsio
 */
@Entity
@Table(name = "ca_terrains", uniqueConstraints = @UniqueConstraint(columnNames = {"type", "terrain_no"}, name = "uniqueTerrain"))
@Inheritance(strategy = InheritanceType.JOINED)
public class TerrainImpl implements Terrain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    protected String createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    protected TerrainType type;

    @Column(name = "terrain_no", nullable = false)
    protected Long no;

    @Column(name = "terrain_name", nullable = false)
    protected String name;

    @Column(name = "is_archival", columnDefinition = "BOOLEAN", nullable = false)
    protected boolean archival = false;

    @OneToMany(targetEntity = TerrainFileImpl.class, mappedBy = "terrain", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt DESC")
    protected SortedSet<TerrainFile> files;

    @OneToMany(targetEntity = TerrainAssignmentImpl.class, mappedBy = "terrain", cascade = CascadeType.REMOVE)
    @OrderBy("startDate DESC")
    protected SortedSet<TerrainAssignment> assignments;

    @OneToMany(targetEntity = TerrainNoteImpl.class, mappedBy = "terrain", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt DESC")
    protected Set<TerrainNote> notes;

    @Column(name = "last_notification_date", nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date lastNotificationDate;

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
    public TerrainType getType() {
        return type;
    }

    @Override
    public void setType(TerrainType type) {
        this.type = type;
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
    public SortedSet<TerrainFile> getFiles() {
        return files;
    }

    public void setFiles(SortedSet<TerrainFile> files) {
        this.files = files;
    }

    @Override
    public SortedSet<TerrainAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(SortedSet<TerrainAssignment> assignments) {
        this.assignments = assignments;
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
    public Set<TerrainNote> getNotes() {
        return notes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setNotes(Set<TerrainNote> notes) {
        this.notes = notes;
    }

    @Override
    public Date getLastNotificationDate() {
        return lastNotificationDate;
    }

    @Override
    public void setLastNotificationDate(Date lastNotificationDate) {
        this.lastNotificationDate = lastNotificationDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final TerrainImpl other = (TerrainImpl) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.no + ". " + this.name + " (" + this.type + ")";
    }

}
