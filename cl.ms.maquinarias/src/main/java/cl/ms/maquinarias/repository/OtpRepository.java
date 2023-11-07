package cl.ms.maquinarias.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cl.ms.maquinarias.models.Otp;

public interface OtpRepository extends JpaRepository<Otp, Long> {

	@Query(value = "SELECT * FROM otp  WHERE u_id =:idUser ORDER BY fechacreacion DESC LIMIT 1", nativeQuery = true)
	public Otp findLastOtpForUser(@Param("idUser") Long idUser);

	@Query(value = "SELECT * FROM otp INNER JOIN usuarios on usuarios.id = otps_users.u_id  WHERE u_id =:idUser ", nativeQuery = true)
	public List<Otp> listadoOTPporIdUser(@Param("idUser") Long idUser);
}
