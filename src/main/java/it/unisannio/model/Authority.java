package it.unisannio.model;

import it.unisannio.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table
public class Authority {

   @Id
   @Column(length = 50)
   @NotNull
   @Enumerated(EnumType.STRING)
   private Role name;

   public Authority() { }

   public Authority(@NotNull Role name) {
      this.name = name;
   }

   public Role getName() {
      return name;
   }

   public void setName(Role name) {
      this.name = name;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Authority authority = (Authority) o;
      return name == authority.name;
   }

   @Override
   public int hashCode() {
      return Objects.hash(name);
   }

   @Override
   public String toString() {
      return "Authority{" +
         "name=" + name +
         '}';
   }
}
