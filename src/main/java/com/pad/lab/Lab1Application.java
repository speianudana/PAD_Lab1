package com.pad.lab;


import com.pad.lab.controller.ReportJobSchedulerController;
import com.pad.lab.job.RegisterJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
public class Lab1Application {

	private static final Logger logger = getLogger(ReportJobSchedulerController.class);


	public static void main(String[] args) {
		SpringApplication.run(Lab1Application.class, args);
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail jobDetail = buildJobDetail();
			Trigger trigger = buildJobTrigger(jobDetail);
			scheduler.start();
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {

			logger.info("Register job error!");
		}
    }

	private static JobDetail buildJobDetail() {
		JobDataMap jobDataMap = new JobDataMap();

		return JobBuilder.newJob(RegisterJob.class).withIdentity(UUID.randomUUID().toString(), "register-jobs")
				.withDescription("Send Register Job").usingJobData(jobDataMap).storeDurably().build();
	}

	private static Trigger buildJobTrigger(JobDetail jobDetail) {
		return TriggerBuilder.newTrigger().forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), "register-triggers").withDescription("Send Register Trigger")
				.startAt(new Date())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()).build();
	}
}
