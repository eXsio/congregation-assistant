/* 
 * The MIT License
 *
 * Copyright 2014 exsio.
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
import java.util.SortedSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.persistence.Transient;
import pl.exsio.ca.model.OverseerAssignment;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.PreacherAssignment;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.TerrainAssignment;
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.userdetails.UserDetailsProvider;

/**
 *
 * @author exsio
 */
@Entity(name = "caServiceGroupImpl")
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

    @Column(name = "is_archival", columnDefinition = "BOOLEAN", nullable = false)
    protected boolean archival = false;

    @OneToMany(targetEntity = PreacherAssignmentImpl.class, mappedBy = "group", cascade = CascadeType.REMOVE)
    @OrderBy("date DESC")
    protected SortedSet<PreacherAssignment> preacherAssignments;

    @OneToMany(targetEntity = OverseerAssignmentImpl.class, mappedBy = "group", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @OrderBy("date DESC")
    protected SortedSet<OverseerAssignment> overseerAssignments;

    @OneToMany(targetEntity = TerrainAssignmentImpl.class, mappedBy = "group", cascade = CascadeType.REMOVE)
    @OrderBy("startDate DESC")
    protected SortedSet<TerrainAssignment> terrainAssignments;

    @Transient
    private transient OverseerAssignment latestAssignment;

    @Transient
    private transient boolean latestAssignmentChecked = false;

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
        this.checkLatestAssignment();
        if (this.latestAssignment != null) {
            return this.latestAssignment.getGroupNo();
        } else {
            return null;
        }
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
    public SortedSet<OverseerAssignment> getOverseerAssignments() {
        return this.overseerAssignments;
    }

    public void setOverseerAssignments(SortedSet<OverseerAssignment> overseerAssignments) {
        this.overseerAssignments = overseerAssignments;
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
        this.checkLatestAssignment();
        if (this.latestAssignment instanceof OverseerAssignment) {
            return this.getCaption(this.latestAssignment);
        } else {
            return "ID: "+this.getId();
        }
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

    @Override
    public Preacher getOverseer() {
        this.checkLatestAssignment();
        if (this.latestAssignment != null) {
            return this.latestAssignment.getPreacher();
        } else {
            return null;
        }
    }

    private void checkLatestAssignment() {
        if (!this.latestAssignmentChecked) {
            if (this.overseerAssignments != null && this.overseerAssignments.size() > 0) {
                this.latestAssignment = this.overseerAssignments.last();
            }
            this.latestAssignmentChecked = true;
        }
    }

    @Override
    public String getCaption(OverseerAssignment assignment) {
        return t("ca.service_group") + assignment.getGroupNo() + " (" + assignment.getPreacher() + ")";
    }

}
