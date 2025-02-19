package com.abishek.financeapi.Controller;

import com.abishek.financeapi.DTO.StatsDTO;
import com.abishek.financeapi.Service.Stats.StatsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class StatsControllerTest {
//Returns HTTP 200 with valid user stats for existing user ID
    @Test
    void getStats() {
        StatsService statsService = Mockito.mock(StatsService.class);
        StatsDTO mockStats = new StatsDTO(); // Assume StatsDTO has a default constructor
        Mockito.when(statsService.getUserStats(1L)).thenReturn(mockStats);

        StatsController statsController = new StatsController(statsService);
        ResponseEntity<?> response = statsController.getStats(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockStats, response.getBody());
    }
    //Handles non-existent user ID gracefully
    @Test
    public void test_get_stats_non_existent_user_id() {
        StatsService statsService = Mockito.mock(StatsService.class);
        Mockito.when(statsService.getUserStats(999L)).thenReturn(null);

        StatsController statsController = new StatsController(statsService);
        ResponseEntity<?> response = statsController.getStats(999L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

}