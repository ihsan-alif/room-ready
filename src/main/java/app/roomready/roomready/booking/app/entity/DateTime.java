package app.roomready.roomready.booking.app.entity;

import app.roomready.roomready.booking.app.listener.Times;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners({
        Times.class
})
public class DateTime implements Times {

    @Column(name = "created_at")
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
    @JsonIgnore
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy/MM/dd, HH:mm:ss", timezone = "Asia/Jakarta")
    @JsonIgnore
    private LocalDateTime updatedAt;

}
