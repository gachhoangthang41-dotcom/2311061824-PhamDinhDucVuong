# CRS Microservices

## Dịch vụ

| Service | Port | Vai trò |
| --- | --- | --- |
| api-gateway | 8080 | Điểm vào duy nhất cho API public |
| auth-service | 8081 | Đăng nhập và cấp JWT |
| course-service | 8082 | Quản lý học phần |
| registration-service | 8083 | Đăng ký học phần |

## Tài khoản mẫu

| Username | Password | Role |
| --- | --- | --- |
| `admin` | `admin123` | `ADMIN` |
| `student1` | `student123` | `STUDENT` |

Mọi request public nên gọi qua Gateway tại `http://localhost:8080`.

## Cấu trúc thư mục

```text
crs-frontend/
services/
  api-gateway/
  auth-service/
  course-service/
  registration-service/
```

## Cấu hình môi trường và chạy service

Các giá trị kết nối database, JWT, API key, port và URL giữa service nằm trong file `.env` ở thư mục gốc; file này đã được Git bỏ qua. Để tạo file cho một máy mới:

```bash
cp .env.example .env
```

Điền lại user/password MySQL trong `.env`, rồi chạy mỗi service ở một terminal:

```bash
./scripts/run-service.sh auth
./scripts/run-service.sh course
./scripts/run-service.sh registration
./scripts/run-service.sh gateway
```

Script tự nạp `.env` trước khi chạy Maven. Khởi động MySQL và tạo các database `auth_db`, `course_db`, `registration_db` trước khi chạy service tương ứng. Khởi động theo thứ tự `auth`, `course`, `registration`, rồi `gateway`.
