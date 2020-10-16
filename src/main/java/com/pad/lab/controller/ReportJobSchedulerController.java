package com.pad.lab.controller;

import com.pad.lab.job.ReportJob;
import com.pad.lab.payload.ScheduleReportRequest;
import com.pad.lab.payload.ScheduleReportResponse;
import org.quartz.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Date;
import java.util.UUID;

import static org.slf4j.LoggerFactory.*;

@RestController
public class ReportJobSchedulerController {
    private static final Logger logger = getLogger(ReportJobSchedulerController.class);
    @Autowired
    Scheduler scheduler;

    @PostMapping("/scheduleReport")
    public ResponseEntity<ScheduleReportResponse> scheduleReport(@Valid @RequestBody ScheduleReportRequest scheduleReportRequest) {


        try {

            JobDetail jobDetail = buildJobDetail(scheduleReportRequest);
            Trigger trigger = buildJobTrigger(jobDetail);
            scheduler.scheduleJob(jobDetail, trigger);

            ScheduleReportResponse scheduleReportResponse = new ScheduleReportResponse(true, jobDetail.getKey().getName(),
                    jobDetail.getKey().getGroup(), "Report Scheduled Successfully!");
            return ResponseEntity.ok(scheduleReportResponse);
        } catch (SchedulerException ex) {
            logger.error("Error scheduling report", ex);
            ScheduleReportResponse scheduleReportResponse = new ScheduleReportResponse(false, "Error scheduling report generation. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleReportResponse);
        }
    }

    private JobDetail buildJobDetail(ScheduleReportRequest scheduleReportRequest) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("reportType", scheduleReportRequest.getReportType());
        jobDataMap.put("reportPath", scheduleReportRequest.getReportPath());


        return JobBuilder.newJob(ReportJob.class).withIdentity(UUID.randomUUID().toString(), "report-jobs")
                .withDescription("Send Report Job").usingJobData(jobDataMap).storeDurably().build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "report-triggers").withDescription("Send Report Trigger")
                .startAt(new Date())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()).build();
    }
}
