package com.test.client.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.test.client.model.Employee;

@Controller
@RequestMapping("")
public class EmployeeMvcController {
	
	@Value("${url.head}")
	private String urlHeader;
	
	@Value("${url.mid}")
	private String mapApi;
	
	@Value("${api}")
	private String getApi;
	
	@GetMapping("/")
	public String showUserList(Model model) {
	    model.addAttribute("user", "hozaifa");
	    return "index";
	}
	
	@RequestMapping(value = "/editEmployee/{id}", method = RequestMethod.GET)
	public String /* ResponseEntity<Employee> */ getEmployeeById(@PathVariable int id, 
			Model model) {

		String url = urlHeader + mapApi + getApi + "/{id}";
		RestTemplate restTemplate = new RestTemplate();

		Employee employee = restTemplate.getForObject(url, Employee.class, id);
//			return new ResponseEntity<Employee>(employee, HttpStatus.OK);

		model.addAttribute("employee", employee);

		return "add-edit-employee";
	}

	@GetMapping(path = "/add")
	public String getTemplateOfEmployee(Model model) {
		
		System.out.println("clecked");
		model.addAttribute("employee", new Employee());
		
		return "add-edit-employee";
	}
	

	@GetMapping(path = "/employee")
	public String getAllEmployees(Model model) throws URISyntaxException {
		
		RestTemplate restTemplate = new RestTemplate();
		String url = urlHeader+mapApi+getApi;
		URI uri = new URI(url);
		ResponseEntity<List<Employee>> responseEntity =
				restTemplate.exchange(uri, 
						HttpMethod.GET,
						null, 
						new ParameterizedTypeReference<List<Employee>>() {});
		
		List<Employee> emps = responseEntity.getBody();
		
		model.addAttribute("employees", emps);
		
		return "list-employees";
		
//		return new ResponseEntity<List<Employee>>(emps, HttpStatus.OK);
	}
	
	@PostMapping(path = "/createEmployee")
	public String /*ResponseEntity<Employee>*/ createNewEmployee(Employee employee,
			Model model) 
			throws URISyntaxException{
		
		RestTemplate restTemplate = new RestTemplate();
		String url = urlHeader+mapApi+getApi;
		URI uri = new URI(url);
		Employee newEmp = restTemplate.postForObject(uri, employee, Employee.class);
//		return new ResponseEntity<Employee>(newEmp, HttpStatus.OK);

		model.addAttribute("employee", newEmp);
		return "redirect:/employee";
	}
	
	@PutMapping(path = "/employee/{id}", headers = "Accept=application/json")
	public ResponseEntity<Employee> updateEmployee(@PathVariable("id") int id, 
			@RequestBody Employee employee) throws URISyntaxException {
		
		RestTemplate restTemplate = new RestTemplate();
		String url = urlHeader+mapApi+getApi+"/"+id;
		
		restTemplate.put(url, employee, id);
		
		//return getEmployeeById(id);
		return null;
	}
	

	@GetMapping(path = "/delete/{id}")
	public String deleteEmployee(@PathVariable("id") int id) 
			throws URISyntaxException {
		
		RestTemplate restTemplate = new RestTemplate();
		String url = urlHeader+mapApi+getApi+"/"+id;
		 
		restTemplate.delete(url, id);
		
//		Employee employee = restTemplate.getForObject(uri, Employee.class);
//		
//		if(employee == null) {
//			String responseContent = "Employee has been deleted successfully";
//			return new ResponseEntity<String>(responseContent,HttpStatus.OK);
//		} else {
//			String error = "Error while deleting Employee from database";
//			return new ResponseEntity<String>(error,HttpStatus.INTERNAL_SERVER_ERROR);
//		}

		return "redirect:/employee";
	}
}
