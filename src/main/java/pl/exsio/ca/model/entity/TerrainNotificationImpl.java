/* 
 * The MIT License
 *
 * Copyright 2015 exsio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.exsio.ca.model.entity;

import java.util.Date;
import java.util.Objects;
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
import pl.exsio.ca.model.Event;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.frameset.security.userdetails.UserDetailsProvider;

/**
 *
 * @author exsio
 */
@Entity(name = "caTerrainNotificationImpl")
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
    
    @ManyToOne(targetEntity = PreacherImpl.class)
    @JoinColumn(name = "override_preacher_id", nullable = true)
    protected Preacher overridePreacher;

    @ManyToOne(targetEntity = EventImpl.class)
    @JoinColumn(name = "event_id", nullable = true)
    protected Event event;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
        this.createdBy = UserDetailsProvider.getUserDetails().getUsername();
    }

    @Override
    public Preacher getOverridePreacher() {
        return overridePreacher;
    }

    @Override
    public void setOverridePreacher(Preacher overridePreacher) {
        this.overridePreacher = overridePreacher;
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

    @Override
    public Event getEvent() {
        return event;
    }

    @Override
    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public int compareTo(TerrainNotification o) {
        return this.getDate().compareTo(o.getDate());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final TerrainNotificationImpl other = (TerrainNotificationImpl) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
