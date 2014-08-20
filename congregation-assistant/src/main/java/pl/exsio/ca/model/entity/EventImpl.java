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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import pl.exsio.ca.model.Event;
import pl.exsio.frameset.security.userdetails.UserDetailsProvider;

/**
 *
 * @author exsio
 */
@Entity
@Table(name = "ca_events")
@Inheritance(strategy = InheritanceType.JOINED)
public class EventImpl implements Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    protected String createdBy;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    protected Date startDate;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "end_date", nullable = false)
    protected Date endDate;

    @Column(name = "event_name", nullable = false)
    protected String name;

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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final EventImpl other = (EventImpl) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
