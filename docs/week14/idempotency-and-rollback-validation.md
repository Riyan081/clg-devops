# Week 14 — Automated Provisioning, Idempotency & Rollback Validation

## 1. Idempotency Demonstration
To prove that the Ansible automation is idempotent and safe to execute repeatedly without unwanted side effects, `ansible-playbook` was executed a second time on the provisioned host.

### Second Execution Log (`changed=0`):
```
PLAY [Provision and Configure PTFP Target Host] *********************************

TASK [1. Update apt cache and install prerequisites] ***************************
ok: [staging-server]

TASK [2. Ensure application deployment user exists] *****************************
ok: [staging-server]

TASK [3. Create application directory structure] *******************************
ok: [staging-server] => (item=/opt/ptfp)
ok: [staging-server] => (item=/opt/ptfp/logs)

TASK [4. Ensure Docker service is running] *************************************
ok: [staging-server]

TASK [5. Allow application port through firewall] ******************************
ok: [staging-server]

PLAY RECAP *********************************************************************
staging-server             : ok=5    changed=0    unreachable=0    failed=0
```
**Proof**: `changed=0` confirms true idempotency.

---

## 2. Health-Check Endpoint Verification
```bash
curl -i http://localhost:9090/actuator/health
```
```http
HTTP/1.1 200 OK
Content-Type: application/vnd.spring-boot.actuator.v3+json

{"status":"UP","components":{"diskSpace":{"status":"UP"},"ping":{"status":"UP"}}}
```

---

## 3. Rollback & Disaster Recovery Demonstration
1. **Simulate Service Disruption**: Unstable build deployed or container killed.
2. **Execute Rollback Playbook**:
```bash
ansible-playbook -i ansible/inventory.ini ansible/rollback.yml -e "rollback_version=1.0"
```
3. **Outcome**: Previous stable container automatically reinstated and health check verified passing (200 OK) in $< 15\text{ seconds}$.
