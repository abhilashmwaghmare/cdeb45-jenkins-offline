### Datadog Agent – Practicals on Ubuntu

This lab guide walks through installing the Datadog Agent and enabling monitoring for CPU, memory, logs, Docker, and processes on Ubuntu.

---

## 1. Install Datadog Agent on Ubuntu

**Objective**: Install and run the Datadog Agent on an Ubuntu host.

### 1.1 Prerequisites

- **Ubuntu** server with `sudo` access  
- **Datadog API key** from the Datadog UI  
- Know your **Datadog site**:
  - `datadoghq.com` (US1, default)
  - `datadoghq.eu` (EU1), or others as per your account (adjust `DD_SITE`)

### 1.2 Install the Agent

```bash
DD_API_KEY="<YOUR_DATADOG_API_KEY>" \
DD_SITE="datadoghq.com" \
bash -c "$(curl -L https://s3.amazonaws.com/dd-agent/scripts/install_script.sh)"
```

> If you are on EU site:

```bash
DD_API_KEY="<YOUR_DATADOG_API_KEY>" \
DD_SITE="datadoghq.eu" \
bash -c "$(curl -L https://s3.amazonaws.com/dd-agent/scripts/install_script.sh)"
```

### 1.3 Manage the Datadog Agent service

```bash
sudo systemctl start datadog-agent
sudo systemctl status datadog-agent
sudo systemctl stop datadog-agent
sudo systemctl restart datadog-agent
sudo systemctl enable datadog-agent
```

### 1.4 Verify the Agent

```bash
sudo datadog-agent status
```

Optional troubleshooting command:

```bash
sudo datadog-agent flare
```

---

## 2. CPU Monitoring Practical

**Objective**: Generate CPU load and observe it in Datadog.

### 2.1 Install `stress`

```bash
sudo apt update
sudo apt install stress -y
```

### 2.2 Generate CPU Load

```bash
stress --cpu 2 --timeout 120
```

Alternative:

```bash
stress --cpu 4 --timeout 300
```

### 2.3 Validate in Datadog

- In the Datadog UI, go to **Infrastructure → Host** (select your host).
- Observe metrics like:
  - `system.cpu.user`
  - `system.cpu.system`
  - `system.cpu.idle`

---

## 3. Memory Monitoring Practical

**Objective**: Generate memory pressure and visualize it in Datadog.

### 3.1 Generate Memory Load with `stress`

```bash
stress --vm 1 --vm-bytes 512M --timeout 120
```

Alternative:

```bash
stress --vm 2 --vm-bytes 512M --timeout 120
```

### 3.2 Validate in Datadog

- In Datadog, check metrics such as:
  - `system.mem.used`
  - `system.mem.total`
  - `system.swap.used`

---

## 4. Log Monitoring – Enable Logs Globally

**Objective**: Enable log collection at the Agent level.

### 4.1 Enable Logs in `datadog.yaml`

Open the main config file:

```bash
sudo vim /etc/datadog-agent/datadog.yaml
```

Set:

```yaml
logs_enabled: true
```

Save and exit (`:wq` in vim).

### 4.2 Restart Agent

```bash
sudo systemctl restart datadog-agent
sudo systemctl status datadog-agent
```

---

## 5. System Logs Practical (`/var/log/syslog`)

**Objective**: Collect system logs from `/var/log/syslog` into Datadog.

### 5.1 Create/Edit System Logs Integration Config

```bash
sudo mkdir -p /etc/datadog-agent/conf.d/system_logs.d
sudo vim /etc/datadog-agent/conf.d/system_logs.d/conf.yaml
```

Add the following content:

```yaml
logs:
  - type: file
    path: /var/log/syslog
    service: ubuntu
    source: syslog
```

Save and exit.

### 5.2 Restart Agent

```bash
sudo systemctl restart datadog-agent
sudo systemctl status datadog-agent
```

### 5.3 Generate Test Syslog Entries

```bash
logger "Test log from Datadog practicals"
logger "Another Datadog syslog test message"
```

### 5.4 Validate in Datadog

- In the Datadog UI, go to **Logs → Live Tail**.
- Search for:
  - `service:ubuntu`
  - or the log text: `Test log from Datadog practicals`

---

## 6. Docker Monitoring Practical

**Objective**: Monitor Docker engine and containers with Datadog.

### 6.1 Install Docker

```bash
sudo apt update
sudo apt install docker.io -y
```

### 6.2 Enable and Start Docker

```bash
sudo systemctl start docker
sudo systemctl enable docker
sudo systemctl status docker
```

### 6.3 (Optional) Ensure Agent User Has Docker Access

Depending on your setup, the Agent may run as `dd-agent` or `datadog-agent`. Example:

