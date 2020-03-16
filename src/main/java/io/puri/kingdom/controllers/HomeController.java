package io.puri.kingdom.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import io.puri.kingdom.services.CoronaVirusDataService;
import models.LocationStats;

@Controller
public class HomeController {
	
	@Autowired
	CoronaVirusDataService coronaVirusDataService;
	
	@RequestMapping("/")
	public String home(Model model) {
		List<LocationStats> allStats = coronaVirusDataService.getAllStats();
		
		int totalCasesCount = coronaVirusDataService.getLatestCaseCount();
		int previousCasesCount = coronaVirusDataService.getPreviousCaseCount();
		int newCasesCount = totalCasesCount - previousCasesCount;
	
		model.addAttribute("locationStats", allStats);
		model.addAttribute("totalCasesCount", totalCasesCount);
		model.addAttribute("newCasesCount", newCasesCount);
		
		return "home";
	}
}
