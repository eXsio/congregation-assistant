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
package pl.exsio.ca.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;

/**
 *
 * @author exsio
 */
public interface TerrainAssignment extends Serializable, Comparable<TerrainAssignment> {

    Long getId();

    ServiceGroup getGroup();

    void setGroup(ServiceGroup group);
    
    void setPreacher(Preacher preacher);
    
    Preacher getPreacher();

    Terrain getTerrain();

    void setTerrain(Terrain terrain);

    Date getStartDate();

    void setStartDate(Date start);

    Date getEndDate();

    void setEndDate(Date end);

    String getComment();

    void setComment(String comment);

    boolean isActive();

    void setActive(boolean active);

    SortedSet<TerrainNotification> getNotifications();

    Date getCreatedAt();

    String getCreatedBy();
    
    String getCaption();
    
    boolean isNotificationDateValid(Date date);
    
    boolean isExpired();
    
    TerrainOwner getOwner();
}
