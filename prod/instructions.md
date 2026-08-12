cd /home/ubuntu/hotdeals-back/prod
chmod +x *.sh

# One-time setup (run in order):
sudo ./1_setup_firewall.sh
sudo ./2_setup_ssl.sh
sudo ./3_setup_springboot.sh

# Deploy & re-deploy:
./4_deploy_prod.sh