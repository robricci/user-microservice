package it.unisannio.security.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTTokenDTO {
	private String idToken;

    public JWTTokenDTO(String idToken) {
       this.idToken = idToken;
    }

    @JsonProperty("id_token")
    String getIdToken() {
       return idToken;
    }

    void setIdToken(String idToken) {
       this.idToken = idToken;
    }
}
