# Thiết kế biên giới Service

## Bốn thành phần

| Thành phần | Cổng | Database riêng | Trách nhiệm chính |
| --- | ---: | --- | --- |
| API Gateway | 8080 | Không có DB | Điểm vào duy nhất, định tuyến, CORS và xác thực sơ bộ. |
| auth-service | 8081 | `auth_db` | Quản lý user, student, đăng nhập và JWT. |
| course-service | 8082 | `course_db` | Quản lý học phần, tìm kiếm, phân trang và số chỗ. |
| registration-service | 8083 | `registration_db` | Quản lý đăng ký/hủy đăng ký, gọi API nội bộ của course-service. |

## Nguyên tắc sở hữu dữ liệu

- Mỗi service sở hữu **một database riêng**; không service nào truy cập trực tiếp database của service khác.
- Muốn đọc hoặc thay đổi dữ liệu thuộc service khác phải gọi REST API của service đó.
- `registration-service` chỉ lưu `courseId` và `studentId`; không có bảng `Course` và không tạo khóa ngoại sang `course_db`.
- Chỉ `course-service` được thay đổi số chỗ còn lại của học phần.

## Bảng định tuyến Gateway dự kiến

| Route từ client | Chuyển tiếp tới | Ghi chú |
| --- | --- | --- |
| `/api/auth/**` | `http://localhost:8081` | `login` là public; phần còn lại cần JWT. |
| `/api/courses/**` | `http://localhost:8082` | GET public; POST/PUT/DELETE cần role `ADMIN`. |
| `/api/registrations/**` | `http://localhost:8083` | Cần JWT, role `STUDENT` hoặc `ADMIN`. |
| `/api/public/courses` | `http://localhost:8082` | Dùng API Key cho đối tác ngoài. |

Các route `/internal/**` không công khai qua Gateway; chúng chỉ phục vụ giao tiếp service-to-service.
