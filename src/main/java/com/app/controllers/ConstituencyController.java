package com.app.controllers;

import com.app.dto.ConstituencyDto;
import com.app.service.ConstituencyService;
import com.app.validators.ConstituencyValidator;
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
@RequestMapping("/constituencies")
public class ConstituencyController {
    private ConstituencyService constituencyService;
    private ConstituencyValidator constituencyValidator;

    public ConstituencyController(ConstituencyService constituencyService, ConstituencyValidator constituencyValidator) {
        this.constituencyService = constituencyService;
        this.constituencyValidator = constituencyValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(constituencyValidator);
    }

    @GetMapping("/add")
    public String addConstituencyGET(Model model) {
        model.addAttribute("constituency", new ConstituencyDto());
        model.addAttribute("errors", new HashMap<>());
        return "constituencies/add";
    }

    @PostMapping("/add")
    public String addConstituencyPOST(@Valid @ModelAttribute ConstituencyDto constituencyDto,
                                      BindingResult bindingResult,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode));

            model.addAttribute("constituency", constituencyDto);
            model.addAttribute("errors", errors);
            return "constituencies/add";
        }
        constituencyService.addConstituency(constituencyDto);
        return "redirect:/constituencies";
    }

    @GetMapping
    public String getAllConstituencies(Model model) {
        model.addAttribute("constituencies", constituencyService.getAllConstituencies());
        return "constituencies/all";
    }

    @PostMapping("/delete")
    public String deleteConstituency(@RequestParam Long id) {
        constituencyService.deleteConstituency(id);
        return "redirect:/constituencies";
    }
}