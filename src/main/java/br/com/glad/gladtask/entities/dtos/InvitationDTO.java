package br.com.glad.gladtask.entities.dtos;

import lombok.Data;

@Data
public class InvitationDTO {
    private String id;
    private String authorUserId;
    private String receiverUserId;
    private String teamId;
    private boolean isActive;
}
