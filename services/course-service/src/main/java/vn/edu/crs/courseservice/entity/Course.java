package vn.edu.crs.courseservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entity mapping for the course table owned only by course-service. */
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_mon_hoc", nullable = false, length = 255)
    private String tenMonHoc;

    @Column(name = "so_tin_chi", nullable = false)
    private Integer soTinChi;

    @Column(name = "so_cho_toi_da", nullable = false)
    private Integer soChoToiDa;

    @Column(name = "so_cho_con_lai", nullable = false)
    private Integer soChoConLai;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenMonHoc() { return tenMonHoc; }
    public void setTenMonHoc(String tenMonHoc) { this.tenMonHoc = tenMonHoc; }
    public Integer getSoTinChi() { return soTinChi; }
    public void setSoTinChi(Integer soTinChi) { this.soTinChi = soTinChi; }
    public Integer getSoChoToiDa() { return soChoToiDa; }
    public void setSoChoToiDa(Integer soChoToiDa) { this.soChoToiDa = soChoToiDa; }
    public Integer getSoChoConLai() { return soChoConLai; }
    public void setSoChoConLai(Integer soChoConLai) { this.soChoConLai = soChoConLai; }
}
