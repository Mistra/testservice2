package net.endu.enduscan.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import net.endu.enduscan.model.IntermediatePoint;
import net.endu.enduscan.model.Location;

public class CustomIntermediatePointRepositoryImpl
        implements CustomIntermediatePointRepository<IntermediatePoint> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public <S extends IntermediatePoint> S save(S intermediatePoint) {

        Optional<Location> location =
                locationRepository.findById(intermediatePoint.getLocationId());
        String locationName =
                location.map(Location::getName).map(this::transformOldNameIntoNew).orElse("");
        intermediatePoint.setField(locationName);

        if (intermediatePoint.getId() == null) {
            entityManager.persist(intermediatePoint);
        } else {
            entityManager.merge(intermediatePoint);
        }

        return intermediatePoint;
    }

    @Override
    @Transactional
    public <S extends IntermediatePoint> Iterable<S> saveAll(Iterable<S> intermediatePoints) {
        intermediatePoints.forEach(this::save);
        return intermediatePoints;
    }

    @Override
    public List<IntermediatePoint> findByLocationId(Long locationId) {
        List<IntermediatePoint> intermediatePointList = entityManager.createQuery(
                "SELECT ip FROM Path p INNER JOIN IntermediatePoint ip ON p.id = ip.pathId INNER JOIN Location l ON p.editionId = l.editionId WHERE l.id = :locationId",
                IntermediatePoint.class).setParameter("locationId", locationId).getResultList();

        return locationRepository
                .findById(locationId).map(
                        location -> intermediatePointList.stream()
                                .filter(point -> point.getField()
                                        .equals(transformOldNameIntoNew(location.getName())))
                                .map(ip -> {
                                    ip.setLocationId(location.getId());
                                    return ip;
                                }).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

    @Override
    public List<IntermediatePoint> findByPathId(Long pathId) {
        List<IntermediatePoint> intermediatePointList = entityManager
                .createQuery("SELECT ip FROM IntermediatePoint ip WHERE ip.pathId = :pathId",
                        IntermediatePoint.class)
                .setParameter("pathId", pathId).getResultList();


        List<Location> locationList = entityManager.createQuery(
                "SELECT l FROM Location l INNER JOIN Path p ON l.editionId = p.editionId WHERE p.id = :pathId",
                Location.class).setParameter("pathId", pathId).getResultList();

        return intermediatePointList.stream()
                .map(point -> assignLocationToPoint(point, locationList))
                .collect(Collectors.toList());
    }

    @Override
    public List<IntermediatePoint> findByEditionId(Long editionId) {
        List<IntermediatePoint> intermediatePointList = entityManager.createQuery(
                "SELECT ip FROM IntermediatePoint ip INNER JOIN Path p ON ip.pathId = p.id WHERE p.editionId = :editionId",
                IntermediatePoint.class).setParameter("editionId", editionId).getResultList();

        List<Location> locationList =
                entityManager
                        .createQuery("SELECT l FROM Location l WHERE l.editionId = :editionId",
                                Location.class)
                        .setParameter("editionId", editionId).getResultList();

        return intermediatePointList.stream()
                .map(point -> assignLocationToPoint(point, locationList))
                .collect(Collectors.toList());
    }

    private IntermediatePoint assignLocationToPoint(IntermediatePoint point,
            List<Location> locationList) {
        locationList.stream().filter(
                location -> transformOldNameIntoNew(location.getName()).equals(point.getField()))
                .findFirst().ifPresent(location -> point.setLocationId(location.getId()));
        return point;
    }

    private String transformOldNameIntoNew(String name) {
        name = name.toUpperCase();
        name = name.equals("ORAPARTENZA") ? "START" : name;
        name = name.equals("ORAARRIVO") ? "FINISH" : name;
        name = name.startsWith("ORAIN") ? name.replace("ORAIN", "SPLIT") : name;
        return name;
    }
}
