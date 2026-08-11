package vn.edu.crs.registrationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.crs.registrationservice.client.CourseClient;
import vn.edu.crs.registrationservice.dto.RegistrationRequestDTO;
import vn.edu.crs.registrationservice.entity.Registration;
import vn.edu.crs.registrationservice.repository.RegistrationRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private static final String DA_DANG_KY =
            "DA_DANG_KY";

    private static final String DA_HUY =
            "DA_HUY";

    private final RegistrationRepository
            registrationRepository;

    private final CourseClient courseClient;

    /**
     * Đăng ký môn học.
     */
    public Registration register(
            RegistrationRequestDTO dto
    ) {

        // =========================================
        // BƯỚC 1:
        // kiểm tra sinh viên đã đăng ký chưa
        // =========================================

        boolean existed =
                registrationRepository
                        .existsByStudentIdAndCourseIdAndTrangThai(
                                dto.getStudentId(),
                                dto.getCourseId(),
                                DA_DANG_KY
                        );

        if (existed) {

            throw new IllegalStateException(
                    "Sinh viên đã đăng ký môn học này rồi"
            );
        }

        // =========================================
        // BƯỚC 2:
        // gọi course-service để trừ chỗ
        // =========================================

        courseClient.reserveSeat(
                dto.getCourseId()
        );

        // Nếu reserveSeat lỗi
        // chương trình dừng ở trên
        // và không tạo Registration.

        // =========================================
        // BƯỚC 3:
        // tạo Registration
        // =========================================

        Registration registration =
                new Registration();

        registration.setStudentId(
                dto.getStudentId()
        );

        registration.setCourseId(
                dto.getCourseId()
        );

        registration.setTrangThai(
                DA_DANG_KY
        );

        registration.setNgayDangKy(
                LocalDateTime.now()
        );

        // =========================================
        // BƯỚC 4:
        // lưu vào registration_db
        // =========================================

        return registrationRepository.save(
                registration
        );
    }

    /**
     * Hủy đăng ký.
     */
    public void cancel(
            Long registrationId
    ) {

        // =========================================
        // BƯỚC 1:
        // tìm Registration
        // =========================================

        Registration registration =
                registrationRepository
                        .findById(registrationId)
                        .orElseThrow(
                                () ->
                                        new NoSuchElementException(
                                                "Không tìm thấy đăng ký id = "
                                                        + registrationId
                                        )
                        );

        // =========================================
        // BƯỚC 2:
        // kiểm tra đã hủy chưa
        // =========================================

        if (DA_HUY.equals(
                registration.getTrangThai()
        )) {

            throw new IllegalStateException(
                    "Đăng ký này đã được hủy trước đó"
            );
        }

        // =========================================
        // BƯỚC 3:
        // gọi course-service hoàn lại chỗ
        // =========================================

        courseClient.releaseSeat(
                registration.getCourseId()
        );

        // =========================================
        // BƯỚC 4:
        // đổi trạng thái
        // =========================================

        registration.setTrangThai(
                DA_HUY
        );

        // =========================================
        // BƯỚC 5:
        // lưu DB
        // =========================================

        registrationRepository.save(
                registration
        );
    }
}