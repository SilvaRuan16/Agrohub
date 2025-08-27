package br.com.agrohub.demo.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
   
public class LoginModel implements Serializable {
   private Long id;
   private String userAccess;
   private String password;
}
