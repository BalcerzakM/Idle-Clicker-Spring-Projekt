package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.ReportDto;
import com.gametest.springprojekt.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<?> createReport(
            @RequestBody ReportDto report
    ){
        reportService.addReportToDB(report);
        return ResponseEntity.ok("Wysłano Zgłoszenie");
    }

}
