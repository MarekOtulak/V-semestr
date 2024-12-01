/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.BeatLoop.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Marek
 */
@Entity
@Table(name = "userrole")
@NamedQueries({
    @NamedQuery(name = "Userrole.findAll", query = "SELECT u FROM Userrole u"),
    @NamedQuery(name = "Userrole.findByUserroleid", query = "SELECT u FROM Userrole u WHERE u.userroleid = :userroleid"),
    @NamedQuery(name = "Userrole.findByAssignedAt", query = "SELECT u FROM Userrole u WHERE u.assignedAt = :assignedAt"),
    @NamedQuery(name = "Userrole.findByRevokedAt", query = "SELECT u FROM Userrole u WHERE u.revokedAt = :revokedAt")})
public class Userrole implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Userrole_id")
    private Integer userroleid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "assigned_at")
    @Temporal(TemporalType.DATE)
    private Date assignedAt;
    @Column(name = "revoked_at")
    @Temporal(TemporalType.DATE)
    private Date revokedAt;
    @JoinColumn(name = "Role_role_id", referencedColumnName = "role_id")
    @ManyToOne(optional = false)
    private Role roleroleid;
    @JoinColumn(name = "User_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User useruserid;

    public Userrole() {
    }

    public Userrole(Integer userroleid) {
        this.userroleid = userroleid;
    }

    public Userrole(Integer userroleid, Date assignedAt) {
        this.userroleid = userroleid;
        this.assignedAt = assignedAt;
    }

    public Integer getUserroleid() {
        return userroleid;
    }

    public void setUserroleid(Integer userroleid) {
        this.userroleid = userroleid;
    }

    public Date getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Date assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Date getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Date revokedAt) {
        this.revokedAt = revokedAt;
    }

    public Role getRoleroleid() {
        return roleroleid;
    }

    public void setRoleroleid(Role roleroleid) {
        this.roleroleid = roleroleid;
    }

    public User getUseruserid() {
        return useruserid;
    }

    public void setUseruserid(User useruserid) {
        this.useruserid = useruserid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userroleid != null ? userroleid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Userrole)) {
            return false;
        }
        Userrole other = (Userrole) object;
        if ((this.userroleid == null && other.userroleid != null) || (this.userroleid != null && !this.userroleid.equals(other.userroleid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beatloop.entities.Userrole[ userroleid=" + userroleid + " ]";
    }
    
}
