package springmvclab7.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import springmvclab7.model.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

}
