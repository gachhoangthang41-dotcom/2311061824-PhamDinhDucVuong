package vn.edu.crs.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.crs.courseservice.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {


    boolean existsByTenMonHocIgnoreCase(String tenMonHoc);

    
    boolean existsByTenMonHocIgnoreCaseAndIdNot(
            String tenMonHoc,
            Long id
    );

    Page<Course> findByTenMonHocContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );
}
