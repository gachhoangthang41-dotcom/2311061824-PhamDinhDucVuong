package vn.edu.crs.registrationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.crs.registrationservice.entity.Registration;

import java.util.List;

public interface RegistrationRepository
        extends JpaRepository<Registration, Long> {

    /**
     * Lấy danh sách đăng ký của sinh viên.
     */
    List<Registration> findByStudentId(
            Long studentId
    );

    /**
     * Kiểm tra sinh viên đã đăng ký môn này chưa.
     */
    boolean existsByStudentIdAndCourseIdAndTrangThai(
            Long studentId,
            Long courseId,
            String trangThai
    );
}