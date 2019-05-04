package br.com.glad.gladtask.entities.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StatusEnum {
	CRIADA,
	EM_ESPERA,
	EM_ANDAMENTO,
	CONCLUIDA;

	public static StatusEnum getStatus(String status) {
		switch (status) {
			case "Criada": return CRIADA;
			case "Em espera": return EM_ESPERA;
			case "Em andamento": return EM_ANDAMENTO;
			case "Concluída": return CONCLUIDA;
			default: return CRIADA;
		}
	}

	public String getDescription() {
		switch (this) {
			case CRIADA: return "Criada";
			case EM_ESPERA: return "Em espera";
			case EM_ANDAMENTO: return "Em andamento";
			case CONCLUIDA: return "Concluída";
			default: return "Criada";
		}
	}
}