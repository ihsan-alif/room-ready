package app.roomready.roomready.booking.app.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class Time {

    @PrePersist
    @PreUpdate
    public void setLastUpdatedAt(Times times){
        times.setUpdatedAt(LocalDateTime.now());
    }

    @PrePersist
    public void setLastCreatedAt(Times times){
        times.setCreatedAt(LocalDateTime.now());
    }
}
