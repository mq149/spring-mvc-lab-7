# spring-mvc-lab-7
ISD Lab#7: Simple Employee Management RestAPI using Java, Spring Boot with MongoDB

# Table of Contents
- [Homework requirements](#homework-requirements)
- [Result](#result)
  - [Get all](#get-all)
  - [Get by ID](#get-by-id)
  - [Update](#update)
  - [Delete by ID](#delete-by-id)
  - [Delete all](#delete-all)
- [Setup database](#setup-database)
  - [Create database](#create-database)
  - [Import data](#import-data)
- [Files](#files)
  - [application.properties](#applicationproperties)
  - [Model](#model)
  - [Repository](#repository)
  - [Controller](#controller)

# Homework requirements
Sinh viên hãy thực hiện lại bài hướng dẫn trên để hiểu cách hoạt động hệ thống, ứng dụng
xây dựng Web API dùng Java.

Yêu cầu: Sinh viên xây dựng một trang web API đơn giản để quản lý (CRUD) sách bằng
cách sử dụng Spring MVC và Spring Boot.

# Result
## Get all 
GET ```/employees```
<kbd>![!](https://github.com/mq149/spring-mvc-lab-7/blob/master/results/lab7-get.png "Get all employees")</kbd>

## Get by ID
GET ```/employees/{id}```
<kbd>![!](https://github.com/mq149/spring-mvc-lab-7/blob/master/results/lab7-get-id.png "Get one employee")</kbd>

## Update
PUT ```/employees/{id}```
<kbd>![!](https://github.com/mq149/spring-mvc-lab-7/blob/master/results/lab7-put.png "Update one employee")</kbd>

## Delete by ID
DELELE ```/employees/{id}```
<kbd>![!](https://github.com/mq149/spring-mvc-lab-7/blob/master/results/lab7-delete.png "")</kbd>
Check deleted employee:
<kbd>![!](https://github.com/mq149/spring-mvc-lab-7/blob/master/results/lab7-delete-check.png "")</kbd>

## Delete all
DELETE ```/employeeall```
<kbd>![!](https://github.com/mq149/spring-mvc-lab-7/blob/master/results/lab7-delete-all.png "")</kbd>
Get all employees:
<kbd>![!](https://github.com/mq149/spring-mvc-lab-7/blob/master/results/lab7-delete-all-check.png "")</kbd>
Check employees document in database:
<kbd>![!](https://github.com/mq149/spring-mvc-lab-7/blob/master/results/lab7-delete-all-check-db.png "")</kbd>

# Setup database
## Create database
Create a database named "isd-lab" with a data collection "employees":
<kbd>![!](https://github.com/mq149/spring-mvc-lab-7/blob/master/results/lab7-db-create.png "Create database")</kbd>

## Import data
Import data from json with 4 attributes: id (mã nhân viên), name (tên nhân viên), year_of_experience (số năm kinh nghiệm) và probationary (đang thử việc hoặc không):
<kbd>![!](https://github.com/mq149/spring-mvc-lab-7/blob/master/results/lab7-db-add-data.png "Import data from JSON")</kbd>

# Files
## application.properties
```
spring.data.mongodb.database=isd-lab
spring.data.mongodb.port=27017
```

## Model
Package: springmvclab7.model, ```Employee.java```
```
@Document(collection = "employees")
public class Employee {
	
	@Id
	private String id;
	private String name;
	private int year_of_experience;
	private boolean probationary;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getYOE() {
		return year_of_experience;
	}
	
	public void setYOE(int year_of_experience) {
		this.year_of_experience = year_of_experience;
	}
	
	public boolean isProbationary() {
		return probationary;
	}
	
	public void setProbationary(boolean probationary) {
		this.probationary = probationary;
	}
	
	public Employee(String id, String name, int year_of_experience, boolean probationary) {
		super();
		this.id = id;
		this.name = name; 
		this.year_of_experience = year_of_experience;
		this.probationary = probationary;
	}
	
	public Employee() {
		super();
	}
}
```

## Repository
Package: springmvclab7.repository, ```EmployeeRepository.java```
```
import org.springframework.data.mongodb.repository.MongoRepository;
import springmvclab7.model.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

}
```

## Controller
Package: springmvclab7.controller, ```EmployeeController.java```
```
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
```

