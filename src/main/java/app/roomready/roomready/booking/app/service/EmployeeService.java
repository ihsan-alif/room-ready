package app.roomready.roomready.booking.app.service;

import app.roomready.roomready.booking.app.dto.request.SearchEmployeeRequest;
import app.roomready.roomready.booking.app.dto.request.UpdateEmployeeRequest;
import app.roomready.roomready.booking.app.dto.response.EmployeeResponse;
import app.roomready.roomready.booking.app.dto.response.UploadResponse;
import app.roomready.roomready.booking.app.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EmployeeService {

    void create(Employee employee);

    EmployeeResponse getById(String id);

    Employee get(String id);

    EmployeeResponse update(UpdateEmployeeRequest request);

    void delete(String id);

    Page<EmployeeResponse> search(SearchEmployeeRequest request);

    UploadResponse uploadAvatar(MultipartFile avatarFileName, String id) throws IOException;
}
