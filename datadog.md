Introduction to Datadog
Installing the Datadog Agent on Linux, Docker
Introduction to the Datadog UI
Introduction to Infrastructure Monitoring
Monitoring Hosts
Monitoring Docker and Kubernetes


Setting up Datadog agent on Kubernetes
Host Maps and Container Maps
Basic Integrations with AWS
Understanding metrics in Datadog
Types of metrics (gauge, counter, histogram)
Collecting and visualizing metrics

Custom Metrics in DataDog
Sending custom metrics to Datadog
Building basic dashboards for metrics
Creating custom dashboards
Timeboards and Screenboards

Introduction to Log Management
Collecting logs with the Datadog agent
Introduction to alerting in Datadog
Creating monitors and alerts

---

### Install Datadog Agent on Ubuntu
systemctl start datadog-agent
systemctl status datadog-agent

## CPU Monitoring

apt install stress -y
stress --cpu 2 --timeout 120

## Memory Monitoring

stress --vm 1 --vm-bytes 512M --timeout 120

## Log Monitoring

vim /etc/datadog-agent/datadog.yaml

logs_enabled: true

systemctl restart datadog-agent

## System Logs

vim /etc/datadog-agent/conf.d/system_logs.d/conf.yaml

logs:
  - type: file
    path: /var/log/syslog
    service: ubuntu
    source: syslog

## Docker Monitoring

apt install docker.io -y
systemctl start docker

## Enable Docker Integration

vim /etc/datadog-agent/conf.d/docker.d/conf.yaml       #Integration configs file

## Process Collection

vim /etc/datadog-agent/datadog.yaml   ## main configuration file

process_config:
  enabled: "true"

systemctl restart datadog-agent
datadog-agent status | grep -i process

## Validate Agent Configuration

datadog-agent configcheck

## Agent logs
/var/log/datadog/

<<<<<<< HEAD
Introduction to Datadog
Installing the Datadog Agent on Linux, Docker
Introduction to the Datadog UI
Introduction to Infrastructure Monitoring
Monitoring Hosts
Monitoring Docker and Kubernetes


Setting up Datadog agent on Kubernetes
Host Maps and Container Maps
Basic Integrations with AWS
Understanding metrics in Datadog
Types of metrics (gauge, counter, histogram)
Collecting and visualizing metrics

Custom Metrics in DataDog
Sending custom metrics to Datadog
Building basic dashboards for metrics
Creating custom dashboards
Timeboards and Screenboards

Introduction to Log Management
Collecting logs with the Datadog agent
Introduction to alerting in Datadog
Creating monitors and alerts
=======

###  Install Datadog Agent on Ubuntu
systemctl start datadog-agent
systemctl status datadog-agent

## CPU Monitoring

apt install stress -y
stress --cpu 2 --timeout 120

## Memory Monitoring

stress --vm 1 --vm-bytes 512M --timeout 120

## Log Monitoring

vim /etc/datadog-agent/datadog.yaml

logs_enabled: true

systemctl restart datadog-agent

## System Logs

vim /etc/datadog-agent/conf.d/system_logs.d/conf.yaml

logs:
  - type: file
    path: /var/log/syslog
    service: ubuntu
    source: syslog

## Docker Monitoring

 apt install docker.io -y
 systemctl start docker


## Enable Docker Integration

vim /etc/datadog-agent/conf.d/docker.d/conf.yaml       #Integration configs file

## Process Collection

vim /etc/datadog-agent/datadog.yaml   ## main configuration file

process_config:
  enabled: "true"


systemctl restart datadog-agent
datadog-agent status | grep -i process


## Validate Agent Configuration

datadog-agent configcheck


## Agent logs
/var/log/datadog/






>>>>>>> c3095af (Add Datadog documentation)

