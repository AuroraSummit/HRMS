#!/bin/bash
# Test all fixed APIs
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/hr/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | \
  python -c "import sys,json; print(json.load(sys.stdin)['data']['accessToken'])")

echo "=== 1. 请假管理 - employeeName ==="
curl -s "http://localhost:8080/api/v1/hr/attendance/leave/page?page=1&pageSize=10" \
  -H "Authorization: Bearer $TOKEN" | \
  python -c "import sys,json; [print(f'  {r[\"employeeName\"]}') for r in json.load(sys.stdin)['data']['records']]"

echo "=== 2. 员工详情 - deptName/positionName ==="
curl -s "http://localhost:8080/api/v1/hr/employee/1" \
  -H "Authorization: Bearer $TOKEN" | \
  python -c "import sys,json; d=json.load(sys.stdin)['data']; print(f'  deptName={d[\"deptName\"]}, positionName={d[\"positionName\"]}')"

echo "=== 3. 招聘需求 - deptName/positionName ==="
curl -s "http://localhost:8080/api/v1/hr/recruitment/demand/page?page=1&pageSize=10" \
  -H "Authorization: Bearer $TOKEN" | \
  python -c "import sys,json; [print(f'  {r[\"title\"]}: dept={r[\"deptName\"]}, pos={r[\"positionName\"]}') for r in json.load(sys.stdin)['data']['records']]"

echo "=== 4. 面试/Offer - 有数据 ==="
echo "--- 面试 ---"
curl -s "http://localhost:8080/api/v1/hr/recruitment/interview/page?page=1&pageSize=10" \
  -H "Authorization: Bearer $TOKEN" | \
  python -c "import sys,json; [print(f'  {r[\"candidateName\"]}') for r in json.load(sys.stdin)['data']['records']]"
echo "--- Offer ---"
curl -s "http://localhost:8080/api/v1/hr/recruitment/offer/page?page=1&pageSize=10" \
  -H "Authorization: Bearer $TOKEN" | \
  python -c "import sys,json; [print(f'  {r[\"candidateName\"]}') for r in json.load(sys.stdin)['data']['records']]"

echo "=== 5. 薪酬配置 ==="
curl -s "http://localhost:8080/api/v1/hr/salary/config/page?page=1&pageSize=10" \
  -H "Authorization: Bearer $TOKEN" | \
  python -c "import sys,json; [print(f'  {r[\"name\"]}: basic={r[\"basicSalary\"]}') for r in json.load(sys.stdin)['data']['records']]"

echo "=== 6. 合同(张三) ==="
curl -s "http://localhost:8080/api/v1/hr/contract/page?page=1&pageSize=10&empId=1" \
  -H "Authorization: Bearer $TOKEN" | \
  python -c "import sys,json; [print(f'  type={r[\"contractType\"]}') for r in json.load(sys.stdin)['data']['records']]"

echo ""
echo "全部接口验证完成！"