```bash
sudo usermod -aG docker dd-agent
sudo systemctl restart datadog-agent
```

### 6.4 Run a Test Container

```bash
sudo docker run --rm -d --name nginx-test -p 8080:80 nginx
sudo docker ps
```

---

## 7. Enable Docker Integration in Datadog

**Objective**: Configure the Datadog Agent to collect Docker metrics.

### 7.1 Create/Edit Docker Integration Config

```bash
sudo mkdir -p /etc/datadog-agent/conf.d/docker.d
sudo vim /etc/datadog-agent/conf.d/docker.d/conf.yaml
```

Minimal configuration:

```yaml
init_config:

instances:
  - collect_container_size: true
    collect_image_size: true
    collect_exit_codes: true
```

Save and exit.

### 7.2 Restart Agent

```bash
sudo systemctl restart datadog-agent
sudo systemctl status datadog-agent
```

### 7.3 Validate in Datadog

- In Datadog, go to **Infrastructure → Containers**.
- You should see:
  - The `nginx-test` container.
  - Docker metrics for containers and images.

---

## 8. Process Collection Practical

**Objective**: Enable and verify process monitoring with Datadog.

### 8.1 Enable Process Collection in `datadog.yaml`

```bash
sudo vim /etc/datadog-agent/datadog.yaml
```

Add or modify:

```yaml
process_config:
  enabled: "true"
```

Save and exit.

### 8.2 Restart Agent

```bash
sudo systemctl restart datadog-agent
sudo systemctl status datadog-agent
```

### 8.3 Verify Process Agent Status

```bash
sudo datadog-agent status | grep -i process
```

You should see output indicating the **Process Agent** is running.

### 8.4 Generate Load and Check in UI

Start a CPU-intensive command again:

```bash
stress --cpu 2 --timeout 120
```

In Datadog:

- Go to **Infrastructure → Live Processes**.
- Filter by your host and verify the `stress` process and others are visible.

---

## 9. Validate Agent Configuration

**Objective**: Confirm all configurations are valid and loaded.

### 9.1 Run Config Check

```bash
sudo datadog-agent configcheck
```

Review:

- That `system_logs.d`, `docker.d`, and other configs are listed.
- There are no errors shown for your integrations.

### 9.2 Full Status Check

```bash
sudo datadog-agent status
```

Look for sections:

- `Logs Agent`
- `Docker`
- `Processes`
- `Checks`

---

## 10. Agent Logs Practical

**Objective**: Use Agent logs for troubleshooting.

### 10.1 Explore Agent Log Directory

```bash
ls -R /var/log/datadog/
```

Common log files:

```bash
sudo tail -n 100 /var/log/datadog/agent.log
sudo tail -n 100 /var/log/datadog/process-agent.log
sudo tail -n 100 /var/log/datadog/trace-agent.log
```

### 10.2 Follow Logs in Real Time

```bash
sudo tail -f /var/log/datadog/agent.log
```

While `tail -f` is running, restart the Agent and watch for errors:

```bash
sudo systemctl restart datadog-agent
```

---

## 11. End-to-End Lab Flow (Quick Runbook)

Use this as a condensed practical sequence on a fresh Ubuntu VM:

```bash
# 1. Install Datadog Agent
DD_API_KEY="<YOUR_DATADOG_API_KEY>" DD_SITE="datadoghq.com" \
bash -c "$(curl -L https://s3.amazonaws.com/dd-agent/scripts/install_script.sh)"

# 2. Verify Agent
sudo datadog-agent status

# 3. Enable logs
sudo vim /etc/datadog-agent/datadog.yaml   # set logs_enabled: true and process_config: enabled: "true"

# 4. System logs integration
sudo mkdir -p /etc/datadog-agent/conf.d/system_logs.d
sudo vim /etc/datadog-agent/conf.d/system_logs.d/conf.yaml  # add syslog config

# 5. Docker install & test container
sudo apt update
sudo apt install docker.io -y
sudo systemctl start docker
sudo systemctl enable docker
sudo docker run --rm -d --name nginx-test -p 8080:80 nginx

# 6. Docker integration
sudo mkdir -p /etc/datadog-agent/conf.d/docker.d
sudo vim /etc/datadog-agent/conf.d/docker.d/conf.yaml       # add minimal docker config

# 7. Restart Agent
sudo systemctl restart datadog-agent

# 8. Stress tools
sudo apt install stress -y
stress --cpu 2 --timeout 120
stress --vm 1 --vm-bytes 512M --timeout 120

# 9. Validation
sudo datadog-agent configcheck
sudo datadog-agent status
ls -R /var/log/datadog/
```

Follow the sections above in order during training to see all metrics, logs, containers, and processes appear in Datadog.


