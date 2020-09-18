package net.endu.enduscan.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import net.endu.enduscan.exception.UnboundEntityException;
import net.endu.enduscan.model.OperatorAssignment;
import net.endu.enduscan.model.Location;
import net.endu.enduscan.model.Operator;
import net.endu.enduscan.repository.OperatorAssignmentRepository;

@SpringBootTest
class OperatorServiceTests {

	@Autowired
	LocationService locationService;

	@Autowired
	OperatorService operatorService;

	@Autowired
	OperatorAssignmentRepository assignmentRepository;

	@AfterEach
	void clear() {
		locationService.deleteAll();
		operatorService.deleteAll();
	}

	@Test
	void assignThrowsOnUnboundEntities() {
		Long zero = Long.valueOf(0);
		Long one = Long.valueOf(1);

		Exception unboundOperatorException = assertThrows(UnboundEntityException.class, () -> {
			operatorService.assignToLocationById(zero, one);
		});

		assertEquals("Cannot operate assignements over an unbound operator",
				unboundOperatorException.getMessage());

		unboundOperatorException = assertThrows(UnboundEntityException.class, () -> {
			operatorService.assignToLocationById(one, zero);
		});

		assertEquals("Cannot operate assignements over an unbound location",
				unboundOperatorException.getMessage());
	}

	@Test
	void assignToLocationById() {
		Location location1 = new Location();
		location1.setEditionId(Long.valueOf(1000));
		location1.setName("SPLIT1");
		
		Location location2 = new Location();
		location2.setEditionId(Long.valueOf(1000));
		location2.setName("SPLIT2");

		locationService.create(location1);
		locationService.create(location2);

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

		operatorService.assignToLocationById(operator1.getId(), location1.getId());
		operatorService.assignToLocationById(operator1.getId(), location2.getId());
		operatorService.assignToLocationById(operator2.getId(), location1.getId());

		List<OperatorAssignment> assignments =
				StreamSupport.stream(assignmentRepository.findAll().spliterator(), false)
						.collect(Collectors.toList());

		assertEquals(3, assignments.size());
	}

	@Test
	void unassignToLocationById() {
		Location location1 = new Location();
		location1.setEditionId(Long.valueOf(1000));
		location1.setName("SPLIT1");
		
		Location location2 = new Location();
		location2.setEditionId(Long.valueOf(1000));
		location2.setName("SPLIT2");

		locationService.create(location1);
		locationService.create(location2);

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

		operatorService.assignToLocationById(operator1.getId(), location1.getId());
		operatorService.assignToLocationById(operator1.getId(), location2.getId());
		operatorService.assignToLocationById(operator2.getId(), location1.getId());

		operatorService.unassignLocationById(operator1.getId(), location1.getId());

		List<OperatorAssignment> assignments =
				StreamSupport.stream(assignmentRepository.findAll().spliterator(), false)
						.collect(Collectors.toList());

		assertEquals(2, assignments.size());
	}

	@Test
	void findByLocationId() {
		Location location1 = new Location();
		location1.setEditionId(Long.valueOf(1000));
		location1.setName("SPLIT1");
		
		Location location2 = new Location();
		location2.setEditionId(Long.valueOf(1000));
		location2.setName("SPLIT2");

		locationService.create(location1);
		locationService.create(location2);

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

		Operator operator3 = new Operator();
		operator3.setEditionId(Long.valueOf(1000));
		operator3.setName("Jimmy");
		operator3.setEmail("jimmy@email.it");
		operator3.setMobileNumber("3463333333");
		operator3 = operatorService.create(operator3);

		operatorService.assignToLocationById(operator1.getId(), location1.getId());
		operatorService.assignToLocationById(operator2.getId(), location1.getId());
		operatorService.assignToLocationById(operator3.getId(), location2.getId());

		List<Operator> operatorList = operatorService.findByLocationId(location1.getId());

		assertEquals(2, operatorList.size());
		assertEquals(operator1.getId(), operatorList.get(0).getId());
		assertEquals(operator2.getId(), operatorList.get(1).getId());
	}

	@Test
	void delete() {
		Location location1 = new Location();
		location1.setEditionId(Long.valueOf(1000));
		location1.setName("SPLIT1");
		
		Location location2 = new Location();
		location2.setEditionId(Long.valueOf(1000));
		location2.setName("SPLIT2");

		locationService.create(location1);
		locationService.create(location2);

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

		Operator operator3 = new Operator();
		operator3.setEditionId(Long.valueOf(1000));
		operator3.setName("Jimmy");
		operator3.setEmail("jimmy@email.it");
		operator3.setMobileNumber("3463333333");
		operator3 = operatorService.create(operator3);

		operatorService.assignToLocationById(operator1.getId(), location1.getId());
		operatorService.assignToLocationById(operator2.getId(), location1.getId());
		operatorService.assignToLocationById(operator3.getId(), location2.getId());

		operatorService.deleteById(operator1.getId());

		List<Operator> operatorList = operatorService.findByLocationId(location1.getId());

		assertEquals(1, operatorList.size());
	}

}
