#!/bin/bash
# Login and get token
RESP=$(curl -s -X POST http://localhost:8080/api/v1/hr/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')
TOKEN=$(echo $RESP | python -c "import sys,json; print(json.load(sys.stdin)['data']['accessToken'])")
echo "=== 部门树接口返回 ==="
curl -s "http://localhost:8080/api/v1/hr/org/dept/tree" \
  -H "Authorization: Bearer $TOKEN"
echo ""
echo "=== 部门详情(id=1) ==="
curl -s "http://localhost:8080/api/v1/hr/org/dept/1" \
  -H "Authorization: Bearer $TOKEN"
