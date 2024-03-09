# Advanced Message Queuing Protocol Demo

Projeto para demonstração do protocolo AMQP por meio do RabbitMQ.

# Pré-requisitos

Possuir instalado:

    - jdk 17
    - Maven 3.6.3
    - Docker 25.0.3

# Build

```shell
./build.sh
```

# Execução

```shell
docker compose up -d
```

# Visualização da Execução

Ative o monitoramento de log da aplicação amqpdemo:

```shell
docker logs --follow --tail 1 amqpdemo
```

Acesse a interface do gerenciador de mensagens do RabbitMQ:

[http://localhost:15672](http://localhost:15672)

Acesse o endereço que dispara as mensagens:

[http://localhost:8080/olamundo](http://localhost:8080/olamundo)