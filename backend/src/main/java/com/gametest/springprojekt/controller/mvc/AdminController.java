package com.gametest.springprojekt.controller.mvc;

import com.gametest.springprojekt.dto.CharacterDto;
import com.gametest.springprojekt.dto.RankingPositionDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.ReportEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.service.RankingService;
import com.gametest.springprojekt.service.ReportService;
import com.gametest.springprojekt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private final RankingService rankingService;
    private final UserService userService;

    @GetMapping("/reports")
    public String showReports(Model model) {
        List<ReportEntity> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);
        return "admin/reports";
    }

    @PostMapping("/reports/delete/{id}")
    public String deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return "redirect:/mvc/admin/reports";
    }

    @GetMapping("/ranking/view")
    public String rankingPage(
            @PageableDefault(
                    size = 10,
                    sort = {"auraLvl", "id"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            Model model
    ) {
        Page<CharacterDto> page = rankingService.getRanking(pageable);
        model.addAttribute("page", page);
        return "admin/ranking";
    }

    @GetMapping("/ranking/search")
    public String searchView(@RequestParam String name, Model model) {

        CharacterEntity character = rankingService.getCharacterFromPlayersList(name);

        model.addAttribute("characterId", character.getId());
        model.addAttribute("userName", character.getUser().getUsername());
        model.addAttribute("userId", character.getUser().getId());
        model.addAttribute("userEmail", character.getUser().getEmail());
        model.addAttribute("userRole", character.getUser().getRole());
        model.addAttribute("name", character.getName());
        model.addAttribute("auraLvl", character.getAuraLvl());
        model.addAttribute("cristals", character.getCristals());
        model.addAttribute("class", character.getCharacterClass().getClassName());

        return "admin/ranking-search";
    }


    @GetMapping("/ban/blackList")
    public String getBlackList(Model model) {
        List<UserEntity> bannedList = userService.getBannedList();
        model.addAttribute("bannedUsers", bannedList);
        return "admin/black-list";
    }

    @PostMapping("/ban/{id}")
    public String banUser(
            @PathVariable Long id){
        userService.banUser(id);
        return "redirect:/mvc/admin/ranking/view";
    }

    @PostMapping("/unban/{id}")
    public String unBanUser(
            @PathVariable Long id){
        userService.unBanUser(id);
        return "redirect:/mvc/admin/ranking/view";
    }


}