package cl.ms.maquinarias.response;

import lombok.Data;

@Data
public class ResponseData<T> {

    GenericResponse response;
    T data;
    
}
