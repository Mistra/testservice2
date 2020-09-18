package net.endu.enduscan.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import net.endu.enduscan.model.IntermediatePoint;
import net.endu.enduscan.model.Location;
import net.endu.enduscan.model.Path;

@SpringBootTest
class IntermediatePointServiceTests {

	@Autowired
	PathService pathService;

	@Autowired
	IntermediatePointService pointService;

	@Autowired
	LocationService locationService;

	@AfterEach
	public void clear() {
		pathService.deleteAll();
		pointService.deleteAll();
		locationService.deleteAll();
	}

	@Test
	void findByPathId() {
		// Setup
		Path path = new Path();
		path.setEditionId(Long.valueOf(1000));
		path.setName("Pathname");
		path = pathService.create(path);

		IntermediatePoint point1 = new IntermediatePoint();
		point1.setPathId(path.getId());
		pointService.create(point1);

		IntermediatePoint point2 = new IntermediatePoint();
		point2.setPathId(path.getId());
		pointService.create(point2);

		// Test
		assertEquals(2, pointService.findByPathId(path.getId()).size());
	}

	@Test
	void findByLocationId() {
		// Setup
		Path path = new Path();
		path.setEditionId(Long.valueOf(1000));
		path.setName("Pathname");
		pathService.create(path);

		Location location = new Location();
		location.setName("SPLIT1");
		location.setEditionId(Long.valueOf(1000));
		location = locationService.create(location);

		IntermediatePoint point1 = new IntermediatePoint();
		point1.setLocationId(location.getId());
		point1.setPathId(path.getId());
		pointService.create(point1);

		IntermediatePoint point2 = new IntermediatePoint();
		point2.setLocationId(location.getId());
		point2.setPathId(path.getId());
		pointService.create(point2);

		// Test
		assertEquals("SPLIT1", point1.getField());
		assertEquals(2, pointService.findByLocationId(location.getId()).size());
	}

	@Test
	void findByLocationIdWithLocationWithOldName() {
		// Setup
		Path path = new Path();
		path.setEditionId(Long.valueOf(1000));
		path.setName("Pathname");
		pathService.create(path);

		Location location = new Location();
		location.setName("ORAIN1");
		location.setEditionId(Long.valueOf(1000));
		location = locationService.create(location);

		IntermediatePoint point1 = new IntermediatePoint();
		point1.setLocationId(location.getId());
		point1.setPathId(path.getId());
		pointService.create(point1);

		IntermediatePoint point2 = new IntermediatePoint();
		point2.setLocationId(location.getId());
		point2.setPathId(path.getId());
		pointService.create(point2);

		// Test
		assertEquals("SPLIT1", point1.getField());
		assertEquals(location.getId(), point1.getLocationId());
		assertEquals("SPLIT1", point2.getField());
		assertEquals(location.getId(), point2.getLocationId());
		assertEquals(2, pointService.findByLocationId(location.getId()).size());
	}

	@Test
	void saveWithLocationOldName() {
		Path path = new Path();
		path.setEditionId(Long.valueOf(1000));
		path.setName("Pathname");
		pathService.create(path);

		Location location1 = new Location();
		location1.setName("ORAPARTENZA");
		location1.setEditionId(Long.valueOf(1000));
		location1 = locationService.create(location1);

		IntermediatePoint point1 = new IntermediatePoint();
		point1.setLocationId(location1.getId());
		point1.setPathId(path.getId());
		pointService.create(point1);

		Location location2 = new Location();
		location2.setName("ORAIN1");
		location2.setEditionId(Long.valueOf(1000));
		location2 = locationService.create(location2);

		IntermediatePoint point2 = new IntermediatePoint();
		point2.setLocationId(location2.getId());
		point2.setPathId(path.getId());
		pointService.create(point2);

		Location location3 = new Location();
		location3.setName("ORAIN22");
		location3.setEditionId(Long.valueOf(1000));
		location3 = locationService.create(location3);

		IntermediatePoint point3 = new IntermediatePoint();
		point3.setLocationId(location3.getId());
		point3.setPathId(path.getId());
		pointService.create(point3);

		Location location4 = new Location();
		location4.setName("ORAARRIVO");
		location4.setEditionId(Long.valueOf(1000));
		location4 = locationService.create(location4);

		IntermediatePoint point4 = new IntermediatePoint();
		point4.setLocationId(location4.getId());
		point4.setPathId(path.getId());
		pointService.create(point4);

		assertEquals("START", point1.getField());
		assertEquals("SPLIT1", point2.getField());
		assertEquals("SPLIT22", point3.getField());
		assertEquals("FINISH", point4.getField());
	}

	@Test
	void saveAll() {
		// Setup
		IntermediatePoint point1 = pointService.create(new IntermediatePoint());
		point1.setName("test1");
		Long id1 = point1.getId();
		pointService.update(point1, point1.getId());

		IntermediatePoint point2 = pointService.create(new IntermediatePoint());
		point2.setName("test2");
		Long id2 = point2.getId();
		pointService.update(point2, point2.getId());
		point2.setName("test2.1");

		IntermediatePoint point3 = new IntermediatePoint();
		point3.setName("test3");

		List<IntermediatePoint> pointList = pointService.saveAll(Stream.of(point2, point3).collect(Collectors.toList()));
		Long id3 = pointList.get(1).getId();

		// Tests
		assertEquals("test1", pointService.findById(id1).get().getName());
		assertEquals("test2.1", pointService.findById(id2).get().getName());
		assertEquals("test3", pointService.findById(id3).get().getName());
		assertEquals(3, pointService.findAll().size());
	}

	@Test
	void assignPointToDifferentLocation() {

		Location location1 = new Location();
		location1.setEditionId(Long.valueOf(1000));
		location1.setName("SPLIT1");

		Location location2 = new Location();
		location2.setEditionId(Long.valueOf(1000));
		location2.setName("SPLIT2");

		locationService.create(location1);
		locationService.create(location2);		

		IntermediatePoint point = new IntermediatePoint();
		point.setLocationId(location1.getId());
		pointService.create(point);

		assertEquals("SPLIT1", point.getField());
		assertEquals(location1.getId(), point.getLocationId());

		point.setLocationId(location2.getId());
		pointService.update(point, point.getId());

		assertEquals("SPLIT2", point.getField());
	}

}
