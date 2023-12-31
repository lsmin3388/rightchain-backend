package com.principes.rightchain.report.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.agree.entity.Agree;
import com.principes.rightchain.agree.repository.AgreeRepository;
import com.principes.rightchain.chain.entity.Chain;
import com.principes.rightchain.chain.service.ChainService;
import com.principes.rightchain.exception.NotFoundException;
import com.principes.rightchain.report.dto.request.ReportCreateRequest;
import com.principes.rightchain.report.dto.response.ReportReadResponse;
import com.principes.rightchain.report.entity.Report;
import com.principes.rightchain.report.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final ChainService chainService;
    private final AgreeRepository agreeRepository;

    @Transactional
    public String writeReport(
            Account account,
            ReportCreateRequest reportCreateRequest) {
        Report report = reportRepository.save(reportCreateRequest.toEntity(account));

        chainService.createChain(report);
        return getCaseNumFromId(report.getId());
    }

    public ReportReadResponse readReport(String caseNum) {
        Report report = this.findReportByCaseNum(caseNum);
        List<Chain> chains = chainService.findAllByReport(report);
        List<Agree> agrees = agreeRepository.findAllByReport(report);

        return new ReportReadResponse(report, agrees, chains);
    }

    public List<ReportReadResponse> readAllReport() {
        List<Report> reports = this.findAllReport();
        List<ReportReadResponse> reportReadResponses = new ArrayList<>();

        for (Report report : reports) {
            List<Chain> chains = chainService.findAllByReport(report);
            List<Agree> agrees = agreeRepository.findAllByReport(report);

            reportReadResponses.add(new ReportReadResponse(report, agrees, chains));
        }

        return  reportReadResponses;
    }

    public Report findReportByCaseNum(String caseNum) {
        return reportRepository.findById(getIdFromCaseNum(caseNum))
                .orElseThrow(() -> new NotFoundException("해당 사건 번호로 사건을 조회할 수 없습니다."));
    }

    public List<Report> findAllReport() {
        return reportRepository.findAll();
    }

    private Long getIdFromCaseNum(String caseNum) {
        return Long.parseLong(caseNum) % 10000;
    }

    private String getCaseNumFromId(Long id) {
        return "2023" + String.format("%04d", id);
    }
}
