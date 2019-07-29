package pl.barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.barbershop.model.Barbershop;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface BarbershopRepository extends JpaRepository<Barbershop,Long> {

    Barbershop findByNip(String nip);

    List<Barbershop> findByCity(String city);
}
