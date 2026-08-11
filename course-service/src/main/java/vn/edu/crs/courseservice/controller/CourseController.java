package vn.edu.crs.courseservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.crs.courseservice.dto.CourseDTO;
import vn.edu.crs.courseservice.service.CourseService;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /**
     * Buổi 3:
     * GET /courses
     *
     * Hỗ trợ:
     * keyword
     * page
     * size
     * sort
     */
    @GetMapping
    public Page<CourseDTO> search(
            @RequestParam(name = "search", required = false)
            String search,

            @RequestParam(name = "keyword", required = false)
            String keyword,

            Pageable pageable
    ) {

        return courseService.search(
                search != null ? search : keyword,
                pageable
        );
    }

    /**
     * GET /courses/{id}
     */
    @GetMapping("/{id}")
    public CourseDTO getById(
            @PathVariable Long id
    ) {

        return courseService.getById(id);
    }

    /**
     * POST /courses
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO create(
            @Valid
            @RequestBody
            CourseDTO dto
    ) {

        return courseService.create(dto);
    }

    /**
     * PUT /courses/{id}
     */
    @PutMapping("/{id}")
    public CourseDTO update(
            @PathVariable Long id,

            @Valid
            @RequestBody
            CourseDTO dto
    ) {

        return courseService.update(id, dto);
    }

    /**
     * DELETE /courses/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {

        courseService.delete(id);
    }
}
