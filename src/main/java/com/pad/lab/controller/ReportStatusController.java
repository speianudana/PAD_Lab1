package com.pad.lab.controller;

import com.pad.lab.model.Report;
import com.pad.lab.payload.ReportStatusRequest;
import com.pad.lab.payload.ReportStatusResponse;
import com.pad.lab.repository.ReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ReportStatusController {
    @Autowired
    ReportRepo reportRepo;

    @GetMapping("/getReportStatus")
    public ResponseEntity<ReportStatusResponse> getReportStatus(@RequestParam String id) {

        try {
            Report report = reportRepo.getOne(Long.parseLong(id));
            ReportStatusResponse reportStatusResponse = new ReportStatusResponse();
            reportStatusResponse.setReportId(report.getId());
            reportStatusResponse.setReportType(report.getType());
            reportStatusResponse.setReportStatus(report.getStatus());

            return ResponseEntity.ok(reportStatusResponse);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ReportStatusResponse());
        }
    }

    @GetMapping("/getNumberOfRunningJobs")
    public @ResponseBody
    String getNumberOfGeneratingReports() {
        long count = reportRepo.findAll().stream().filter(report -> report.getStatus().equals("generating")).count();
        return "There are " + count + " running processes.";
    }

    @PutMapping("/putReportStatus")
    public ResponseEntity<ReportStatusResponse> setReportStatus(@Valid @RequestBody ReportStatusRequest reportStatusRequest) {

        try {
            Report report = reportRepo.getOne(Long.parseLong(reportStatusRequest.getReportId()));
            report.setStatus(reportStatusRequest.getReportStatus());
            reportRepo.save(report);
            Report updatedReport = reportRepo.getOne(Long.parseLong(reportStatusRequest.getReportId()));
            ReportStatusResponse reportStatusResponse = new ReportStatusResponse();
            reportStatusResponse.setReportId(updatedReport.getId());
            reportStatusResponse.setReportType(updatedReport.getType());
            reportStatusResponse.setReportStatus(updatedReport.getStatus());

            return ResponseEntity.ok(reportStatusResponse);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ReportStatusResponse());
        }
    }
}
