package com.example.calenderdemo.controller;

import com.example.calenderdemo.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    HomeService homeService;

    @GetMapping("/")
    public String index (Model model){

        // Udskiftes med Parameters fra HTML siden @RequestParam
        int monthInput = 4;
        int yearInput = 2024;

        Calendar calendar = Calendar.getInstance();
        List<List<Integer>> weeks = new ArrayList<>();

        /* sætter de ønskede kalender isntillinger
        * Year sætter årstal
        * Month sætter måned, (-1 skyldes at månederne er indexerede, dvs jan. er 0, feb er 1 osv.)
        * Day of month sætter den første dag i måneden til at være 1
        * First Day of Week sætter Mandag til at være første dag på ugen, da Søndag er standard
        * */
        calendar.set(Calendar.YEAR, yearInput);

        if (monthInput == 1) {
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
        } else {
            calendar.set(Calendar.MONTH, monthInput - 1);
        }

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        /*
        * Day of Week variabel sætter den første dag i ugen til at være Mandag og sætter index på mandag til at være 0
        * Actual Maximum sætter max dage der er på den pågældende måned, denne tager også højde for skudår
        * year er årstallet
        * */

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        // Models bliver oprettet så de kan blive displayed på HTML siden.

        model.addAttribute("year", year);
        model.addAttribute("month", monthInput);

        List<Integer> week = new ArrayList<>();


        // Loopet sætter værdien til at være null på de pladser før den første dag i ugen
        for (int i = 0; i < dayOfWeek; i++){
            week.add(null);
        }

        /* Loopet tilføjer dage til en week array som bliver smidt ind i weeks array.
        *  Herefter orpetter den en ny week array, hvor der kan tilføjes nye dage til.
        */
        for (int day = 1; day <= daysInMonth; day++){
            week.add(day);
            if (week.size() == 7) {
                weeks.add(week);
                week = new ArrayList<>();
            }
        }


        // If og loop sætter værdien på i'ets palds til null når i overgår længden på den begyndende uge
        if (week.size() > 0) {
            for (int i = week.size(); i < 7; i++){
                week.add(null);
            }
            weeks.add(week);
        }


        model.addAttribute("weeks", weeks);

        return "home/index";
    }


    @GetMapping("/dayView")
    public String showDayView(){
        return "home/dayView";
    }

    @GetMapping("/weekView")
    public String showWeekView(){
        return "home/weekView";
    }
}
