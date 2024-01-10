package app.roomready.roomready.booking.app.service.impl;

import app.roomready.roomready.booking.app.dto.request.SearchEmployeeRequest;
import app.roomready.roomready.booking.app.dto.request.UpdateEmployeeRequest;
import app.roomready.roomready.booking.app.dto.response.EmployeeResponse;
import app.roomready.roomready.booking.app.dto.response.UploadResponse;
import app.roomready.roomready.booking.app.entity.Employee;
import app.roomready.roomready.booking.app.entity.UserCredential;
import app.roomready.roomready.booking.app.repository.EmployeeRepository;
import app.roomready.roomready.booking.app.service.EmployeeService;
import app.roomready.roomready.booking.app.service.UserService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    private final UserService userService;

    private final List<String> contentTypes = List.of("image/jpeg", "image/png");

    private final Path directoryPath;

    public EmployeeServiceImpl(EmployeeRepository repository, UserService userService, @Value("${app.room-ready.directory-image-path}") String directoryPath) {
        this.repository = repository;
        this.userService = userService;
        this.directoryPath = Paths.get(directoryPath);
    }

    private static final String ROLE = "ROLE_ADMIN";

    private static final String FORBIDDEN = "FORBIDDEN";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Employee employee) {
        repository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getById(String id) {

        Employee employee = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found")
        );

        UserCredential currentUser = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCredential userCredential = employee.getUserCredential();

        if (!currentUser.getId().equals(userCredential.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals(ROLE)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);

        return toEmployeeResponse(employee);
    }


    @Override
    public Employee get(String id) {
        Employee employee = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found")
        );

        UserCredential currentUser = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCredential userCredential = employee.getUserCredential();

        if (!currentUser.getId().equals(userCredential.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals(ROLE)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);

        return employee;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeResponse update(UpdateEmployeeRequest request) {
        Employee employee = repository.findById(request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND")
        );

        UserCredential currentUser = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCredential userCredential = employee.getUserCredential();

        if (!currentUser.getId().equals(userCredential.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals(ROLE)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);

        employee.setName(request.getName());
        employee.setDivision(request.getDivision());
        employee.setPosition(request.getPosition());
        employee.setContactInfo(request.getContactInfo());

        repository.save(employee);

        return toEmployeeResponse(employee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        Employee employee = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        UserCredential currentUser = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCredential userCredential = employee.getUserCredential();

        if (!currentUser.getId().equals(userCredential.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals(ROLE)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);

        userService.delete(userCredential.getId());
        repository.delete(employee);
    }

    @Override
    public Page<EmployeeResponse> search(SearchEmployeeRequest request) {
        Specification<Employee> specification = getEmployeeSpecification(request);

        if (request.getPage() <= 0) request.setPage(1);

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<Employee> all = repository.findAll(specification, pageable);

        List<EmployeeResponse> responses = new ArrayList<>();

        for (Employee employee : all){
            EmployeeResponse response = toEmployeeResponse(employee);

            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, all.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UploadResponse uploadAvatar(MultipartFile avatarFileName, String id) throws IOException {

        saveValidation(avatarFileName);

        Employee employee = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        UserCredential currentUser = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCredential userCredential = employee.getUserCredential();

        if (!currentUser.getId().equals(userCredential.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals(ROLE)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);

        String filename = String.format("%d_%s", System.currentTimeMillis(), avatarFileName.getOriginalFilename());

        Path filePath = directoryPath.resolve(filename);

        Files.copy(avatarFileName.getInputStream(), filePath);

        employee.setAvatarFileName(filename);

        repository.save(employee);

        return UploadResponse.builder()
                .avatarFileName(employee.getAvatarFileName())
                .build();
    }

    private static Specification<Employee> getEmployeeSpecification(SearchEmployeeRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"), "%"+ request.getName()+"%"));
            }

            if (Objects.nonNull(request.getDivision())){
                predicates.add(criteriaBuilder.like(root.get("division"), "%"+ request.getDivision()+"%"));
            }

            if (Objects.nonNull(request.getPosition())){
                predicates.add(criteriaBuilder.like(root.get("position"), "%"+ request.getPosition()+"%"));
            }

            if (Objects.nonNull(request.getContactInfo())){
                predicates.add(criteriaBuilder.like(root.get("contactInfo"), "%"+ request.getContactInfo()+"%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }

    private void saveValidation(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "image is required");
        if (!contentTypes.contains(multipartFile.getContentType()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid content type");
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
    }


    private EmployeeResponse toEmployeeResponse(Employee employee){
        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .division(employee.getDivision())
                .position(employee.getPosition())
                .contactInfo(employee.getContactInfo())
                .username(employee.getUserCredential().getUsername())
                .build();
    }
}
