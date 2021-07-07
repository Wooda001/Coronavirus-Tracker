package Andy.Woodard.Coronavirus.Tracker.controllers;


import Andy.Woodard.Coronavirus.Tracker.models.LocationStats;
import org.springframework.beans.factory.annotation.Autowired;
import Andy.Woodard.Coronavirus.Tracker.services.CoronaVirusDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.xml.stream.Location;
import java.lang.reflect.Array;
import java.util.List;
import java.util.stream.IntStream;

@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();
        int totalCases = coronaVirusDataService.calculateTotal();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalCases);

        return "home";
    }
}
