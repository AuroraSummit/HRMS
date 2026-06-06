import subprocess, json

# Get valid hash using subprocess
result = subprocess.run(
    ['mysql', '-u', 'root', '-plwp20040411', '-h', 'localhost', 'hrms',
     '-e', "SELECT password FROM auth_user WHERE id=3"],
    capture_output=True, text=True
)
lines = result.stdout.strip().split('\n')
valid_hash = lines[1].strip() if len(lines) > 1 else ''
print(f"Hash ({len(valid_hash)} chars)")

# Update all broken accounts using a temp SQL file
sql = f"UPDATE auth_user SET password='{valid_hash}' WHERE LENGTH(password) < 40;"
result2 = subprocess.run(
    ['mysql', '-u', 'root', '-plwp20040411', '-h', 'localhost', 'hrms', '-e', sql],
    capture_output=True, text=True
)
print(f"Update stderr: {result2.stderr}")

# Verify
result3 = subprocess.run(
    ['mysql', '-u', 'root', '-plwp20040411', '-h', 'localhost', 'hrms',
     '-e', "SELECT id, username, LENGTH(password) as len FROM auth_user ORDER BY id"],
    capture_output=True, text=True
)
print(result3.stdout)
