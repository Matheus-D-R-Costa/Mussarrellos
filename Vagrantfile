Vagrant.configure("2") do |config|
  config.vm.box = "rockylinux/8"
  config.vm.box_version = ">= 0"

  config.vm.define "rocky8" do |rocky8|
    rocky8.vm.host_name = "rocky8"
    rocky8.vm.network "private_network", ip: "192.168.56.10"
    rocky8.vm.synced_folder "./data", "/root/data"

    rocky8.vm.provider "virtualbox" do |vb|
      vb.name = "rocky8"
      vb.memory = 4096
      vb.cpus = 2
      vb.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]
    end

    rocky8.vm.provision "shell", inline: <<-SHELL
      dnf update -y
      dnf install -y dnf-utils device-mapper-persistent-data lvm2
      dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo
      dnf install -y docker-ce docker-ce-cli containerd.io
      systemctl start docker
      systemctl enable docker
      curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
      chmod +x /usr/local/bin/docker-compose
      echo "Docker version:"
      docker --version
      echo "Docker Compose version:"
      docker-compose --version

      # Instalação do Nginx
      cat > /etc/yum.repos.d/nginx.repo << 'EOF'
[nginx]
name=nginx repo
baseurl=http://nginx.org/packages/mainline/rhel/8/$basearch/
gpgcheck=0
enabled=1
EOF

      # Instalar e configurar Nginx
      dnf install -y nginx
      systemctl enable nginx
      systemctl start nginx

      # Configurar firewall
      firewall-cmd --permanent --zone=public --add-port=80/tcp
      firewall-cmd --reload

      # Verificar status do Nginx
      systemctl status nginx
    SHELL
  end
end
