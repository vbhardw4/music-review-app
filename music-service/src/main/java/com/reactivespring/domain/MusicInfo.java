package com.reactivespring.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class MusicInfo {
    private String musicInfoId;
    @NotBlank(message = "musicInfo.name must be present")
    private String name;
    @NotNull
    @Positive(message = "musicInfo.year must be a Positive Value")
    private Integer year;

    @NotNull
    private List<@NotBlank(message = "musicInfo.cast must be present") String> cast;
    private LocalDate release_date;
}