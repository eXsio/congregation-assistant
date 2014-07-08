/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.entity;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.PreacherAssignment;
import pl.exsio.ca.model.PreacherPriviledge;
import pl.exsio.ca.model.Priviledge;
import pl.exsio.frameset.security.entity.UserImpl;
import pl.exsio.frameset.security.model.User;
import pl.exsio.frameset.security.userdetails.UserDetailsProvider;

/**
 *
 * @author exsio
 */
@Entity
@Table(name = "ca_preachers")
@Inheritance(strategy = InheritanceType.JOINED)
public class PreacherImpl implements Preacher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "first_name", nullable = false)
    protected String firstName;

    @Column(name = "last_name", nullable = false)
    protected String lastName;

    @Column(name = "email", nullable = true)
    protected String email;

    @Column(name = "phone_no", nullable = true)
    protected String phoneNo;

    @ManyToOne(targetEntity = UserImpl.class)
    @JoinColumn(name = "user_id", nullable = true)
    protected User user;

    @OneToMany(targetEntity = PreacherPriviledgeImpl.class, mappedBy = "preacher", cascade = CascadeType.REMOVE)
    protected Set<PreacherPriviledge> priviledges;

    @OneToMany(targetEntity = PreacherAssignmentImpl.class, mappedBy = "preacher", cascade = CascadeType.REMOVE)
    protected Set<PreacherAssignment> assignments;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    protected String createdBy;

    @Column(name = "is_archival", columnDefinition = "BOOLEAN", nullable = false)
    protected boolean archival = false;

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
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhoneNo() {
        return phoneNo;
    }

    @Override
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Set<PreacherPriviledge> getPriviledges() {
        return priviledges;
    }

    public void setPriviledges(Set<PreacherPriviledge> priviledges) {
        this.priviledges = priviledges;
    }

    @Override
    public Set<PreacherAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<PreacherAssignment> assignments) {
        this.assignments = assignments;
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
    public boolean hasUser() {
        return this.user instanceof User;
    }

    @Override
    public Set<Priviledge> getGrantedPriviledges() {
        Set<Priviledge> gp = new HashSet<>();
        for (PreacherPriviledge pr : this.priviledges) {
            gp.add(pr.getPriviledge());
            gp.addAll(Arrays.asList(pr.getPriviledge().getSubPriviledges()));
        }
        return gp;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public boolean isArchival() {
        return archival;
    }

    @Override
    public void setArchival(boolean archival) {
        this.archival = archival;
    }

}
