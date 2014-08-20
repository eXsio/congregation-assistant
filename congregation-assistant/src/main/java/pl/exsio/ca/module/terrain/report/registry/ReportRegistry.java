/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.module.terrain.report.registry;

import java.util.Set;
import pl.exsio.ca.module.terrain.report.Report;

/**
 *
 * @author exsio
 */
public interface ReportRegistry {
    
    void registerReport(Report report);
    
    void setReports(Iterable<Report> reports);
    
    Set<Report> getRegisteredReports();
}
