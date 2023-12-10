package com.specificgroup.job.controller;

import com.specificgroup.job.dto.VacancyResponse;
import com.specificgroup.job.service.JobService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides  and support methods for processing http request and response.
 */
@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public List<VacancyResponse> findVacancies(){
        return jobService.findVacancies();
    }
}