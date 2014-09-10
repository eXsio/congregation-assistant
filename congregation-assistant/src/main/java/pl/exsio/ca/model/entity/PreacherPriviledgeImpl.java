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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import pl.exsio.ca.model.PreacherPriviledge;
import pl.exsio.ca.model.Priviledge;
import pl.exsio.frameset.security.userdetails.UserDetailsProvider;

/**
 *
 * @author exsio
 */
@Entity(name = "caPreacherPriviledgeImpl")
@Table(name = "ca_preacher_priviledges")
@Inheritance(strategy = InheritanceType.JOINED)
public class PreacherPriviledgeImpl implements PreacherPriviledge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "priviledge", nullable = false)
    protected Priviledge priviledge;

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
    protected Date endDate = null;

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
    public Priviledge getPriviledge() {
        return priviledge;
    }

    @Override
    public void setPriviledge(Priviledge priviledge) {
        this.priviledge = priviledge;
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
    public Preacher getPreacher() {
        return preacher;
    }

    @Override
    public void setPreacher(Preacher preacher) {
        this.preacher = preacher;
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
    public boolean isEnabled() {
        return this.endDate == null;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final PreacherPriviledgeImpl other = (PreacherPriviledgeImpl) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
