package com.gametest.springprojekt.controller.mvc;

import com.gametest.springprojekt.model.ReportEntity;
import com.gametest.springprojekt.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mvc/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")//dodatkowe zabezpieczenie metod (bez admina leci forbiden)
public class AdminController {

    private final ReportService reportService;

    @GetMapping("/reports")
    public String showReports(Model model) {
        List<ReportEntity> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);
        return "admin/reports";
    }

    @PostMapping("/reports/delete/{id}")
    public String deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return "redirect:/admin/reports";
    }
}