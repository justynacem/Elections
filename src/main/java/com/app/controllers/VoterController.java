package com.app.controllers;
import com.app.dto.VoterDto;
import com.app.model.Education;
import com.app.model.Gender;
import com.app.service.ConstituencyService;
import com.app.service.TokenService;
import com.app.service.VoterService;
import com.app.validators.VoterValidator;
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
@RequestMapping("/voters")
public class VoterController {
    private VoterService voterService;
    private VoterValidator voterValidator;
    private TokenService tokenService;
    private ConstituencyService constituencyService;

    public VoterController(VoterService voterService, VoterValidator voterValidator, TokenService tokenService, ConstituencyService constituencyService) {
        this.voterService = voterService;
        this.voterValidator = voterValidator;
        this.tokenService = tokenService;
        this.constituencyService = constituencyService;
    }

    @InitBinder(value = "/voters/showToken")
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(voterValidator);
    }


    @GetMapping("/add")
    public String addVoterGET(Model model) {
        model.addAttribute("voter", new VoterDto());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("education", Education.values());
        model.addAttribute("constituencies", constituencyService.getAllConstituencies());
        model.addAttribute("errors", new HashMap<>());
        return "voters/add";
    }

    @PostMapping("/showToken")
    public String showToken(@Valid @ModelAttribute VoterDto voterDto,
                            BindingResult bindingResult,
                            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode));

            model.addAttribute("voter", voterDto);
            model.addAttribute("genders", Gender.values());
            model.addAttribute("education", Education.values());
            model.addAttribute("constituencies", constituencyService.getAllConstituencies());
            model.addAttribute("errors", errors);
            return "voters/add";
        }
        VoterDto voter = voterService.addVoter(voterDto);
        model.addAttribute("token", tokenService.getTokenById(voter.getId()));
        return "voters/showToken";
    }
}
