/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.module.terrain.report.registry;

import java.util.LinkedHashSet;
import java.util.Set;
import pl.exsio.ca.module.terrain.report.Report;


public class ReportRegistryImpl implements ReportRegistry {

    protected final Set<Report> reports;
    
    public ReportRegistryImpl() {
        this.reports = new LinkedHashSet<>();
    }
    
    @Override
    public void registerReport(Report report) {
        this.reports.add(report);
    }

    @Override
    public void setReports(Iterable<Report> reports) {
        for(Report report: reports) {
            this.registerReport(report);
        }
    }

    @Override
    public Set<Report> getRegisteredReports() {
        return reports;
    }
    
}
