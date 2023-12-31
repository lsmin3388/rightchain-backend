package com.principes.rightchain.report.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.report.dto.request.ReportCreateRequest;
import com.principes.rightchain.report.dto.response.ReportReadResponse;
import com.principes.rightchain.report.service.ReportService;
import com.principes.rightchain.security.details.PrincipalDetails;
import com.principes.rightchain.utils.api.ApiUtil;
import com.principes.rightchain.utils.api.ApiUtil.ApiSuccessResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ApiSuccessResult<String> writeReport(
            Authentication authentication,
            @RequestBody ReportCreateRequest reportCreateRequest) {

        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        Account account = userDetails.getAccount();

        String caseNum = reportService.writeReport(account, reportCreateRequest);

        return ApiUtil.success(caseNum);
    }

    @GetMapping("/my-school")
    public ApiSuccessResult<List<ReportReadResponse>> readReportMySchool(
            Authentication authentication
    ) {
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        Account account = userDetails.getAccount();

        List<ReportReadResponse> reportReadResponse = reportService.readAllReport();
        List<ReportReadResponse> readResponses = new ArrayList<>();

        for (ReportReadResponse report : reportReadResponse) {
            if (report.getSchoolName().equals(account.getSchoolName())) {
                readResponses.add(report);
            }
        }

        return ApiUtil.success(readResponses);
    }

    @GetMapping("/{caseNum}")
    public ApiSuccessResult<ReportReadResponse> readReportByCaseNum(
            @PathVariable String caseNum) {

        ReportReadResponse reportReadResponse = reportService.readReport(caseNum);

        return ApiUtil.success(reportReadResponse);
    }

    @GetMapping
    public ApiSuccessResult<List<ReportReadResponse>> readAllReport() {

        List<ReportReadResponse> reportReadResponses = reportService.readAllReport();

        return ApiUtil.success(reportReadResponses);
    }

}
