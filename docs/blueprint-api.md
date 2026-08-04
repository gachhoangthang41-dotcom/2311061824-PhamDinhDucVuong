# Blueprint API

API trao đổi JSON. Các endpoint public đi qua API Gateway; endpoint bắt đầu `/internal` chỉ dành cho giao tiếp giữa service.

## auth-service — cổng 8081, Gateway: `/api/auth`

| Method | Endpoint | Mô tả | Quyền |
| --- | --- | --- | --- |
| POST | `/auth/login` | Đăng nhập, trả JWT | Public |
| POST | `/auth/register` | Đăng ký tài khoản | Public |

## course-service — cổng 8082, Gateway: `/api/courses`

| Method | Endpoint | Mô tả | Quyền |
| --- | --- | --- | --- |
| GET | `/courses?search=&page=&size=` | Danh sách, tìm kiếm và phân trang | Public |
| GET | `/courses/{id}` | Chi tiết học phần | Public |
| POST | `/courses` | Thêm học phần | ADMIN |
| PUT | `/courses/{id}` | Sửa học phần | ADMIN |
| DELETE | `/courses/{id}` | Xóa học phần | ADMIN |

### API nội bộ course-service

| Method | Endpoint | Mô tả |
| --- | --- | --- |
| PATCH | `/internal/courses/{id}/reserve-seat` | Kiểm tra còn chỗ và giảm `soChoConLai` trong transaction. |
| PATCH | `/internal/courses/{id}/release-seat` | Hoàn một chỗ khi hủy đăng ký. |

## registration-service — cổng 8083, Gateway: `/api/registrations`

| Method | Endpoint | Mô tả | Quyền |
| --- | --- | --- | --- |
| POST | `/registrations` | Đăng ký học phần; gọi ngầm `reserve-seat` | STUDENT |
| GET | `/registrations/my` | Danh sách đăng ký của người dùng hiện tại | STUDENT |
| DELETE | `/registrations/{id}` | Hủy đăng ký; gọi ngầm `release-seat` | STUDENT / ADMIN |
