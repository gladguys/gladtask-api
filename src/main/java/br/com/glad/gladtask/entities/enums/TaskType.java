package br.com.glad.gladtask.entities.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskType {
	@JsonProperty("Documentação")
	DOCUMENTACAO,

	@JsonProperty("Feature")
	FEATURE,

	@JsonProperty("Bug")
	BUG,

	@JsonProperty("Melhoria")
	MELHORIA,

	@JsonProperty("Teste")
	TESTE,

	@JsonProperty("Alinhamento")
	ALINHAMENTO,

	@JsonProperty("Reunião")
	REUNIAO,

	@JsonProperty("Outro")
	OUTRO;
}
