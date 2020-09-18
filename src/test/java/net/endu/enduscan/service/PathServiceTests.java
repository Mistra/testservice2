package net.endu.enduscan.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import net.endu.enduscan.model.IntermediatePoint;
import net.endu.enduscan.model.Path;

@SpringBootTest
class PathServiceTests {

	@Autowired
	PathService pathService;

	@Autowired
	IntermediatePointService pointService;

	@AfterEach
	void clear() {
		pathService.deleteAll();
		pointService.deleteAll();
	}

	@Test
	void deleteById() {
		// Setup
		// One path with two bound points
		Path path = new Path();
		path.setEditionId(Long.valueOf(1000));
		path.setName("Pathname");
		path = pathService.create(path);

		IntermediatePoint point1 = new IntermediatePoint();
		point1.setPathId(path.getId());

		IntermediatePoint point2 = new IntermediatePoint();
		point2.setPathId(path.getId());

		pointService.create(point1);
		pointService.create(point2);

		// Tests
		assertEquals(1, pathService.findAll().size());
		assertEquals(2, pointService.findAll().size());

		pathService.deleteById(path.getId());

		assertEquals(0, pathService.findAll().size());
		assertEquals(0, pointService.findAll().size());
	}

}
