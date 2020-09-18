package net.endu.enduscan.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import net.endu.enduscan.model.IntermediatePoint;
import net.endu.enduscan.model.Location;
import net.endu.enduscan.model.Operator;

@SpringBootTest
class LocationServiceTests {

	@Autowired
	LocationService locationService;

	@Autowired
	OperatorService operatorService;

	@Autowired
	IntermediatePointService pointService;

	@AfterEach
	void clear() {
		locationService.deleteAll();
		pointService.deleteAll();
		operatorService.deleteAll();
	}

	@Test
	void createLocation() {
		Location location1 = new Location();
		location1.setEditionId(Long.valueOf(1000));
		location1.setName("SPLIT");
		location1 = locationService.create(location1);

		Location location2 = new Location();
		location2.setEditionId(Long.valueOf(1000));
		location2.setName("SPLIT");
		location2 = locationService.create(location2);
		
		assertEquals("SPLIT1", location1.getName());
		assertEquals("SPLIT2", location2.getName());
	}

	@Test
	void updateLocation() {
		Location location = new Location();
		location.setEditionId(Long.valueOf(1000));
		location.setName("SPLIT");
		location = locationService.create(location);

		// SPLIT1 to SPLIT = SPLIT1
		location.setName("SPLIT");
		location = locationService.update(location, location.getId());
		
		assertEquals("SPLIT1", location.getName());

		// SPLIT1 to FINISH = FINISH
		location.setName("FINISH");
		location = locationService.update(location, location.getId());
		
		assertEquals("FINISH", location.getName());

		// FINISH to FINISH = FINISH
		location.setName("FINISH");
		location = locationService.update(location, location.getId());
		
		assertEquals("FINISH", location.getName());

		// FINISH to SPLIT = SPLIT1
		location.setName("SPLIT");
		location = locationService.update(location, location.getId());
		
		assertEquals("SPLIT1", location.getName());
	}

	@Test
	void deleteByIdWithIntermediatePoints() {
		// Setup
		// One location with two bound points
		Location location = new Location();
		location.setEditionId(Long.valueOf(1000));
		location.setName("SPLIT");
		location = locationService.create(location);

		IntermediatePoint point1 = new IntermediatePoint();
		point1.setLocationId(location.getId());

		IntermediatePoint point2 = new IntermediatePoint();
		point2.setLocationId(location.getId());

		Long id1 = pointService.create(point1).getId();
		Long id2 = pointService.create(point2).getId();

		// Tests
		assertEquals(1, locationService.findAll().size());
		assertEquals(2, pointService.findAll().size());

		locationService.deleteById(location.getId());

		assertEquals(0, pointService.findById(id1).get().getLocationId());
		assertEquals(0, pointService.findById(id2).get().getLocationId());
		assertEquals(0, locationService.findAll().size());
	}

	@Test
	void deleteByIdWithOperators() {
		Location location1 = new Location();
		location1.setEditionId(Long.valueOf(1000));
		location1.setName("SPLIT");
		
		Location location2 = new Location();
		location2.setEditionId(Long.valueOf(1000));
		location2.setName("SPLIT");

		Location location3 = new Location();
		location3.setEditionId(Long.valueOf(1000));
		location3.setName("SPLIT");

		locationService.create(location1);
		locationService.create(location2);
		locationService.create(location3);

		Operator operator1 = new Operator();
		operator1.setEditionId(Long.valueOf(1000));
		operator1.setName("John");
		operator1.setEmail("johnny@gmail.com");
		operator1.setMobileNumber("3463333333");
		operator1 = operatorService.create(operator1);

		Operator operator2 = new Operator();
		operator2.setEditionId(Long.valueOf(1000));
		operator2.setName("Jason");
		operator2.setEmail("jason@email.it");
		operator2.setMobileNumber("3463333333");
		operator2 = operatorService.create(operator2);

		locationService.assignToOperatorById(location1.getId(), operator1.getId());
		locationService.assignToOperatorById(location2.getId(), operator1.getId());
		locationService.assignToOperatorById(location3.getId(), operator2.getId());

		locationService.deleteById(location1.getId());

		List<Location> locationList = locationService.findByOperatorId(operator1.getId());

		assertEquals(1, locationList.size());
	}

	@Test
	void deleteAll() {
		// Setup
		// One location with two bound points
		Location location1 = new Location();
		location1.setEditionId(Long.valueOf(1000));
		location1.setName("SPLIT");
		
		Location location2 = new Location();
		location2.setEditionId(Long.valueOf(1000));
		location2.setName("SPLIT");

		locationService.create(location1);
		locationService.create(location2);

		IntermediatePoint point1 = new IntermediatePoint();
		point1.setLocationId(location1.getId());
		pointService.create(point1);

		IntermediatePoint point2 = new IntermediatePoint();
		point2.setLocationId(location1.getId());
		pointService.create(point2);

		IntermediatePoint point3 = new IntermediatePoint();
		point3.setLocationId(location2.getId());
		pointService.create(point3);

		// Tests
		assertEquals(2, locationService.findAll().size());
		assertEquals(3, pointService.findAll().size());

		// locationService.deleteAll();
		locationService.deleteById(location1.getId());
		locationService.deleteById(location2.getId());

		assertEquals(0, locationService.findAll().size());
		Long sumOfLocationIds = pointService.findAll().stream()
				.map(IntermediatePoint::getLocationId).reduce(Long.valueOf(0), Long::sum);
		assertEquals(0, sumOfLocationIds);
	}

	@Test
	void findByOperatorId() {
		Location location1 = new Location();
		location1.setEditionId(Long.valueOf(1000));
		location1.setName("SPLIT");
		
		Location location2 = new Location();
		location2.setEditionId(Long.valueOf(1000));
		location2.setName("SPLIT");

		Location location3 = new Location();
		location3.setEditionId(Long.valueOf(1000));
		location3.setName("SPLIT");

		locationService.create(location1);
		locationService.create(location2);
		locationService.create(location3);

		Operator operator1 = new Operator();
		operator1.setEditionId(Long.valueOf(1000));
		operator1.setName("John");
		operator1.setEmail("johnny@gmail.com");
		operator1.setMobileNumber("3463333333");
		operator1 = operatorService.create(operator1);

		Operator operator2 = new Operator();
		operator2.setEditionId(Long.valueOf(1000));
		operator2.setName("Jason");
		operator2.setEmail("jason@email.it");
		operator2.setMobileNumber("3463333333");
		operator2 = operatorService.create(operator2);

		locationService.assignToOperatorById(location1.getId(), operator1.getId());
		locationService.assignToOperatorById(location2.getId(), operator1.getId());
		locationService.assignToOperatorById(location3.getId(), operator2.getId());

		List<Location> locationList = locationService.findByOperatorId(operator1.getId());

		assertEquals(2, locationList.size());
		assertEquals(location1.getId(), locationList.get(0).getId());
		assertEquals(location2.getId(), locationList.get(1).getId());
	}
}
