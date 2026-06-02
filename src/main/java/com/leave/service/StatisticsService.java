package com.leave.service;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    Map<String, Object> dashboard(Long userId);
    List<Map<String, Object>> studentStats(Long studentId);
    List<Map<String, Object>> classStats(Long userId);
}
