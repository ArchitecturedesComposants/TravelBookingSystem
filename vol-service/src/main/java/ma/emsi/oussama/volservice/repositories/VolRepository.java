package ma.emsi.oussama.volservice.repositories;

import ma.emsi.oussama.volservice.entities.Vol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface VolRepository extends JpaRepository<Vol, Long> {
    // Méthode de recherche personnalisée demandée dans le diagramme
    List<Vol> findByAeroportDepart(String ville);
}
