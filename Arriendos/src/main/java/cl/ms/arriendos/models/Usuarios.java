package cl.ms.arriendos.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


@Data
public class Usuarios implements Serializable {


    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean enabled;
    private String nombre;
    private String apellido;

    //private List<Role> roles;

    //private List<Otp> otps;

}
