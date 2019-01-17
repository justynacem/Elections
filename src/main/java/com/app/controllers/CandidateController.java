package com.app.controllers;

import com.app.dto.CandidateDto;
import com.app.service.CandidateService;
import com.app.service.ConstituencyService;
import com.app.service.PoliticalPartyService;
import com.app.validators.CandidateValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    private CandidateService candidateService;
    private CandidateValidator candidateValidator;
    private PoliticalPartyService politicalPartyService;
    private ConstituencyService constituencyService;

    public CandidateController(CandidateService candidateService, CandidateValidator candidateValidator,
                               PoliticalPartyService politicalPartyService, ConstituencyService constituencyService) {
        this.candidateService = candidateService;
        this.candidateValidator = candidateValidator;
        this.politicalPartyService = politicalPartyService;
        this.constituencyService = constituencyService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(candidateValidator);
    }

    @GetMapping("/add")
    public String addCandidateGET(Model model) {
        model.addAttribute("candidate", new CandidateDto());
        model.addAttribute("constituencies", constituencyService.getAllConstituencies());
        model.addAttribute("parties", politicalPartyService.getAllPoliticalParties());
        model.addAttribute("errors", new HashMap<>());
        return "candidates/add";
    }

    @PostMapping("/add")
    public String addCandidatePOST(@Valid @ModelAttribute CandidateDto candidateDto,
                                      BindingResult bindingResult,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode));

            model.addAttribute("candidate", candidateDto);
            model.addAttribute("constituencies", constituencyService.getAllConstituencies());
            model.addAttribute("parties", politicalPartyService.getAllPoliticalParties());
            model.addAttribute("errors", errors);
            return "candidates/add";
        }
        candidateService.addCandidate(candidateDto);
        return "redirect:/candidates";
    }

    @GetMapping
    public String getAllCandidates(Model model) {
        model.addAttribute("candidates", candidateService.getAllCandidates());
        return "candidates/all";
    }

    @PostMapping("/delete")
    public String deleteCandidate(@RequestParam Long id) {
        candidateService.deleteCandidate(id);
        return "redirect:/candidates";
    }
}