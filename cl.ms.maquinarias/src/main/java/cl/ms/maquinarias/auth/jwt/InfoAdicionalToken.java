package cl.ms.maquinarias.auth.jwt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;


import cl.ms.maquinarias.models.Usuarios;
import cl.ms.maquinarias.response.ResponseData;
import cl.ms.maquinarias.service.UsuarioService;

@Component
public class InfoAdicionalToken implements TokenEnhancer {
	
	@Autowired
	UsuarioService usuarioService;



    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    	ResponseData<Usuarios> user = (ResponseData<Usuarios>) usuarioService.buscarUsuarioPorUsername(authentication.getName());
    	Map<String, Object> info = new HashMap<>();
    	Usuarios usuario = user.getData();
         
    	info.put("info_adicional","Hola ".concat(authentication.getName()));
    	info.put("id", usuario.getId());
		info.put("nombre",usuario.getNombre());
    	info.put("apellido",usuario.getApellido());
		info.put("email",usuario.getEmail());
		info.put("username", usuario.getUsername());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		return accessToken;
    	
    }
    
}
