package br.com.glad.gladtask.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Invitation {

    @Id
    @ApiModelProperty(notes = "The database generated invitation ID")
    private String id;

    @DBRef
    private User author;

    @DBRef
    private User receiver;

    @DBRef
    private Team team;

    private boolean isActive;
}
