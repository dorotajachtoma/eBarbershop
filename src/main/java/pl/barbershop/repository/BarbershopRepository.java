package pl.barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.barbershop.model.Barbershop;
import javax.transaction.Transactional;
import java.util.Set;

@Transactional
public interface BarbershopRepository extends JpaRepository<Barbershop,Long> {


    Set<Barbershop> findByCity(String city);

    Barbershop findByEmail(String email);


}
