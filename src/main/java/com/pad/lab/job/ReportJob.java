package com.pad.lab.job;

import com.pad.lab.model.Report;
import com.pad.lab.repository.ReportRepo;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ReportJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(ReportJob.class);

    @Autowired
    ReportRepo reportRepo;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String reportType = jobDataMap.getString("reportType");
        String reportPath = jobDataMap.getString("reportPath");

        generateReport(reportType, reportPath);
    }

    private void generateReport(String reportType, String reportPath) {
        try {
            Report report1 = new Report();
            report1.setType(reportType);
            report1.setFileDownloadPath(reportPath);
            report1.setStatus("generating");
            Report savedReport = reportRepo.save(report1);
            logger.info("Generating Report of type {}", reportType);
            Thread.sleep(20000L);
            savedReport.setStatus("completed");
            reportRepo.save(savedReport);

        } catch (Exception ex) {
            logger.error("Failed to generate report");
        }
    }
}
