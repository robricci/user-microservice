package it.unisannio.controller.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class RegisterDTO implements Serializable {

   @NotNull
   @Size(min = 1, max = 50)
   private String username;

   @NotNull
   @Size(min = 4, max = 100)
   private String password;

   @NotNull
   @Size(min = 4, max = 50)
   private String firstname;

   @NotNull
   @Size(min = 4, max = 50)
   private String lastname;

   @NotNull
   @Size(min = 4, max = 50)
   private String email;

   public RegisterDTO() { }

   public RegisterDTO(@NotNull @Size(min = 1, max = 50) String username, @NotNull @Size(min = 4, max = 100) String password,
                      @NotNull @Size(min = 4, max = 50) String firstname, @NotNull @Size(min = 4, max = 50) String lastname,
                      @NotNull @Size(min = 4, max = 50) String email) {
      this.username = username;
      this.password = password;
      this.firstname = firstname;
      this.lastname = lastname;
      this.email = email;
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

   public String getFirstname() {
      return firstname;
   }

   public void setFirstname(String firstname) {
      this.firstname = firstname;
   }

   public String getLastname() {
      return lastname;
   }

   public void setLastname(String lastname) {
      this.lastname = lastname;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }
}
