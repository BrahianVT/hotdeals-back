cd /home/ubuntu/hotdeals-back/prod
chmod +x *.sh

# One-time setup (run in order):
sudo ./1_setup_firewall.sh
sudo ./2_setup_ssl.sh
sudo ./3_setup_springboot.sh

# Deploy & re-deploy:
./4_deploy_prod.sh



## Errors

Caused by: java.net.BindException: Permission denied

Step 1: Grant Java Permission to Bind to Port 443:

# 1. Grant network bind capabilities to Java
sudo setcap 'cap_net_bind_service=+ep' $(readlink -f $(which java))

# 2. Verify the capability was applied (it should print: .../java cap_net_bind_service=ep)
getcap $(readlink -f $(which java))



To ensure this permission never breaks during future Java/system updates, allow non-root users to bind to web ports:

sudo sysctl -w net.ipv4.ip_unprivileged_port_start=80
echo "net.ipv4.ip_unprivileged_port_start=80" | sudo tee -a /etc/sysctl.conf