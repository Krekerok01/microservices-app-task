package com.specificgroup.job.service;

import com.specificgroup.job.dto.VacancyResponse;
import java.util.List;

public interface JobService {

    List<VacancyResponse> findVacancies();
}
