package vn.edu.crs.courseservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.crs.courseservice.dto.CourseDTO;
import vn.edu.crs.courseservice.entity.Course;
import vn.edu.crs.courseservice.repository.CourseRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    /**
     * Lấy danh sách toàn bộ môn học.
     */
    public List<CourseDTO> getAll() {
        return courseRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Lấy chi tiết một môn học theo id.
     */
    public CourseDTO getById(Long id) {
        Course course = findCourseById(id);

        return toDTO(course);
    }

    /**
     * Tạo môn học mới.
     */
    @Transactional
    public CourseDTO create(CourseDTO dto) {
        String tenMonHoc = normalizeName(dto.getTenMonHoc());

        if (courseRepository.existsByTenMonHocIgnoreCase(tenMonHoc)) {
            throw new IllegalArgumentException(
                    "Tên môn học đã tồn tại"
            );
        }

        Course course = new Course();

        course.setTenMonHoc(tenMonHoc);
        course.setSoTinChi(dto.getSoTinChi());
        course.setSoChoToiDa(dto.getSoChoToiDa());

        /*
         * Quy tắc nghiệp vụ:
         * Khi môn học vừa được tạo, chưa có sinh viên đăng ký.
         * Vì vậy số chỗ còn lại bằng số chỗ tối đa.
         */
        course.setSoChoConLai(dto.getSoChoToiDa());

        Course savedCourse = courseRepository.save(course);

        return toDTO(savedCourse);
    }

    /**
     * Cập nhật thông tin môn học.
     */
    @Transactional
    public CourseDTO update(Long id, CourseDTO dto) {
        Course course = findCourseById(id);

        String tenMonHoc = normalizeName(dto.getTenMonHoc());

        /*
         * Không cho đổi thành tên của môn học khác.
         * Bỏ qua chính id đang cập nhật.
         */
        boolean duplicateName =
                courseRepository.existsByTenMonHocIgnoreCaseAndIdNot(
                        tenMonHoc,
                        id
                );

        if (duplicateName) {
            throw new IllegalArgumentException(
                    "Tên môn học đã tồn tại"
            );
        }

        /*
         * Tính số sinh viên đã đăng ký.
         *
         * Ví dụ:
         * - Số chỗ tối đa hiện tại: 40
         * - Số chỗ còn lại: 30
         * - Số chỗ đã đăng ký: 10
         */
        int soChoDaDangKy =
                course.getSoChoToiDa() - course.getSoChoConLai();

        /*
         * Không được giảm số chỗ tối đa xuống thấp hơn
         * số sinh viên đã đăng ký.
         */
        if (dto.getSoChoToiDa() < soChoDaDangKy) {
            throw new IllegalArgumentException(
                    "Số chỗ tối đa mới không được nhỏ hơn " +
                    "số chỗ đã đăng ký: " + soChoDaDangKy
            );
        }

        course.setTenMonHoc(tenMonHoc);
        course.setSoTinChi(dto.getSoTinChi());
        course.setSoChoToiDa(dto.getSoChoToiDa());

        /*
         * Client không được tự truyền soChoConLai.
         * Service tự tính lại dựa trên số chỗ đã đăng ký.
         */
        course.setSoChoConLai(
                dto.getSoChoToiDa() - soChoDaDangKy
        );

        Course updatedCourse = courseRepository.save(course);

        return toDTO(updatedCourse);
    }

    /**
     * Xóa môn học.
     */
    @Transactional
    public void delete(Long id) {
        Course course = findCourseById(id);

        courseRepository.delete(course);
    }

    /**
     * Tìm Entity theo id.
     * Nếu không có thì phát sinh lỗi 404.
     */
    private Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Không tìm thấy môn học có id = " + id
                ));
    }

    /**
     * Chuẩn hóa tên môn học:
     * - Xóa khoảng trắng đầu và cuối.
     * - Các khoảng trắng liên tiếp được đổi thành một khoảng trắng.
     */
    private String normalizeName(String tenMonHoc) {
        return tenMonHoc
                .trim()
                .replaceAll("\\s+", " ");
    }

    /**
     * Chuyển Entity thành DTO.
     */
    private CourseDTO toDTO(Course course) {
        return new CourseDTO(
                course.getId(),
                course.getTenMonHoc(),
                course.getSoTinChi(),
                course.getSoChoToiDa(),
                course.getSoChoConLai()
        );
    }
}