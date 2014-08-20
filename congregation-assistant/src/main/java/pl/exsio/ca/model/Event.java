/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author exsio
 */
public interface Event extends Serializable {

    Long getId();

    String getName();

    void setName(String name);

    Date getStartDate();

    void setStartDate(Date date);

    Date getEndDate();

    void setEndDate(Date date);

    Date getCreatedAt();

    String getCreatedBy();

}
