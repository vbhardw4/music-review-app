package com.reactivespring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class MusicInfo {

    @Id
    private String musicInfoId;
    @NotBlank(message = "MusicInfo.name must be present")
    private String name;
    @NotNull()
    @Positive(message = "MusicInfo.year must be present")
    private Integer year;
    private List<@NotBlank(message = "MusicInfo.Cast must be present") String> cast;
    private LocalDate releaseDate;

}
