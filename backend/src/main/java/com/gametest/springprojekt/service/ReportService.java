package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ReportDto;
import com.gametest.springprojekt.model.ReportEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.ReportRepository;
import com.gametest.springprojekt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;


    public void addReportToDB(ReportDto report) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserEntity user = userRepository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie ma takiego użytkownika!"));

        ReportEntity newReport = new ReportEntity();
        newReport.setUser(user);
        newReport.setTitle(report.getTitle());
        newReport.setDescription(report.getDescription());
        reportRepository.save(newReport);
    }


    public List<ReportEntity> getAllReports() {
        return reportRepository.findAllByOrderByCreatedAtDesc();
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
