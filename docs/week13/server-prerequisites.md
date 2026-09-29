# Week 13 — Server Prerequisites & Ansible Configuration Specification

## 1. Target Node Prerequisites Matrix

| Requirement Area | Specification | Purpose |
|---|---|---|
| **OS & Kernel** | Ubuntu 22.04 LTS (x86_64) | Production-grade hosting target |
| **System Packages** | `curl`, `git`, `openjdk-17-jre-headless`, `ufw`, `docker.io` | Execution environment dependencies |
| **Service User** | `deploy` (`uid: 1001`, `gid: 1001`) with sudo privileges | Non-root secure application runner |
| **Directory Paths** | `/opt/ptfp`, `/opt/ptfp/logs` (`mode: 0755`) | Application storage & log archiving |
| **Firewall Ports** | Port `9090` (TCP, Inbound) | Application access port |
| **System Services** | `docker` daemon enabled and active | Container orchestration |

---

## 2. Ansible Playbook Execution Log
```bash
ansible-playbook -i ansible/inventory.ini ansible/playbook.yml
```

**Output Log**:
```
PLAY [Provision and Configure PTFP Target Host] *********************************

TASK [1. Update apt cache and install prerequisites] ***************************
changed: [staging-server]

TASK [2. Ensure application deployment user exists] *****************************
changed: [staging-server]

TASK [3. Create application directory structure] *******************************
changed: [staging-server] => (item=/opt/ptfp)
changed: [staging-server] => (item=/opt/ptfp/logs)

TASK [4. Ensure Docker service is running] *************************************
ok: [staging-server]

TASK [5. Allow application port through firewall] ******************************
changed: [staging-server]

PLAY RECAP *********************************************************************
staging-server             : ok=5    changed=4    unreachable=0    failed=0
```
