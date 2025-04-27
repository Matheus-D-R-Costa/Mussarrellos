Vagrant.configure("2") do |config|
  # Timeout de boot aumentado para 10 minutos
  config.vm.boot_timeout = 600
  
  # Box base
  config.vm.box = "debian/bullseye64"
  
  # Configurações gerais
  config.vm.hostname = "server"
  
  # Configuração da rede - voltando para private_network com IP fixo para evitar problemas
  config.vm.network "private_network", ip: "192.168.56.10"
  
  # Pasta compartilhada
  config.vm.synced_folder "./data", "/opt/mussarellos", create: true
  
  # Configurações de SSH
  config.ssh.insert_key = true
  config.ssh.forward_agent = true
  config.ssh.keep_alive = true
  config.ssh.forward_x11 = false
  
  # Provedor VirtualBox com recursos adequados
  config.vm.provider :virtualbox do |vb|
    vb.name = "server"
    vb.memory = 4096
    vb.cpus = 2
    vb.gui = false
    vb.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]
  end
  
  # Provisioning
  config.vm.provision "shell", inline: <<-SHELL
    # Atualização e utilitários
    apt-get update
    apt-get install -y apt-transport-https ca-certificates curl gnupg lsb-release openssh-server
    
    # Garante que o SSH esteja configurado e funcionando
    systemctl enable --now ssh

    # Java
    apt-get install openjdk-17-jdk
    
    # Docker CE
    curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/debian $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
    apt-get update
    apt-get install -y docker-ce docker-ce-cli containerd.io
    systemctl enable --now docker
    
    # Docker Compose
    curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" \
        -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
    
    # Nginx
    apt-get install -y nginx
    systemctl enable --now nginx
    
    # Firewall
    apt-get install -y ufw
    ufw allow 22/tcp  # Garante SSH
    ufw allow 80/tcp
    ufw --force enable
    
    # Verificações finais
    echo "SSH está:" $(systemctl is-active ssh)
    echo "Nginx está:" $(systemctl is-active nginx)
    echo "Docker está:" $(systemctl is-active docker)
  SHELL
end
