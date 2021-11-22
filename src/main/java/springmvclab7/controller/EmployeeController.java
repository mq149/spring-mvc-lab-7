package springmvclab7.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import springmvclab7.model.Employee;
import springmvclab7.repository.EmployeeRepository;

@RestController
@RequestMapping("/api")
public class EmployeeController {

	@Autowired
	EmployeeRepository repo;
	
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> GetEmployees() {
		try {
			List<Employee> emps = new ArrayList<Employee>();
			repo.findAll().forEach(emps::add);
			if (emps.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(emps, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> GetEmployee(@PathVariable("id") String id) {
		Optional<Employee> emp = repo.findById(id);
		if (emp.isPresent()) {
			return new ResponseEntity<>(emp.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/employees")
	public ResponseEntity<Employee> CreateEmployee(@RequestBody Employee employee) {
		try {
			Employee emp = new Employee(
					employee.getId(),
					employee.getName(),
					employee.getYOE(),
					employee.isProbationary()
					);
			Employee _emp = repo.save(emp);
			return new ResponseEntity<>(_emp, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> UpdateEmployee(@PathVariable("id") String id, @RequestBody Employee employee) {
		Optional<Employee> empData = repo.findById(id);
		if (empData.isPresent()) {
			Employee _emp = empData.get();
			_emp.setName(employee.getName());
			_emp.setYOE(employee.getYOE());
			_emp.setProbationary(employee.isProbationary());
			return new ResponseEntity<>(repo.save(_emp), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Employee> DeleteEmployee(@PathVariable("id") String id) {
		try {
			repo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/employeeall")
	public ResponseEntity<Employee> DeleteAllEmployee() {
		try {
			repo.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
