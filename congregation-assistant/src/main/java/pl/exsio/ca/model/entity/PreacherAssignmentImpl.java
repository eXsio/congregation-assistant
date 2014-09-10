/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.PreacherAssignment;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.frameset.security.userdetails.UserDetailsProvider;

/**
 *
 * @author exsio
 */
@Entity(name = "caPreacherAssignmentImpl")
@Table(name = "ca_preacher_assignments")
@Inheritance(strategy = InheritanceType.JOINED)
public class PreacherAssignmentImpl implements PreacherAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "date", nullable = false)
    protected Date date;

    @Column(name="created_at", nullable = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    protected String createdBy;

    @Column(name = "is_active", columnDefinition = "BOOLEAN", nullable = false)
    protected boolean active;

    @ManyToOne(targetEntity = ServiceGroupImpl.class)
    @JoinColumn(name = "group_id", nullable = false)
    protected ServiceGroup group;

    @ManyToOne(targetEntity = PreacherImpl.class)
    @JoinColumn(name = "preacher_id", nullable = false)
    protected Preacher preacher;

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
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
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
    public Preacher getPreacher() {
        return preacher;
    }

    @Override
    public void setPreacher(Preacher preacher) {
        this.preacher = preacher;
    }

    @Override
    public int compareTo(PreacherAssignment o) {
        return this.getDate().compareTo(o.getDate());
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final PreacherAssignmentImpl other = (PreacherAssignmentImpl) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
