package com.hrms.module.employee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.common.exception.BusinessException;
import com.hrms.module.employee.dao.EducationDao;
import com.hrms.module.employee.dao.EmployeeDao;
import com.hrms.module.employee.dao.WorkExperienceDao;
import com.hrms.module.org.dao.DepartmentDao;
import com.hrms.module.org.dao.PositionDao;
import com.hrms.module.org.entity.Department;
import com.hrms.module.org.entity.Position;
import com.hrms.module.employee.dto.CreateEmployeeDto;
import com.hrms.module.employee.dto.EmployeeQueryDto;
import com.hrms.module.employee.entity.Education;
import com.hrms.module.employee.entity.Employee;
import com.hrms.module.employee.entity.WorkExperience;
import com.hrms.module.employee.service.EmployeeService;
import com.hrms.module.employee.vo.EmployeeDetailVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements EmployeeService {

    private final EmployeeDao employeeDao;
    private final EducationDao educationDao;
    private final WorkExperienceDao workExperienceDao;
    private final DepartmentDao departmentDao;
    private final PositionDao positionDao;

    @Override
    public Page<EmployeeDetailVO> pageQuery(EmployeeQueryDto queryDto) {
        Page<EmployeeDetailVO> page = new Page<>(queryDto.getPage(), queryDto.getSize());
        return employeeDao.selectEmployeeWithDeptAndPosition(page, queryDto);
    }

    @Override
    public EmployeeDetailVO getDetail(Long id) {
        Employee employee = employeeDao.selectById(id);
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }

        EmployeeDetailVO vo = new EmployeeDetailVO();
        vo.setId(employee.getId());
        vo.setEmpNo(employee.getEmpNo());
        vo.setName(employee.getName());
        vo.setGender(employee.getGender());
        vo.setBirthday(employee.getBirthday());
        vo.setIdCard(employee.getIdCard());
        vo.setPhone(employee.getPhone());
        vo.setEmail(employee.getEmail());
        vo.setAvatar(employee.getAvatar());
        vo.setDeptId(employee.getDeptId());
        vo.setPositionId(employee.getPositionId());
        vo.setHireDate(employee.getHireDate());
        vo.setStatus(employee.getStatus());
        vo.setEmploymentType(employee.getEmploymentType());
        vo.setSource(employee.getSource());
        vo.setEmergencyContact(employee.getEmergencyContact());
        vo.setEmergencyPhone(employee.getEmergencyPhone());
        vo.setCreateTime(employee.getCreateTime());
        vo.setUpdateTime(employee.getUpdateTime());

        // 填充部门名称和岗位名称
        if (employee.getDeptId() != null) {
            Department dept = departmentDao.selectById(employee.getDeptId());
            if (dept != null) vo.setDeptName(dept.getName());
        }
        if (employee.getPositionId() != null) {
            Position pos = positionDao.selectById(employee.getPositionId());
            if (pos != null) vo.setPositionName(pos.getName());
        }

        List<Education> eduList = educationDao.selectList(
                new LambdaQueryWrapper<Education>().eq(Education::getEmpId, id));
        vo.setEducationList(eduList);

        List<WorkExperience> workList = workExperienceDao.selectList(
                new LambdaQueryWrapper<WorkExperience>().eq(WorkExperience::getEmpId, id));
        vo.setWorkExperienceList(workList);

        return vo;
    }

    @Override
    @Transactional
    public Employee create(CreateEmployeeDto dto) {
        // Auto-generate employee number
        String empNo = "EMP" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + String.format("%04d", (int) (Math.random() * 10000));
        dto.setEmpNo(empNo);

        employeeDao.insert(dto);

        if (dto.getEducationList() != null) {
            for (Education edu : dto.getEducationList()) {
                edu.setEmpId(dto.getId());
                educationDao.insert(edu);
            }
        }

        if (dto.getWorkExperienceList() != null) {
            for (WorkExperience exp : dto.getWorkExperienceList()) {
                exp.setEmpId(dto.getId());
                workExperienceDao.insert(exp);
            }
        }

        return dto;
    }

    @Override
    @Transactional
    public Employee update(Employee employee) {
        employeeDao.updateById(employee);
        return employee;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        employeeDao.deleteById(id);
        educationDao.delete(new LambdaQueryWrapper<Education>().eq(Education::getEmpId, id));
        workExperienceDao.delete(new LambdaQueryWrapper<WorkExperience>().eq(WorkExperience::getEmpId, id));
    }

    @Override
    public void export(EmployeeQueryDto queryDto, HttpServletResponse response) {
        // Stub for Excel export
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=employees.xlsx");
    }
}
