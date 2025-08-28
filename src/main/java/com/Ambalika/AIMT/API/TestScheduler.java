package com.Ambalika.AIMT.API;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.Ambalika.AIMT.Repository.TestRepo;
import com.Ambalika.AIMT.model.Test;

@Component
public class TestScheduler {

    @Autowired
    private TestRepo tstrepo;    

    // Check every minute for tests to activate or deactivate
    @Scheduled(fixedRate = 10000)
    public void manageScheduledTest()
    {
        // Use ZonedDateTime with your specific time zone (example: Asia/Kolkata)
        ZonedDateTime nowZoned = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        LocalDateTime now = nowZoned.toLocalDateTime(); // Convert to LocalDateTime
        List<Test> tests = tstrepo.findAll();    
        
        for(Test test : tests)
        {
            // Activate test if start time has arrived and it's not active
            if(!test.getTeststatus().equals("Active") && test.getStarttime().isBefore(now))
            {
                test.setTeststatus("Active");
                tstrepo.save(test);
            }            
            // Deactivate test if end time (24 hours after start) has passed
            if(test.getTeststatus().equals("Active") && test.getEndTime().isBefore(now))
            {
                test.setTeststatus("Test Over");
                tstrepo.save(test);
            }
        }
    }
}
