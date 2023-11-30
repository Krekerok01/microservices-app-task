package com.specificgroup.job.service;

import com.specificgroup.job.dto.VacancyResponse;
import java.util.List;

public interface JobService {

    /**
     * Load a list of vacancies from a third-party service
     *
     * @return a list of VacancyResponse objects
     */
    List<VacancyResponse> findVacancies();
}
