package it.unisannio.controller.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class LoginDTO implements Serializable {

   @NotNull
   @Size(min = 1, max = 50)
   private String username;

   @NotNull
   @Size(min = 4, max = 100)
   private String password;

   public LoginDTO() { }

   public LoginDTO(@NotNull @Size(min = 1, max = 50) String username, @NotNull @Size(min = 4, max = 100) String password) {
      this.username = username;
      this.password = password;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }
}
